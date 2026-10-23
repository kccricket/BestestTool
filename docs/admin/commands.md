# Admin Commands

| Command | Description | Permission |
|---|---|---|
| `/bestesttool admin reload` | Reload `config.yml` and the language files. | `bestesttool.admin.reload` |
| `/bestesttool admin debug [<state>]` | Toggle (or set) debug logging. | `bestesttool.admin.debug` |
| `/bestesttool admin selftest start [<stage>]` | Start (or jump to a stage of) the [self-test](selftest.md). | `bestesttool.admin.selftest` |
| `/bestesttool admin selftest next` / `status` / `stop` | Skip to the next stage, show progress, or abort. | `bestesttool.admin.selftest` |
| `/bestesttool admin benchmark start [full\|hotbar]` | Start the [benchmark](benchmark.md). | `bestesttool.admin.benchmark` |
| `/bestesttool admin benchmark status` / `stop` | Show progress, or abort. | `bestesttool.admin.benchmark` |

Admin commands are hidden from tab completion, and fail to parse, for senders without the matching permission. `selftest` and `benchmark` are additionally hidden unless `enable_selftest` / `enable_benchmark` is `true` in `config.yml`.

Player commands are documented in the [Player Guide](../player-guide/commands.md).
