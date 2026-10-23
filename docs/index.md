# BestestTool

*Automatically switch to the best tool — no more digging through your inventory mid-dig.*

BestestTool switches your held item to the best tool for the block you're about to break, or the best weapon when you attack a mob. Tool selection reads Paper's live per-item mining data, so new blocks, new tools, and datapack-defined tool components are picked up automatically — not from a hand-maintained tool-tier table. An optional hotbar "Refill" feature tops up depleted stacks from the rest of your inventory.

## Features

- Automatic best-tool switching for both mining and combat.
- Per-player block blacklist — never auto-switch for specific blocks.
- Optional hotbar-only mode, and a favorite slot for when the best tool isn't already in your hotbar.
- Optional avoidance of tools and weapons that are about to break.
- Optional automatic hotbar refill.
- PlaceholderAPI placeholders.
- Full command autosuggestions and client-side argument validation.
- Folia-supported.

## Installation

**Requirements:** Paper (or a Paper fork such as Leaves or Purpur) 1.20.6 or newer; Folia is supported. Spigot, CraftBukkit and Bukkit-only server jars are not supported.

1. Download `BestestTool.jar` from [Releases](https://github.com/kccricket/BestestTool/releases).
2. Drop the JAR into your server's `plugins/` folder.
3. Restart your server.

## Acknowledgements

BestestTool is a modernised Paper-API fork of the original [BestTool](https://github.com/JEFF-Media-GbR/Spigot-BestTools) by mfnalex (Jeff Media GbR), released under the Apache License 2.0.
