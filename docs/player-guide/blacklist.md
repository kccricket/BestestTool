# Block Blacklist

Add a block to your blacklist and BestestTool will never switch your held item for it.

| Command | Effect |
|---|---|
| `/bestesttool blacklist` (or `blacklist show`) | Show your blacklist. |
| `/bestesttool blacklist add` | Blacklist the block you're holding. |
| `/bestesttool blacklist add <blocks...>` | Blacklist the named blocks. |
| `/bestesttool blacklist add inventory` / `hotbar` | Blacklist every block type in your inventory / hotbar. |
| `/bestesttool blacklist remove ...` | Same forms as `add`, but removes. |
| `/bestesttool blacklist reset` | Clear your blacklist. |

Block names accept the namespaced (`minecraft:dirt`) or bare (`dirt`) form, in any case, and tab-complete.

Server admins can also blacklist blocks for every player with `global_block_blacklist` in `config.yml`.
