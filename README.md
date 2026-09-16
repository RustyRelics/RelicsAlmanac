# Relic's Almanac

A lightweight, per-player HUD showing your coordinates, current biome, and the in-game time of
day — each independently toggleable, positioned to any screen corner, and scaled to taste.
Settings persist per player across reconnects and full server restarts.

## Commands

All subcommands live under `/almanac` (alias: `/ra`). Every setting is per-player.

| Command | Effect |
|---|---|
| `/almanac` | Prints command usage. |
| `/almanac all` | Toggles the whole display. Off hides it without touching the row toggles below; on restores whatever was showing. If nothing is currently enabled (e.g. a fresh install), turns on all three rows. |
| `/almanac coords` | Toggles the coordinates row. |
| `/almanac biome` | Toggles the current-biome row. |
| `/almanac time` | Toggles the in-game time-of-day row. |
| `/almanac position <top_left\|top_right\|bottom_left\|bottom_right>` | Anchors the HUD to a screen corner — handy for staying clear of other mods' HUDs. |
| `/almanac size <1-9>` | Sets the overall HUD scale (fonts, spacing, width all scale together). `3` is the default. |

Turning on any individual row also turns the whole display on, so you don't have to run `/almanac all` separately after your first toggle.

## Installation

Drop the built jar into your Hytale `Mods` folder (`UserData/Mods`) and restart the server or
client.

## Building from source

This mod uses the [Hytale Gradle Plugin](https://github.com/AzureDoom/Hytale-Gradle-Plugin) by
AzureDoom, which handles manifest generation, validation, local dev-server runs, and IDE source
setup.

```text
src/main/java/        Plugin source code
src/main/resources/   Plugin resources, including the UI asset and generated manifest.json
gradle.properties     Mod identity and build configuration
build.gradle.kts      Gradle build and Hytale Gradle Plugin configuration
settings.gradle.kts   Plugin repositories and project name
```

```bash
./gradlew build          # compile + package -> build/libs/
./gradlew runServer      # launch a local dev server with this mod loaded
```

## Resources

- [Hytale Gradle Plugin](https://github.com/AzureDoom/Hytale-Gradle-Plugin)
- [Hytale Modding Guides](https://hytalemodding.dev)

## License

MPL-2.0 — see [LICENSE](LICENSE).
