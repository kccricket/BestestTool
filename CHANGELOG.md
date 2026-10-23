# Changelog

# BestestTool 1.0.0

Release date: 2026-08-10

BestestTool 1.0.0 is a ground-up modernization of the original BestTool plugin, rewritten for
Paper's live per-item mining data with a clean Brigadier-based command tree, Folia support, and no
legacy dependencies.

## Highlights

- **New package & name** — rebranded from `BestTool` (`de.jeff_media.BestTool`) to `BestestTool`
  (`net.kccricket.bestesttool`), moved from Maven/Spigot-API to Gradle/Paper-API, with a fully
  reorganized, concern-based package layout (`commands/`, `listeners/`, `tool/`, `refill/`,
  `model/`, `selftest/`, `benchmark/`, `util/`).
- **Live per-item mining-data tool selection** — reads Paper's
  `BlockData.getDestroySpeed`/`isPreferredTool` directly instead of a hand-maintained tool-tier
  table, so new blocks, new tools, and datapack-defined tool components are picked up
  automatically. Tools that would fail to drop a block correctly (e.g. an enchanted iron pickaxe
  on obsidian) now rank below any tool that mines it correctly, even if slower.
- **Brigadier command tree** — `/bestesttool` (alias `/bt`) rebuilt on Paper's Brigadier command
  API for real per-argument tab completion and client-side validation, including block-name
  suggestions for the blacklist. `/refill` and `/rf` alias directly to the refill toggle.
- **Folia support** (`folia-supported: true`) — all shared mutable state made safe for per-region
  worker threads; the plugin descriptor moved from the legacy `plugin.yml` to `paper-plugin.yml`.
- **New admin diagnostics** — `/bestesttool admin selftest` (a live in-game correctness/
  compatibility test) and `/bestesttool admin benchmark` (a synthetic tick-budget benchmark), both
  off by default behind a config flag and a permission node.
- **New per-player preferences** — `swordsforleaves`, `swordsforcobwebs`, `switchduringbattle`,
  `avoidbreaking`, `favoriteslot`, and `combat swordonmobs`/`combat useaxeassword` (the latter two
  gated behind a new `bestesttool.combat` permission plus `allow_combat_switching` in
  `config.yml`).
- Internal plugin-agnostic utility code (logging, permission checks, messaging/localisation, the
  update checker, config lifecycle) extracted into a shared library, **KcMcLib**, consumed as a
  git submodule — the same pattern ClickSorted uses. No behavior change for players or admins.

## Breaking Changes

- **Renamed from `/besttools` to `/bestesttool`** (alias `/bt`); the old command no longer exists.
  `/refill`/`/rf` now alias straight to `/bestesttool refill` instead of being registered as
  separate commands, and `/refill reload` is removed — use `/bestesttool admin reload`.
- **Removed the settings GUI** (`/bestesttool gui`/`settings`) — a shift-click or drag from the
  player's own inventory while it was open could silently destroy the shifted/dragged item. Its
  favorite-slot picker is replaced by `/bestesttool favoriteslot [<-1 to 8>]`; `hotbaronly` and
  `refill` already had chat-command equivalents.
- **Replaced `/bestesttool performance` with `/bestesttool admin benchmark`** (`start
  [full|hotbar]`/`status`/`stop`) — the old command passively sampled real event-handler wall
  time, so results weren't comparable between runs; the new benchmark runs a fixed synthetic
  workload against the tool-selection routine instead. The `measure_performance` config key is
  removed.
- **`reload`/`debug`/`selftest`/`benchmark` moved under a new `/bestesttool admin` subcommand**,
  with matching permission-node renames: `bestesttool.admin.reload`/`.debug`/`.selftest`/
  `.benchmark`.
- **Permission nodes renamed to the `bestesttool.*` prefix** (no `besttools.*` legacy alias) and
  are now declared in `paper-plugin.yml`'s `permissions:` block instead of registered in code.
  `bestesttool.use` and `bestesttool.refill` now default to `true` (previously `op`), so the
  plugin works for every player out of the box.
- **Sword/combat preferences moved from server config to per-player settings.** `sword_on_mobs`,
  `use_axe_as_sword`, `consider_swords_for_leaves`, and `consider_swords_for_cobwebs` moved from
  server-wide `config.yml` keys into per-player preferences (`defaults.*` still seeds new
  players). `dont_switch_during_battle` is renamed `switch_during_battle` with inverted meaning
  (default `false` reproduces the old `dont_switch_during_battle: true` exactly — no behavior
  change). Old on-disk keys are now unrecognized and named in a console warning on next load.
- **Combat switching now requires an explicit gate.** Added `allow_combat_switching` (config,
  default `true`) and a new `bestesttool.combat` permission (default `true`) — both are required
  to enable weapon-switching on attack and the `/bestesttool combat` subtree.
- **PlaceholderAPI identifiers renamed** from `besttools` to `bestesttool` — `%besttools_btenabled%`
  etc. are now `%bestesttool_btenabled%`, `%bestesttool_rfenabled%`, `%bestesttool_hotbaronly%`,
  `%bestesttool_favoriteslot%`.
- **Minimum supported server raised to Paper 1.20.6**, required by the Brigadier command API the
  command tree is built on.
- **Internal Java API reshuffle, for third-party plugin developers only.** The main class is
  renamed from `Main` to `BestestToolPlugin` and 34 previously flat classes moved into
  subpackages — a manual jar swap (not a hot reload) is needed to pick this up.

