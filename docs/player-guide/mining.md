# Mining

When you start breaking a block, BestestTool picks the item from your inventory that mines it best, using the server's live per-item mining data.

## Per-player options

| Command | Effect |
|---|---|
| `/bestesttool hotbaronly [<state>]` | Only use tools from your hotbar. |
| `/bestesttool favoriteslot [<-1-8>]` | Which hotbar slot a tool is moved into when it comes from the rest of your inventory. |
| `/bestesttool swordsforleaves [<state>]` | Use a sword on leaves when you have no shears (swords take double durability damage on leaves). |
| `/bestesttool swordsforcobwebs [<state>]` | Use a sword on cobwebs when you have no shears. |
| `/bestesttool switchduringbattle [<state>]` | Keep switching tools even while you hold a sword, bow, crossbow or trident. Off by default so a fight never swaps your weapon away. |
| `/bestesttool avoidbreaking [<state>]` | Skip a tool that is down to its last point of durability if a healthier one exists. If every candidate is about to break, mining falls back to a bare hand. |

`<state>` is `yes`/`no`, `true`/`false`, `on`/`off` or `enable`/`disable`. Leave it off to flip the current value.
