# Commands

All commands live under `/bestesttool` (alias `/bt`). `/refill` and `/rf` go straight to the refill toggle.

| Command | Description |
|---|---|
| `/bestesttool` | Toggle automatic best-tool switching. |
| `/bestesttool hotbaronly [<state>]` | Toggle (or set) hotbar-only tool search. |
| `/bestesttool favoriteslot [<-1-8>]` | Report (or set) the favorite slot. |
| `/bestesttool refill [<state>]` | Toggle (or set) automatic hotbar refill. |
| `/bestesttool swordsforleaves [<state>]` | Use swords on leaves without shears. |
| `/bestesttool swordsforcobwebs [<state>]` | Use swords on cobwebs without shears. |
| `/bestesttool switchduringbattle [<state>]` | Keep switching while holding a weapon. |
| `/bestesttool avoidbreaking [<state>]` | Avoid tools and weapons about to break. |
| `/bestesttool combat swordonmobs [<state>]` | Switch to your best sword/axe when attacking mobs. |
| `/bestesttool combat useaxeassword [<state>]` | Prefer an axe over a sword when it hits harder. |
| `/bestesttool blacklist ...` | Manage your [block blacklist](blacklist.md). |

Every toggle except the bare `/bestesttool` takes an optional `<state>`: `yes`/`no`, `true`/`false`, `on`/`off` or `enable`/`disable`.
