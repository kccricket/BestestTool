# Permissions

Out of the box every player can use the plugin. Only ops can reload, debug, self-test or benchmark.

| Node | Default | Grants |
|---|---|---|
| `bestesttool` | `true` | Umbrella for `use`, `combat` and `refill` (does not cascade to admin nodes). |
| `bestesttool.use` | `true` | Automatic switching itself and `/bestesttool` with its non-combat subcommands. |
| `bestesttool.combat` | `true` | Weapon switching on attack and `/bestesttool combat`. Also needs `allow_combat_switching: true`. |
| `bestesttool.refill` | `true` | Hotbar refill and `/bestesttool refill` (`/refill`, `/rf`). |
| `bestesttool.admin` | `op` | Umbrella for the four admin nodes below. |
| `bestesttool.admin.reload` | `op` | `/bestesttool admin reload`. |
| `bestesttool.admin.debug` | `op` | `/bestesttool admin debug`. |
| `bestesttool.admin.selftest` | `op` | `/bestesttool admin selftest`. Also needs `enable_selftest: true`. |
| `bestesttool.admin.benchmark` | `op` | `/bestesttool admin benchmark`. Also needs `enable_benchmark: true`. |
| `bestesttool.throttle.bypass` | | Exempt from the per-player action throttle (`action_cooldown_ms`). |

A sender holding only one admin child permission can still reach that one command.