## New Features

### Live per-item mining data

Tool selection now reads Paper's live `BlockData.getDestroySpeed`/`isPreferredTool` for both
mining and leaves/cobweb handling, replacing the plugin's own hardcoded tier table and
shears/hoe/sword preference list.

### Self-test & benchmark

`/bestesttool admin selftest` (permission `bestesttool.admin.selftest`, default `op`; also
requires `enable_selftest: true` in `config.yml`) builds a labelled arena of blocks — and, for the
combat/refill stages, mobs — next to the tester, hands them a known hotbar kit, and reports
pass/fail in chat as they interact with each one, comparing the plugin's actual choice against a
declared expectation (`selftest/stages.yml`, overridable via `plugins/BestestTool/selftest.yml`).
It forces the tester into survival mode for the run and restores their inventory, game mode, and
settings afterward — including a crash-safety backup restored automatically on next join if the
server goes down mid-test.

`/bestesttool admin benchmark` (permission `bestesttool.admin.benchmark`, default `op`; also
requires `enable_benchmark: true`) ramps a fixed, hardcoded synthetic workload against the
selection routine, doubling the batch size each tick until one exceeds the server's 50ms tick
budget, then reports the peak sustainable batch, the measured cost per selection, and the derived
selections/tick.

### Avoid-breaking-tools

BestestTool now skips an item that's one hit from breaking when a healthier alternative exists,
for both mining and combat. If nothing healthy is left, mining falls back to a bare hand rather
than handing over the near-broken tool; combat keeps using the near-broken weapon, since there's
no bare-hand fallback for attacking. Controlled per player via `/bestesttool avoidbreaking
[<state>]` (default on), with a server-wide `allow_avoid_breaking_tools` kill switch (default on)
in `config.yml`.

### Combat & sword preferences

Five new per-player toggles: `swordsforleaves`, `swordsforcobwebs`, `switchduringbattle` (all
gated by `bestesttool.use`), and `combat swordonmobs`/`combat useaxeassword` (gated by
`bestesttool.use` + `bestesttool.combat`, and requiring `allow_combat_switching: true` in
`config.yml`). Disabling the combat gate hides the combat commands and makes weapon-switching
inert without wiping stored preferences.

## Bug Fixes

- Fixed the per-player best-tool cache going stale after rearranging inventory (drag/click),
  swapping hands with **F**, changing game mode, dying/respawning, or an admin changing
  `global_block_blacklist`/`allow_in_adventure_mode` via reload — previously only dropping,
  picking up, switching held slot, or breaking a tool invalidated it.
- Fixed `/bestesttool refill` (and `/refill`, `/rf`) permanently destroying the empty bowl/bottle
  it was supposed to relocate out of the refill destination slot.
- Fixed `/bestesttool admin` (and every other admin subcommand's parent) showing up in tab
  completion for players holding no admin permission at all.
- Fixed `/bestesttool blacklist reset` dumping every removed material into one giant chat line; it
  now sends a single summary message.
- Fixed `/bestesttool admin benchmark`'s per-batch progress line never actually reaching the
  player who started the run.
- Fixed bStats leaking a `Metrics` instance (and its scheduled submission task) on every reload —
  it's now registered once, in `onEnable`.
- Fixed a crash on plugin load when the global block blacklist contained an invalid material name.
- Fixed `swordOnMobs` and the favorite-slot setting not surviving a server restart.
- Fixed the root cause behind blacklist edits not being explicitly persisted — `Blacklist` now
  writes through to PDC immediately on every add/remove instead of riding along on some unrelated
  save.

## Other Improvements

- Material tab-completion (`blacklist add`/`remove`) now suggests namespaced IDs
  (`minecraft:dirt`), via a new shared `net.kccricket.kcmclib.commands.Suggest` helper (also used
  by ClickSorted); parsing switched from the case-sensitive `Material.getMaterial` to
  `Material.matchMaterial`.
- Replaced the plugin's own messaging/cooldown code with a single KcMcLib
  `net.kccricket.kcmclib.text.Messenger`; every chat message is now consistently prefixed
  (`[BestestTool]`) and rate-limiting folds into the same call.
- Added `action_cooldown_ms` (default 150ms) to throttle repeated command use and blacklist edits
  per player; `bestesttool.throttle.bypass` exempts a player.
- Added `enable_metrics` (default `true`) to opt out of bundled bStats reporting.
- Removed the last Spigot-only API usage (a BungeeCord `TextComponent`) in favor of Paper's
  bundled Adventure API.
- Removed the one-time flat-file playerdata migration and assorted dead code left over from
  earlier BestTool/BestestTool history.
- Added a unit test suite (JUnit 5 + MockBukkit) covering tool selection, commands, and permission
  checks.

## Compatibility

✔️ Paper/Folia 1.20.6 – 26.2.x
✔️ Java 21+

## Upgrading from BestTool (original)

1. Stop your server.
2. Remove the old BestTool jar from `plugins/`.
3. Place `BestestTool-1.0.0.jar` in `plugins/`.
4. Start your server. Fresh `config.yml` and `lang/en_us.yml` files will be generated in
   `plugins/BestestTool/`.
5. Player preferences (favorite slot, blacklist, hotbar-only, combat/avoid-breaking settings) do
   not carry over from the original plugin's flat-file storage; players start with defaults.
