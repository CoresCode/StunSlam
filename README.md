# Stun Slam

A mod that Detects if you performed stun slam and shows a small animation


## Supported Version

- Minecraft: **1.21.11**
- Fabric Loader: **0.19.2**
- Fabric API: **0.141.6+1.21.11**
- Java: **21+**

## Installation

1. Install Fabric for Minecraft 1.21.11.
2. Install Fabric API.
3. Download the Stun Slam `.jar` file.
4. Place it in your Minecraft `mods` folder.
5. Launch Minecraft using the Fabric profile.

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