# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

See the full changelog at https://github.com/iamkaf/kaf-hud

## 6.1.0

### Changed

- Ported to Minecraft 26.2.

## 6.0.0

### Added

- Added a configurable day counter HUD row.
- Added in-game configuration screens for supported loaders.
- Added per-feature toggles and configurable text colors for coordinates, direction, biome, and day counter.
- Added direction display modes for text labels and arrow symbols.
- Added configurable HUD backgrounds, with off, single-panel, and per-line modes.

### Changed

- BREAKING: KafHUD now requires Konfig for configuration support.
  - Get it from [Modrinth](https://modrinth.com/mod/konfig) or [CurseForge](https://www.curseforge.com/minecraft/mc-mods/konfig)

## 5.0.0

### Changed

- Ported to Minecraft 26.1.

## 4.0.0

### Changed

- Ported to Minecraft 1.21.11.
- **Forge support is temporarily disabled** until ForgeGradle supports Gradle 9.

## 3.0.0

### Changed

- Updated the mod to be compatible with Minecraft 1.21.10.

### Fixed

- Fixed biome name display for names with slashes (e.g. `terralith:caves/frostfire_caves` now displays as "Frostfire Caves").

## 2.0.0

### Added

- Added Forge support.

### Changed

- Completely rewritten the mod using Amber 6.
- Updated the mod to be compatible with Minecraft 1.21.7.

## 1.3.0

### Changed

- Updated the mod to be compatible with Minecraft 1.21.5.

### Removed

- Removed the unobtrusive tool harvestability indicator from the HUD, as it was deemed unnecessary.

## 1.2.0

### Changed

- Updated the mod to be compatible with Minecraft 1.21.4.

## 1.1.0

### Added

- Added an unobtrusive tool harvestability indicator that shows the harvestability of aimed blocks in the HUD.

## 1.0.1

### Fixed

- Hide HUD on F1 key press.

## 1.0.0

### Added

- Initial Release

## Types of changes
- `Added` for new features.
- `Changed` for changes in existing functionality.
- `Deprecated` for soon-to-be removed features.
- `Removed` for now removed features.
- `Fixed` for any bug fixes.
- `Security` in case of vulnerabilities.
