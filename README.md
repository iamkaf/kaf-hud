# KafHUD

KafHUD is a minimal location HUD for Fabric, Forge and NeoForge.

## Supported Versions

The active Stonecutter baseline is Minecraft `26.1.2`.
Older lines were removed during the Stonecutter rewrite setup and can be backported later from the active baseline.

## Directory layout

- `common/` contains code shared between all loaders.
- `fabric/`, `forge/` and `neoforge/` contain loader-specific entry points and build logic.
- `versions/<minecraft>/gradle.properties` contains version-specific Minecraft, Java, range, and loader metadata.
- `stonecutter.gradle.kts` controls the active Stonecutter version and preprocessing rules.

## Commands

List buildable Stonecutter nodes:

```bash
just list-nodes
```

Build one node:

```bash
just build 26.1.2-fabric
```

Compile all configured nodes:

```bash
just compile-all
```
