package com.rama.bohio.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import com.rama.bohio.CsActivity
import com.rama.bohio.R

/**
 * Generic "About" screen reused across apps.
 *
 * :bohio can't see each app's R class at compile time, so branding is
 * passed in via Intent extras rather than resource references:
 *
 *   startActivity(
 *       Intent(this, AboutActivity::class.java)
 *           .putExtra(AboutActivity.EXTRA_APP_NAME, getString(R.string.app_name))
 *           .putExtra(AboutActivity.EXTRA_GITHUB_URL, "https://github.com/rama-io/mako")
 *   )
 *
 * Port over from existing About activities:
 *  - Shroomies / community link, license text, etc. as additional extras
 *    or as a multiline string extra
 */
class AboutActivity : CsActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val appName = intent.getStringExtra(EXTRA_APP_NAME) ?: ""
        val githubUrl = intent.getStringExtra(EXTRA_GITHUB_URL)
        val versionName = packageManager.getPackageInfo(packageName, 0).versionName

        findViewById<TextView>(R.id.about_app_name).text = appName
        findViewById<TextView>(R.id.about_version).text =
            getString(R.string.rama_about_version, versionName)

        findViewById<TextView>(R.id.about_github_link).setOnClickListener {
            githubUrl?.let { url -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }
        }
    }

    companion object {
        const val EXTRA_APP_NAME = "extra_app_name"
        const val EXTRA_GITHUB_URL = "extra_github_url"
    }
}
