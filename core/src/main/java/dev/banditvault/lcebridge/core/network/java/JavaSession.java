package dev.banditvault.lcebridge.core.network.java;

import dev.banditvault.lcebridge.core.BridgeConfig;
import org.geysermc.mcprotocollib.auth.GameProfile;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.event.session.*;
import org.geysermc.mcprotocollib.network.factory.ClientNetworkSessionFactory;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.network.session.ClientNetworkSession;
import org.geysermc.mcprotocollib.protocol.MinecraftConstants;
import org.geysermc.mcprotocollib.protocol.MinecraftProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.function.Consumer;

public class JavaSession {
    private static final Logger log = LoggerFactory.getLogger(JavaSession.class);

    private final BridgeConfig config;
    private String username;
    private ClientNetworkSession session;
    private Consumer<Packet> packetHandler;
    private Runnable disconnectHandler;

    public JavaSession(BridgeConfig config, String username) {
        this.config   = config;
        this.username = username;
    }

    public void setPacketHandler(Consumer<Packet> h)  { this.packetHandler   = h; }
    public void setDisconnectHandler(Runnable h)       { this.disconnectHandler = h; }
    public void setUsername(String name)               { this.username = name; }

    public void connect() {
        MinecraftProtocol protocol = buildProtocol();
        session = ClientNetworkSessionFactory.factory()
            .setAddress(config.remoteAddress, config.remotePort)
            .setProtocol(protocol)
            .create();

        // Tell MCProtocolLib's ClientListener to auto-reply to SelectKnownPacks
        // with an empty list — required or the server never sends FinishConfiguration.
        session.setFlag(MinecraftConstants.SEND_BLANK_KNOWN_PACKS_RESPONSE, true);

        session.addListener(new SessionAdapter() {
            @Override public void packetReceived(Session s, Packet p) {
                if (packetHandler != null) packetHandler.accept(p);
            }
            @Override public void disconnected(DisconnectedEvent e) {
                log.info("Java disconnected for {}: {}", username, e.getReason());
                if (disconnectHandler != null) disconnectHandler.run();
            }
            @Override public void packetError(PacketErrorEvent e) {
                // DO NOT suppress — we need to see what's failing.
                // Suppressing here was hiding silent packet decode failures (e.g. compression issues)
                // causing large packets like ChunkBatchStart and LevelChunkWithLight to be dropped.
                log.error("Java packet error for {}: {}", username, e.getCause().getMessage(), e.getCause());
                // Do not call e.setSuppress(true) — let the session handle it naturally.
            }
        });

        session.connect();
        log.info("Connecting to Java server {}:{} as '{}'",
            config.remoteAddress, config.remotePort, username);
    }

    private MinecraftProtocol buildProtocol() {
        if ("online".equalsIgnoreCase(config.authType)) {
            if (config.minecraftProfileId == null || config.minecraftProfileId.isBlank()) {
                throw new IllegalStateException("Remote auth-type=online but minecraft-profile-id was not configured.");
            }
            if (config.minecraftProfileName == null || config.minecraftProfileName.isBlank()) {
                throw new IllegalStateException("Remote auth-type=online but minecraft-profile-name was not configured.");
            }
            if (config.minecraftAccessToken == null || config.minecraftAccessToken.isBlank()) {
                throw new IllegalStateException("Remote auth-type=online but minecraft-access-token was not configured.");
            }

            this.username = config.minecraftProfileName;
            GameProfile profile = new GameProfile(UUID.fromString(config.minecraftProfileId), config.minecraftProfileName);
            log.info("Using launcher-provided online session for '{}'", config.minecraftProfileName);
            return new MinecraftProtocol(profile, config.minecraftAccessToken);
        }

        return new MinecraftProtocol(username);
    }

    public void send(Packet pkt) {
        if (session != null && session.isConnected()) session.send(pkt);
    }

    public void disconnect(String reason) {
        if (session != null && session.isConnected()) session.disconnect(reason);
    }

    public boolean isConnected() {
        return session != null && session.isConnected();
    }
}
