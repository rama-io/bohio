# rama-common

Shared Android library module for Mako, Tūī, Puma/Teyin, and Txori.
Contains theme/font/locale managers, `CsActivity`, `AboutActivity`,
`SettingsActivity`, shared widgets, and the common `values/` resources
(attrs, colors, dimens, themes).

## First-time setup

1. Push this directory as its own git repo (public, so F-Droid's build
   servers can clone it without credentials):

   ```bash
   git init
   git add .
   git commit -m "Initial rama-common scaffold"
   git remote add origin https://github.com/rama-io/rama-common.git
   git push -u origin main
   ```

2. In each app repo, add it as a submodule:

   ```bash
   git submodule add https://github.com/rama-io/rama-common.git common
   git submodule update --init --recursive
   ```

3. In the app's `settings.gradle`:

   ```groovy
   include ':app', ':common'
   project(':common').projectDir = file('common')
   ```

4. In `app/build.gradle`:

   ```groovy
   dependencies {
       implementation project(':common')
   }
   ```

5. Make sure the app's theme extends `Theme.Rama.Base` and overrides the
   `ramaXxx` attrs with its own palette (see `themes.xml` for the full list).

6. For F-Droid, add `submodules: true` to the relevant build entry in the
   app's metadata so `git submodule update --init --recursive` runs after
   checkout. Keep the `rama-common` repo public and avoid rewriting history
   on any commit a published app version's submodule pointer references.

## Porting checklist

Each TODO below marks a spot where the real implementation from an existing
app should replace the stub:

- `CsActivity.kt` — `applyRotationLock()` (read shared rotation-lock pref)
- `ThemeManager.kt` — `lastAppliedTheme` cache invalidation, per-theme
  palette switching beyond opacity
- `FontManager.kt` — actual font asset paths / font set
- `LocaleHelper.kt` — wire up from each app's language picker UI
- `SettingsActivity.kt` — shared preference sections (opacity slider, font
  picker, language picker, rotation lock toggle) as a reusable Fragment;
  re-sort signal pattern (`setResult(RESULT_OK)`)
- `widgets/` — port the rest of the shared widget set (multi-select row
  decorations, `..` up-navigation row style, etc.)
- `AboutActivity.kt` — Shroomies/community link, license text

## Resource override pattern

`:common` declares attrs and ships neutral defaults via `Theme.Rama.Base`.
Each app overrides the attrs it cares about:

```xml
<!-- app/src/main/res/values/themes.xml -->
<style name="Theme.Txori" parent="Theme.Rama.Base">
    <item name="ramaWindowBg">@color/txori_bg</item>
    <item name="ramaCardBackground">@color/txori_card</item>
    <item name="ramaAccentColor">@color/txori_accent</item>
</style>
```

Anything in `:common` that needs a color/dimen must resolve it through one
of the `ramaXxx` attrs (via `ThemeManager.resolveColorAttr`) or `@dimen/rama_*`
— never a direct `R.color.*` reference to an app-only resource, since
`:common` can't see the app's `R` class.
