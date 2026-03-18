package dev.banditvault.lcebridge.core.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.banditvault.lcebridge.core.network.lce.LceItemStack;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Maps Java 1.21.11 protocol item IDs to legacy LCE item IDs and metadata.
 *
 * The lookup is assembled from local vanilla reports and the legacy source tree:
 * - java_item_protocol_ids.json: protocol item id -> Java item name
 * - java_block_default_state_ids.json: block item name -> default block state id
 * - legacy_item_name_to_id.json: legacy source item constant name -> numeric id
 */
public class ItemMappings {
    private static final Logger log = LoggerFactory.getLogger(ItemMappings.class);

    private record LegacyRef(String legacyName, int data) {
    }

    private record MappedItem(int id, int data) {
    }

    private final String[] protocolIdToName;
    private final Map<String, Integer> blockDefaultStateIds;
    private final Map<String, Integer> legacyItemIds;

    private ItemMappings(String[] protocolIdToName,
                         Map<String, Integer> blockDefaultStateIds,
                         Map<String, Integer> legacyItemIds) {
        this.protocolIdToName = protocolIdToName;
        this.blockDefaultStateIds = blockDefaultStateIds;
        this.legacyItemIds = legacyItemIds;
    }

    public static ItemMappings loadFromResource() {
        String[] protocolIdToName = loadItemProtocolNames();
        Map<String, Integer> blockDefaultStateIds = loadIntMap("/mappings/java_block_default_state_ids.json");
        Map<String, Integer> legacyItemIds = loadIntMap("/mappings/legacy_item_name_to_id.json");
        log.info("Loaded item lookup tables: protocolIds={}, blockDefaults={}, legacyIds={}",
            protocolIdToName.length, blockDefaultStateIds.size(), legacyItemIds.size());
        return new ItemMappings(protocolIdToName, blockDefaultStateIds, legacyItemIds);
    }

    public LceItemStack toLce(ItemStack item) {
        if (item == null || item.getAmount() <= 0) {
            return null;
        }

        MappedItem mapped = mapProtocolId(item.getId());
        if (mapped == null) {
            return null;
        }
        return new LceItemStack(mapped.id, item.getAmount(), mapped.data);
    }

    private MappedItem mapProtocolId(int protocolItemId) {
        if (protocolItemId < 0 || protocolItemId >= protocolIdToName.length) {
            return null;
        }

        String javaName = protocolIdToName[protocolItemId];
        if (javaName == null || javaName.isBlank()) {
            return null;
        }

        String bareName = stripNamespace(javaName);

        LegacyRef direct = directOverride(bareName);
        if (direct != null) {
            Integer legacyId = legacyItemIds.get(direct.legacyName());
            if (legacyId != null) {
                return new MappedItem(legacyId, direct.data());
            }
        }

        Integer blockDefaultStateId = blockDefaultStateIds.get(javaName);
        if (blockDefaultStateId != null) {
            return new MappedItem(
                MappingRegistry.blocks().getLceId(blockDefaultStateId),
                MappingRegistry.blocks().getLceData(blockDefaultStateId)
            );
        }

        Integer exactLegacyId = legacyItemIds.get(bareName);
        if (exactLegacyId != null) {
            return new MappedItem(exactLegacyId, 0);
        }

        String legacyName = heuristicLegacyName(bareName);
        if (legacyName != null) {
            Integer legacyId = legacyItemIds.get(legacyName);
            if (legacyId != null) {
                return new MappedItem(legacyId, 0);
            }
        }

        return null;
    }

