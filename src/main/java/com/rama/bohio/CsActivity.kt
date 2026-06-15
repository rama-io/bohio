// package com.rama.bohio

// import android.content.Context
// import android.os.Bundle
// import android.view.MotionEvent
// import androidx.activity.OnBackPressedCallback
// import androidx.appcompat.app.AppCompatActivity
// import com.rama.bohio.util.LocaleHelper
// import com.rama.bohio.util.ThemeManager

// /**
//  * Base activity shared across Rama apps. Subclass this instead of
//  * AppCompatActivity directly.
//  *
//  * Handles:
//  *  - locale override (LocaleHelper) via attachBaseContext
//  *  - window background + nav bar tint (ThemeManager)
//  *  - rotation lock
//  *  - swallowing the phantom ACTION_UP that follows a long-press
//  *
//  * Port over the actual implementations from Mako's CsActivity:
//  *  - applyRotationLock() reading the shared rotation-lock pref
//  *  - any HOME_DOUBLE_TAP_SLEEP / launcher-specific hooks should stay
//  *    in Mako's own activity subclass, not here
//  */
// abstract class CsActivity : AppCompatActivity() {

//     /**
//      * Set to true right before triggering a long-press action (e.g. entering
//      * multi-select) to swallow the synthetic ACTION_UP that follows it.
//      */
//     protected var suppressNextClick: Boolean = false

//     override fun attachBaseContext(newBase: Context) {
//         super.attachBaseContext(LocaleHelper.wrap(newBase))
//     }

//     override fun onCreate(savedInstanceState: Bundle?) {
//         super.onCreate(savedInstanceState)
//         applyRotationLock()
//         ThemeManager.applyWindowBackground(this)

//         onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//             override fun handleOnBackPressed() {
//                 onCsBackPressed()
//             }
//         })
//     }

//     /** Override to customize back behavior. Default just finishes the activity. */
//     open fun onCsBackPressed() {
//         finish()
//     }

//     /**
//      * Applies the rotation lock preference shared across apps.
//      *
//      * TODO: read the shared "rotation lock" pref key and set
//      * requestedOrientation accordingly, e.g.:
//      *
//      *   requestedOrientation = if (rotationLocked) {
//      *       ActivityInfo.SCREEN_ORIENTATION_LOCKED
//      *   } else {
//      *       ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
//      *   }
//      */
//     protected open fun applyRotationLock() {
//     }

//     override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//         if (suppressNextClick && ev.action == MotionEvent.ACTION_UP) {
//             suppressNextClick = false
//             return true
//         }
//         return super.dispatchTouchEvent(ev)
//     }
// }
