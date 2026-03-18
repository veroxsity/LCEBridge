# LCEBridge

<p align="center">
  <img src="https://img.shields.io/github/license/veroxsity/LCEBridge?style=for-the-badge" alt="License" />
  <img src="https://img.shields.io/github/last-commit/veroxsity/LCEBridge?style=for-the-badge" alt="Last Commit" />
  <img src="https://img.shields.io/github/repo-size/veroxsity/LCEBridge?style=for-the-badge" alt="Repo Size" />
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=flat-square&logo=openjdk" alt="Java 21" />
  <img src="https://img.shields.io/badge/Build-Gradle-02303A?style=flat-square&logo=gradle" alt="Gradle" />
  <img src="https://img.shields.io/badge/Network-Netty-333333?style=flat-square" alt="Netty" />
  <img src="https://img.shields.io/badge/Status-Active%20Development-C89200?style=flat-square" alt="Status" />
</p>

LCEBridge is a Java 21 protocol translator that aims to let unmodified Minecraft Legacy Console Edition clients join Minecraft Java Edition servers.

```text
LCE Client <-> LCEBridge <-> Java Server
```

## Purpose

The goal of the bridge is to translate between the native LCE protocol and modern Java protocol traffic while preserving gameplay behavior as closely as possible.

## Current Focus

- Login and session translation
- World and chunk translation
- Block and item mapping
- Standalone bridge runtime

## Repository Layout

| Path | Purpose |
|------|---------|
| `core/` | Packet codecs, session logic, mappings, and translation code |
| `bootstrap-standalone/` | Standalone entrypoint and runtime resources |
| `tools/` | Scripts for generating and refreshing mapping data |

## Build

```powershell
.\gradlew.bat build
```

Build only the standalone bootstrap:

```powershell
.\gradlew.bat :bootstrap-standalone:build
```

## Status

The bridge is in active development. Login and world join flow are progressing, but protocol coverage and gameplay translation are still incomplete.

## Related Repositories

- Hub repo: https://github.com/veroxsity/MinecraftLCE
- Client repo: https://github.com/veroxsity/LCEClient
- Server repo: https://github.com/veroxsity/LCEServer
