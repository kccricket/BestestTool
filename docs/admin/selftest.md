# Self-test

`/bestesttool admin selftest` runs a live, in-game correctness test against the real server. The automated test suite can't cover this, since MockBukkit doesn't implement the per-item mining data (`BlockData.getDestroySpeed` / `isPreferredTool`) that tool selection reads.

The self-test builds a small labelled arena of blocks (and docile mobs for the combat and refill stages) next to you, hands you a known hotbar kit, and reports pass/fail in chat as you interact with each one.

It is gated behind two things at once:

1. `enable_selftest: true` in `config.yml` (default `false`) — otherwise the command doesn't exist, even for ops.
2. The `bestesttool.admin.selftest` permission (default `op`).

While it runs you are forced into survival mode and your hotbar is replaced with each stage's kit. Your inventory, game mode and BestestTool settings are restored when the test ends — normally, stopped early, or on reload or restart. A crash-safety backup is written to disk and restored the next time you join if the server goes down first.

The stages come from the bundled `selftest/stages.yml`; place `plugins/BestestTool/selftest.yml` next to your config to replace them wholesale.

Commands: `start [<stage>]`, `next`, `status`, `stop`.
