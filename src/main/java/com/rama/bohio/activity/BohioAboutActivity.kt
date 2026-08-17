package com.rama.bohio.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.rama.bohio.R
import com.rama.bohio.widgets.WdLabel
import com.rama.bohio.widgets.WdLabelLink

abstract class BohioAboutActivity : BohioActivity() {

    @get:DrawableRes
    protected abstract val appIconRes: Int

    @get:StringRes
    protected abstract val appDescriptionRes: Int

    @get:StringRes
    protected abstract val appNameRes: Int

    @get:ArrayRes
    protected open val appClaimsArrayRes: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.view_about)

        val root = findViewById<View>(R.id.about_root)
        applyEdgeToEdgePadding(root)
        applyCurrentTheme(root)

        val closeButton = findViewById<Button>(R.id.close_button)
        closeButton.setOnClickListener {
            finish()
        }

        val appIcon = findViewById<ImageView>(R.id.app_icon)
        appIcon.setImageResource(appIconRes)

        val appDescription = findViewById<TextView>(R.id.app_description)
        appDescription.setText(appDescriptionRes)

        val claimsLayout = findViewById<LinearLayout>(R.id.claims)
        if (appClaimsArrayRes != 0) {
            val claimsData = resources.getStringArray(appClaimsArrayRes)
            claimsData.forEach { claim ->
                val tag = WdLabel(this)
                tag.setText(claim)
                tag.setIcon(R.drawable.px_octagon_check)
                claimsLayout.addView(tag)
            }
        }

        val contributorsLayout = findViewById<LinearLayout>(R.id.contributors)
        val contributorsNamesData = resources.getStringArray(R.array.app_contributor_names)
        val contributorsUrlData = resources.getStringArray(R.array.app_contributor_urls)
        val contributors = contributorsNamesData.zip(contributorsUrlData)

        contributors.forEachIndexed { index, (name, url) ->
            val nameTag = WdLabel(this)
            nameTag.setText(name)
            nameTag.setIcon(R.drawable.px_user)

            val urlTag = WdLabel(this)
            urlTag.setText(url)
            urlTag.setIcon(R.drawable.px_github)

            contributorsLayout.addView(nameTag)
            contributorsLayout.addView(urlTag)

            if (index != contributors.lastIndex) {
                val separatorView = LayoutInflater.from(this)
                    .inflate(R.layout.separator, contributorsLayout, false)
                contributorsLayout.addView(separatorView)
            }
        }

        val catalogueLayout = findViewById<LinearLayout>(R.id.catalogue)
        val catalogueIconData = resources.obtainTypedArray(R.array.catalogue_icon)
        val catalogueUrlData = resources.getStringArray(R.array.catalogue_url)

        for (i in catalogueUrlData.indices) {
            val icon = catalogueIconData.getResourceId(i, 0)
            val url = catalogueUrlData[i]

            val urlTag = WdLabelLink(this)
            urlTag.setText(url)
            urlTag.setIcon(icon)

            catalogueLayout.addView(urlTag)
        }
        catalogueIconData.recycle()

        val version = packageManager.getPackageInfo(packageName, 0).versionCode
        val nameView = findViewById<TextView>(R.id.name_version)
        nameView.text = getString(R.string.name_version, getString(appNameRes), version)
    }
}
