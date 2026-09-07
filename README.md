# Mushroom Stew Mover

Minecraft **1.20.6 / Fabric** client-side mod.

## Features

- Detects the actual **InventoryScreen**, not the E key.
- When the inventory screen is open, finds `MUSHROOM_STEW` in the main inventory.
- Moves one stew stack at a time into the first empty hotbar slot.
- Does **not** inspect or move the offhand slot.
- If there is no empty hotbar slot, nothing is moved.
- Toggle the feature on/off.
- Configurable move interval: **50–2000 ms**, default **100 ms**.
- Toggle key: **G** by default.
- Settings key: **H** by default.
- Both keys can be rebound in Minecraft's **Options → Controls**.
- Simple settings screen.

## Build

GitHub Actions builds the project automatically on every push to `main`, and the resulting JAR is uploaded as a workflow artifact.

Requirements for local builds: Java 21 and Gradle 8.8+.

```text
gradle build
```

The compiled JAR is placed in `build/libs/`.
