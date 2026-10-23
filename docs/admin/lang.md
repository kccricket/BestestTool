# Localisation & lang overrides

All player-facing messages use **[MiniMessage](https://docs.advntr.dev/minimessage)** formatting.

The default English strings ship **inside the jar**, so improved wording in a new release reaches every server automatically. To customize a message, create a sparse override file at `plugins/BestestTool/lang/<locale>.yml` (`en_us.yml`, `de_de.yml`, …; a language-only name such as `de.yml` is a fallback). Include only the keys you want to change; everything else keeps resolving to the built-in default.

Resolution order for a player's locale token:

```
override[token] → override[lang] → override[default_locale]
  → internal[token] → internal[lang] → internal[default_locale] → internal[en_us]
```

`default_locale` in `config.yml` also selects the language for console output. Changes take effect after `/bestesttool admin reload`.

Tags such as `<slot>`, `<items>`, `<stage>` or `<plugin>` are filled in at runtime; don't remove a placeholder from a message that uses it. The full key list, with defaults, is in the bundled [`en_us.yml`](https://github.com/kccricket/BestestTool/blob/develop/src/main/resources/lang/en_us.yml). The `prefix` key is prepended to every chat message; set it to `""` to drop it.
