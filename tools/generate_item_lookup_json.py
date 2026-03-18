import json
import re
from pathlib import Path


ITEM_ID_PATTERN = re.compile(r"static const int\s+([A-Za-z0-9_]+)_Id\s*=\s*(\d+);")


def main():
    repo_root = Path(__file__).resolve().parents[1]
    reports_root = repo_root.parent / ".local" / "mc_reports" / "reports"
    resources_root = repo_root / "core" / "src" / "main" / "resources" / "mappings"

    registries = json.loads((reports_root / "registries.json").read_text(encoding="utf-8"))
    item_entries = registries["minecraft:item"]["entries"]
    max_protocol = max(entry["protocol_id"] for entry in item_entries.values())
    protocol_names = [None] * (max_protocol + 1)
    for name, entry in item_entries.items():
        protocol_names[entry["protocol_id"]] = name

    blocks = json.loads((reports_root / "blocks.json").read_text(encoding="utf-8"))
    block_defaults = {}
    for block_name, block in blocks.items():
        default_state = None
        for state in block.get("states", []):
            if state.get("default"):
                default_state = state["id"]
                break
        if default_state is None and block.get("states"):
            default_state = block["states"][0]["id"]
        if default_state is not None:
            block_defaults[block_name] = default_state

    item_h = (repo_root.parent / "source" / "Minecraft.World" / "Item.h").read_text(encoding="utf-8")
    legacy_items = {match.group(1): int(match.group(2)) for match in ITEM_ID_PATTERN.finditer(item_h)}

    (resources_root / "java_item_protocol_ids.json").write_text(
        json.dumps(protocol_names, indent=2) + "\n",
        encoding="utf-8",
    )
    (resources_root / "java_block_default_state_ids.json").write_text(
        json.dumps(block_defaults, indent=2, sort_keys=True) + "\n",
        encoding="utf-8",
    )
    (resources_root / "legacy_item_name_to_id.json").write_text(
        json.dumps(legacy_items, indent=2, sort_keys=True) + "\n",
        encoding="utf-8",
    )

    print(f"Wrote {len(protocol_names)} Java protocol item ids")
    print(f"Wrote {len(block_defaults)} Java block default states")
    print(f"Wrote {len(legacy_items)} legacy item ids")


if __name__ == "__main__":
    main()
