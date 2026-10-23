# Getting Started

BestestTool is off by default for new players (server admins can change that — see [Configuration](../admin/configuration.md)). Turn it on for yourself:

```
/bestesttool
```

`/bt` is a shorter alias. Run the same command again to turn it off. From then on, aiming at a block and starting to dig switches your held item to the best tool in your inventory for that block.

BestestTool does nothing in creative or spectator mode. In adventure mode it only works if the server admin has enabled it.

## Where it looks for tools

By default only your **hotbar** is searched. Run `/bestesttool hotbaronly off` to search your whole inventory; when the best tool is outside the hotbar, BestestTool moves it into your **favorite slot** (set with `/bestesttool favoriteslot <0-8>`; `-1` means "whichever slot I'm holding").

## Bare hands

If nothing in your inventory beats a bare hand for a block, BestestTool switches to an empty hand slot rather than leave a tool in your hand. The exception is blocks that only drop anything with Silk Touch (glass, sea lanterns, coral, turtle eggs, …) — there it switches to a Silk Touch item instead.

BestestTool never touches your held item for blocks nothing can break or drop (bedrock and the like) or for decorated pots, where your held item is your own choice between an intact pot and its sherds.

## Tab completion

Every command tab-completes, including block names for the blacklist.
