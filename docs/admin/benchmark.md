# Benchmark

`/bestesttool admin benchmark` answers one question: how many tool selections can the plugin run in a single tick before that tick blows the 50 ms budget?

It ramps a fixed, hardcoded workload (a spread of block materials and a synthetic inventory kit — not your real inventory or world, so results are reproducible) against the selection routine, doubling the batch size each tick until a batch takes 50 ms or more. It then reports the peak batch that stayed under budget, the cost per selection, and the sustainable selections per tick.

Gated like the [self-test](selftest.md):

1. `enable_benchmark: true` in `config.yml` (default `false`). Leave it off on production servers — a run deliberately blows the tick budget.
2. The `bestesttool.admin.benchmark` permission (default `op`).

`start [full|hotbar]` chooses the inventory size (`full`, 36 slots, is the default and the worst case; `hotbar` is 9). It works from the console as well as in-game, and only one run is active server-wide. `stop` aborts early; a reload or shutdown aborts a running benchmark automatically.
