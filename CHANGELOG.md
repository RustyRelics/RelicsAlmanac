# Changelog

All notable changes to Relic's Almanac. Versions follow [Semantic Versioning](https://semver.org/).

## 0.3.0

### Added
- `/almanac nudge <left|right|up|down> [px]` — moves the HUD from wherever it currently is, so it can
  be placed clear of other mods' overlays and vanilla HUD elements instead of being limited to the
  four screen corners. `px` is optional (default 10, 1–50 per command); repeat the command to
  fine-tune. Directions always match what you see on screen, from every corner.
- `/almanac position reset` — clears the nudge and keeps the current corner.

### Changed
- `/almanac position <corner>` now also clears any nudge, so a HUD that was nudged somewhere
  unreachable can always be recovered by picking a corner.

### Compatibility
- Existing saved settings load unchanged; the nudge starts at zero.

## 0.2.0

### Added
- Command descriptions and chat replies are now localizable (en-US catalog included).

## 0.1.0

- Initial release: `/almanac` (`/ra`) with `all`, `coords`, `biome`, `time`, `position` and `size`,
  persisted per player.
