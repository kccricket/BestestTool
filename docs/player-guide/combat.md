# Combat

When you attack a mob, BestestTool can switch to your best sword — or axe — first.

| Command | Effect |
|---|---|
| `/bestesttool combat swordonmobs [<state>]` | Switch to your best sword or axe when attacking mobs. |
| `/bestesttool combat useaxeassword [<state>]` | Prefer an axe over a sword when it hits harder (slower, and no sweep damage). |
| `/bestesttool avoidbreaking [<state>]` | Skip a weapon on its last point of durability when a healthier one exists; unlike mining, if every candidate is about to break the weapon is kept rather than going unarmed. |

The `combat` commands only exist when the server has combat switching enabled (`allow_combat_switching`) and you have the `bestesttool.combat` permission. If either is turned off your stored preferences are kept and take effect again as soon as it is turned back on.
