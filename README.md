# Stun Slam

A mod that Detects if you performed stun slam and shows a small animation


## Supported Versions

| Minecraft | Jar | Java |
|---|---|---|
| 1.21.11 | `stunslam-<version>+mc1.21.11.jar` | 21+ |
| 26.1, 26.1.1, 26.1.2 | `stunslam-<version>+mc26.1.jar` | 25+ |
| 26.2 | `stunslam-<version>+mc26.2.jar` | 25+ |
| 26.3 | `stunslam-<version>+mc26.3.jar` | 25+ |

Requires Fabric Loader and Fabric API. Mod Menu is optional and adds an in-game settings screen; without it, settings are read from `config/stunslam.json`.

## Installation

1. Install Fabric for your Minecraft version.
2. Install Fabric API.
3. Download the Stun Slam `.jar` that matches your Minecraft version.
4. Place it in your Minecraft `mods` folder.
5. Launch Minecraft using the Fabric profile.

## Building

All Minecraft versions are built from this one project (JDK 25 required):

```
./gradlew build
```

The jars end up in `build/libs/`. To build a single version use `./gradlew :26.3:build`, and to start a test client `./gradlew :26.3:runClient`.

- `src/` - code shared by every Minecraft version
- `versions/<mc>/src/` - the few classes that differ per Minecraft version
- `versions/<mc>/gradle.properties` - Minecraft and dependency versions for that build

## How It Works

Stun Slam monitors the player's attacks on the client.

When an axe attack hits a blocking target, the mod records that target as shield-stunned.

For a limited number of ticks afterward, the mod checks for:

- A mace being used
- The same target being hit
- The player falling
- Sufficient fall distance

When all conditions are satisfied, the Stun Slam indicator is displayed.

## Client-Side

Stun Slam is a client-side mod. It does not add blocks, items, entities, or gameplay content to Minecraft.

## License

This project is licensed under the MIT License.

See [LICENSE](LICENSE) for details.