    private static LegacyRef directOverride(String bareName) {
        return switch (bareName) {
            case "charcoal" -> new LegacyRef("coal", 1);
            case "redstone" -> new LegacyRef("redStone", 0);
            case "glowstone_dust" -> new LegacyRef("yellowDust", 0);
            case "porkchop" -> new LegacyRef("porkChop_raw", 0);
            case "cooked_porkchop" -> new LegacyRef("porkChop_cooked", 0);
            case "beef" -> new LegacyRef("beef_raw", 0);
            case "cod" -> new LegacyRef("fish_raw", 0);
            case "cooked_cod" -> new LegacyRef("fish_cooked", 0);
            case "mushroom_stew" -> new LegacyRef("mushroomStew", 0);
            case "golden_apple" -> new LegacyRef("apple_gold", 0);
            case "oak_sign", "spruce_sign", "birch_sign", "jungle_sign", "acacia_sign", "dark_oak_sign",
                 "mangrove_sign", "cherry_sign", "bamboo_sign", "crimson_sign", "warped_sign",
                 "oak_hanging_sign", "spruce_hanging_sign", "birch_hanging_sign", "jungle_hanging_sign",
                 "acacia_hanging_sign", "dark_oak_hanging_sign", "mangrove_hanging_sign",
                 "cherry_hanging_sign", "bamboo_hanging_sign", "crimson_hanging_sign",
                 "warped_hanging_sign" -> new LegacyRef("sign", 0);
            case "oak_door", "spruce_door", "birch_door", "jungle_door", "acacia_door",
                 "dark_oak_door", "mangrove_door", "cherry_door", "bamboo_door",
                 "crimson_door", "warped_door" -> new LegacyRef("door_wood", 0);
            case "iron_door" -> new LegacyRef("door_iron", 0);
            case "oak_boat", "spruce_boat", "birch_boat", "jungle_boat", "acacia_boat",
                 "dark_oak_boat", "mangrove_boat", "cherry_boat", "bamboo_raft" -> new LegacyRef("boat", 0);
            case "golden_carrot" -> new LegacyRef("carrotGolden", 0);
            case "gold_nugget" -> new LegacyRef("goldNugget", 0);
            case "ender_pearl" -> new LegacyRef("enderPearl", 0);
            case "blaze_rod" -> new LegacyRef("blazeRod", 0);
            case "ghast_tear" -> new LegacyRef("ghastTear", 0);
            case "nether_wart" -> new LegacyRef("netherwart_seeds", 0);
            case "glass_bottle" -> new LegacyRef("glassBottle", 0);
            case "spider_eye" -> new LegacyRef("spiderEye", 0);
            case "fermented_spider_eye" -> new LegacyRef("fermentedSpiderEye", 0);
            case "blaze_powder" -> new LegacyRef("blazePowder", 0);
            case "magma_cream" -> new LegacyRef("magmaCream", 0);
            case "experience_bottle" -> new LegacyRef("expBottle", 0);
            case "fire_charge" -> new LegacyRef("fireball", 0);
            case "item_frame" -> new LegacyRef("frame", 0);
            case "flower_pot" -> new LegacyRef("flowerPot", 0);
            case "baked_potato" -> new LegacyRef("potatoBaked", 0);
            case "poisonous_potato" -> new LegacyRef("potatoPoisonous", 0);
            case "carrot_on_a_stick" -> new LegacyRef("carrotOnAStick", 0);
            case "nether_star" -> new LegacyRef("netherStar", 0);
            case "pumpkin_pie" -> new LegacyRef("pumpkinPie", 0);
            case "firework_rocket" -> new LegacyRef("fireworks", 0);
            case "firework_star" -> new LegacyRef("fireworksCharge", 0);
            case "enchanted_book" -> new LegacyRef("enchantedBook", 0);
            case "nether_brick" -> new LegacyRef("netherbrick", 0);
            case "quartz" -> new LegacyRef("netherQuartz", 0);
            case "chest_minecart" -> new LegacyRef("minecart_chest", 0);
            case "furnace_minecart" -> new LegacyRef("minecart_furnace", 0);
            case "tnt_minecart" -> new LegacyRef("minecart_tnt", 0);
            case "hopper_minecart" -> new LegacyRef("minecart_hopper", 0);
            case "lead" -> new LegacyRef("lead", 0);
            case "name_tag" -> new LegacyRef("nameTag", 0);
            case "sugar_cane" -> new LegacyRef("reeds", 0);
            case "clay_ball" -> new LegacyRef("clay", 0);
            case "music_disc_13" -> new LegacyRef("record_01", 0);
            case "music_disc_cat" -> new LegacyRef("record_02", 0);
            case "music_disc_blocks" -> new LegacyRef("record_03", 0);
            case "music_disc_chirp" -> new LegacyRef("record_04", 0);
            case "music_disc_far" -> new LegacyRef("record_05", 0);
            case "music_disc_mall" -> new LegacyRef("record_06", 0);
            case "music_disc_mellohi" -> new LegacyRef("record_07", 0);
            case "music_disc_stal" -> new LegacyRef("record_09", 0);
            case "music_disc_strad" -> new LegacyRef("record_10", 0);
            case "music_disc_ward" -> new LegacyRef("record_11", 0);
            case "music_disc_11" -> new LegacyRef("record_12", 0);
            case "music_disc_wait" -> new LegacyRef("record_08", 0);
            case "black_dye" -> new LegacyRef("dye_powder", 0);
            case "red_dye" -> new LegacyRef("dye_powder", 1);
            case "green_dye" -> new LegacyRef("dye_powder", 2);
            case "brown_dye" -> new LegacyRef("dye_powder", 3);
            case "blue_dye" -> new LegacyRef("dye_powder", 4);
            case "purple_dye" -> new LegacyRef("dye_powder", 5);
            case "cyan_dye" -> new LegacyRef("dye_powder", 6);
            case "light_gray_dye" -> new LegacyRef("dye_powder", 7);
            case "gray_dye" -> new LegacyRef("dye_powder", 8);
            case "pink_dye" -> new LegacyRef("dye_powder", 9);
            case "lime_dye" -> new LegacyRef("dye_powder", 10);
            case "yellow_dye" -> new LegacyRef("dye_powder", 11);
            case "light_blue_dye" -> new LegacyRef("dye_powder", 12);
            case "magenta_dye" -> new LegacyRef("dye_powder", 13);
            case "orange_dye" -> new LegacyRef("dye_powder", 14);
            case "white_dye", "bone_meal" -> new LegacyRef("dye_powder", 15);
            default -> null;
        };
    }

