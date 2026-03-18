# LCEBridge

LCEBridge is a Java 21 protocol translator that lets unmodified Minecraft Legacy Console Edition clients connect to Minecraft Java Edition servers.

## Goals

- Translate the native LCE protocol to modern Java protocol traffic
- Preserve gameplay semantics where possible instead of only handling login flow
- Support standalone proxy deployment and reusable bridge components
- Keep generated mapping data close to the code that consumes it

## Repository Layout

| Path | Purpose |
|------|---------|
| `core/` | Packet codecs, session logic, mappings, and translation code |
| `bootstrap-standalone/` | Standalone entrypoint and runtime resources |
| `tools/` | Scripts for generating and refreshing mapping data |

## Build

From the repository root:

```powershell
.\gradlew.bat build
```

To build just the standalone bootstrap:

```powershell
.\gradlew.bat :bootstrap-standalone:build
```

## Development Notes

- Generated mapping assets live under `core/src/main/resources/mappings/`
- The Python scripts in `tools/` regenerate block and item mapping data
- Session and gameplay translation currently live mostly under `core/src/main/java/dev/banditvault/lcebridge/core/session/`

## Related Repositories

- Hub repo: https://github.com/veroxsity/MinecraftLCE
- Client repo: https://github.com/veroxsity/LCEClient
- Server repo: https://github.com/veroxsity/LCEServer

## Status

The bridge is in active development. Login and world join flow are progressing, but protocol coverage and gameplay translation are still incomplete.
