# Configuration

`config.yml` is documented inline. When an update adds a new key, it appears (with its comment) in your existing file on the next load; run `/bestesttool admin reload` after editing.

## Per-player defaults (`defaults:`)

Applied to new players, or any player whose stored data is missing.

| Key | Default | Meaning |
|---|---|---|
| `enabled` | `false` | Best-tool switching on for new players. |
| `refill_enabled` | `false` | Hotbar refill on for new players. |
| `hotbar_only` | `true` | Only search the hotbar. |
| `favorite_slot` | `8` | Slot (0–8, or `-1` for the held slot) a tool is moved into. |
| `sword_on_mobs` | `true` | Switch to a sword/axe when attacking mobs. |
| `use_axe_as_sword` | `false` | Prefer an axe when it hits harder. |
| `switch_during_battle` | `false` | Keep switching while holding a weapon. |
| `consider_swords_for_leaves` | `false` | Use swords on leaves without shears. |
| `consider_swords_for_cobwebs` | `false` | Use swords on cobwebs without shears. |
| `avoid_breaking_tools` | `true` | Skip tools on their last point of durability. |

## General

| Key | Default | Meaning |
|---|---|---|
| `allow_in_adventure_mode` | `false` | Also work in adventure mode (survival always works). |
| `allow_combat_switching` | `true` | Master switch for combat features; players also need `bestesttool.combat`. Does not affect `switch_during_battle` or the sword-for-leaves/cobwebs options. |
| `allow_avoid_breaking_tools` | `true` | Server-wide kill switch for avoiding near-broken tools. |
| `global_block_blacklist` | `[]` | Blocks that never trigger switching, for every player. |
| `check_for_updates` | `true` | `true`, `on-startup` or `false`. |
| `check_for_updates_interval_hours` | `4` | Hours between update checks. |
| `default_locale` | `en_us` | Fallback [locale](lang.md). |
| `action_cooldown_ms` | `150` | Minimum ms between a player's commands; `<= 0` disables. |
| `enable_metrics` | `true` | Anonymous [bStats](https://bstats.org) reporting. |
| `debug_level` | `OFF` | `OFF`, `DEBUG` or `TRACE`. |
| `dump` | `false` | Write the Material→Tool map to a CSV. |
| `enable_selftest` | `false` | Expose the [self-test](selftest.md). |
| `enable_benchmark` | `false` | Expose the [benchmark](benchmark.md). |

BestestTool already never switches for blocks no tool can break or drop, or for decorated pots; that list is built in and not configurable.