    private static String heuristicLegacyName(String bareName) {
        if (bareName.endsWith("_spawn_egg")) {
            return "spawnEgg";
        }
        if (bareName.endsWith("_bed")) {
            return "bed";
        }
        if (bareName.endsWith("_boat")) {
            return "boat";
        }
        if (bareName.endsWith("_sign") || bareName.endsWith("_hanging_sign")) {
            return "sign";
        }
        if (bareName.endsWith("_door")) {
            return bareName.equals("iron_door") ? "door_iron" : "door_wood";
        }

        String tool = legacyToolName(bareName);
        if (tool != null) {
            return tool;
        }

        String armor = legacyArmorName(bareName);
        if (armor != null) {
            return armor;
        }

        return snakeToCamel(bareName);
    }

    private static String legacyToolName(String bareName) {
        String[] split = bareName.split("_");
        if (split.length != 2) {
            return null;
        }

        String material = switch (split[0]) {
            case "wooden" -> "wood";
            case "stone" -> "stone";
            case "iron" -> "iron";
            case "diamond" -> "diamond";
            case "golden" -> "gold";
            default -> null;
        };
        if (material == null) {
            return null;
        }

        return switch (split[1]) {
            case "sword" -> "sword_" + material;
            case "shovel" -> "shovel_" + material;
            case "pickaxe" -> "pickAxe_" + material;
            case "axe" -> "hatchet_" + material;
            case "hoe" -> "hoe_" + material;
            default -> null;
        };
    }

    private static String legacyArmorName(String bareName) {
        String[] split = bareName.split("_");
        if (split.length != 2) {
            return null;
        }

        String material = switch (split[0]) {
            case "leather" -> "leather";
            case "chainmail" -> "chain";
            case "iron" -> "iron";
            case "diamond" -> "diamond";
            case "golden" -> "gold";
            default -> null;
        };
        if (material == null) {
            return null;
        }

        return switch (split[1]) {
            case "helmet" -> "helmet_" + material;
            case "chestplate" -> "chestplate_" + material;
            case "leggings" -> "leggings_" + material;
            case "boots" -> "boots_" + material;
            default -> null;
        };
    }

    private static String stripNamespace(String name) {
        int idx = name.indexOf(':');
        return idx >= 0 ? name.substring(idx + 1) : name;
    }

    private static String snakeToCamel(String value) {
        StringBuilder out = new StringBuilder();
        boolean upperNext = false;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '_') {
                upperNext = true;
                continue;
            }
            if (upperNext) {
                out.append(Character.toUpperCase(c));
                upperNext = false;
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }

    private static String[] loadItemProtocolNames() {
        InputStream is = ItemMappings.class.getResourceAsStream("/mappings/java_item_protocol_ids.json");
        if (is == null) {
            log.warn("Item protocol lookup not found at mappings/java_item_protocol_ids.json");
            return new String[0];
        }

        try (Reader r = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            JsonArray arr = JsonParser.parseReader(r).getAsJsonArray();
            String[] names = new String[arr.size()];
            for (int i = 0; i < arr.size(); i++) {
                JsonElement el = arr.get(i);
                if (el != null && !el.isJsonNull()) {
                    names[i] = el.getAsString();
                }
            }
            return names;
        } catch (Exception e) {
            log.warn("Failed to load item protocol lookup: {}", e.getMessage());
            return new String[0];
        }
    }

    private static Map<String, Integer> loadIntMap(String resourcePath) {
        InputStream is = ItemMappings.class.getResourceAsStream(resourcePath);
        if (is == null) {
            log.warn("Item lookup resource not found at {}", resourcePath);
            return Map.of();
        }

        try (Reader r = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            JsonObject obj = JsonParser.parseReader(r).getAsJsonObject();
            Map<String, Integer> result = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                if (entry.getValue() == null || entry.getValue().isJsonNull()) {
                    continue;
                }
                result.put(entry.getKey(), entry.getValue().getAsInt());
            }
            return result;
        } catch (Exception e) {
            log.warn("Failed to load item lookup resource {}: {}", resourcePath, e.getMessage());
            return Map.of();
        }
    }
}
