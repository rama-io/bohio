// package com.rama.bohio.util

// import android.content.Context
// import android.graphics.Typeface
// import android.view.View
// import android.view.ViewGroup
// import android.widget.TextView

// /**
//  * Shared font manager. Loads and caches custom typefaces from assets and
//  * applies them recursively to a view tree.
//  *
//  * Port over from existing apps:
//  *  - the actual asset paths / font set per app (each app likely keeps its
//  *    own asset font files but can share this loading + recursive-apply logic)
//  *  - any per-widget-type special casing beyond TextView (e.g. EditText, Button
//  *    are TextView subclasses so they're already covered)
//  */
// object FontManager {

//     private val cache = mutableMapOf<String, Typeface>()

//     /** Loads (and caches) a Typeface from an asset path, e.g. "fonts/jersey25.ttf". */
//     fun get(context: Context, assetPath: String): Typeface =
//         cache.getOrPut(assetPath) {
//             Typeface.createFromAsset(context.assets, assetPath)
//         }

//     /** Recursively applies [typeface] to every TextView (and subclass) under [view]. */
//     fun apply(view: View, typeface: Typeface) {
//         if (view is TextView) {
//             view.typeface = typeface
//         }
//         if (view is ViewGroup) {
//             for (i in 0 until view.childCount) {
//                 apply(view.getChildAt(i), typeface)
//             }
//         }
//     }
// }
