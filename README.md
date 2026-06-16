# Bohío
Shared Android library module for Rama.

## Setup

Add submodule
```bash
git submodule add https://github.com/rama-io/bohio.git bohio
git submodule update --init --recursive
```

Add in the app's `settings.gradle`:
```groovy
include(":app", ":bohio")
project(":bohio").projectDir = file("bohio")
```

Add in the app's `app/build.gradle`:
```groovy
dependencies {
    implementation(project(":bohio"))
}
```

Make sure the app's theme extends `Theme.Rama.Base` in `themes.xml`.
```xml
<resources>
    <style name="Theme.Teyin" parent="Theme.Rama.Base">
        <item name="bgColor">@color/bg_1</item>
    </style>
</resources>
```

For F-Droid, add `submodules: true` to the relevant build entry in the app's metadata so `git submodule update --init --recursive` runs after checkout. Keep the `bohio` repo public and avoid rewriting history on any commit a published app version's submodule pointer references.

## Maintanence

Update submodule
```bash
git submodule update --remote bohio
```