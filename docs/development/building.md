# Building & Testing

BestestTool depends on [KcMcLib](https://github.com/kccricket/KcMcLib), a shared library consumed as a git submodule through a Gradle composite build. Clone with submodules, or the build can't resolve `net.kccricket:kcmclib`:

```bash
git clone --recurse-submodules https://github.com/kccricket/BestestTool.git
cd BestestTool
./gradlew build
```

If you already cloned without `--recurse-submodules`, run `git submodule update --init`.

Build with JDK 25 (Paper 26.x's API is compiled for Java 25). The plugin still emits Java 21 bytecode. The output is the shaded jar `build/libs/BestestTool-<version>.jar`, with bStats relocated.

## Tests

```bash
./gradlew test
```

The MockBukkit-based suite bootstraps the real plugin and dispatches real Bukkit events and commands.

## Dev server

```bash
./gradlew runServer [-PmcVersion=26.3] [-PrunServerJava=25]
```

Runs a local Paper server with the plugin loaded, in `run/<mcVersion>`.

## Building these docs

```bash
uv sync --project docs
uv run --project docs -- mkdocs serve
```

## License

Apache License 2.0 — see [LICENSE.md](https://github.com/kccricket/BestestTool/blob/develop/LICENSE.md).
