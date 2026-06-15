// package com.rama.bohio.ui

// import android.os.Bundle
// import androidx.fragment.app.Fragment
// import com.rama.bohio.CsActivity
// import com.rama.bohio.R

// /**
//  * Base settings activity. Apps extend this and provide their own fragment
//  * containing both shared sections (theme, font, locale, rotation lock) and
//  * app-specific ones.
//  *
//  * Port over from existing SettingsActivity implementations:
//  *  - the re-sort signal pattern (setResult(RESULT_OK) on relevant changes,
//  *    so callers can react via onActivityResult / registerForActivityResult)
//  *  - shared preference sections (ThemeManager opacity slider, FontManager
//  *    picker, LocaleHelper language picker, rotation lock toggle) as a
//  *    reusable Fragment that app fragments can include/compose
//  */
// abstract class SettingsActivity : CsActivity() {

//     override fun onCreate(savedInstanceState: Bundle?) {
//         super.onCreate(savedInstanceState)
//         setContentView(R.layout.activity_settings)

//         if (savedInstanceState == null) {
//             supportFragmentManager.beginTransaction()
//                 .replace(R.id.settings_container, createSettingsFragment())
//                 .commit()
//         }
//     }

//     /** Apps provide their settings fragment (shared sections + app-specific ones). */
//     protected abstract fun createSettingsFragment(): Fragment
// }
