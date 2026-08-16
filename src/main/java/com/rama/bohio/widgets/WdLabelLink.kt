package com.rama.bohio.widgets

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.rama.bohio.R

class WdLabelLink @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val root: LinearLayout
    private val iconImage: ImageView
    private val iconText: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.wd_label_link, this, true)

        root = findViewById(R.id.root)
        iconImage = findViewById(R.id.icon_image)
        iconText = findViewById(R.id.icon_text)

        attrs?.let { setAttrs(context, it) }

        root.setOnClickListener {
            openUrl()
        }
    }

    private fun openUrl() {
        val text = iconText.text.toString().trim()

        if (text.isEmpty()) return

        val url = when {
            text.startsWith("http://", ignoreCase = true) ||
                    text.startsWith("https://", ignoreCase = true) -> {
                text
            }

            else -> {
                "https://$text"
            }
        }

        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)
    }

    private fun setAttrs(context: Context, attrs: AttributeSet) {
        for (i in 0 until attrs.attributeCount) {
            val name = attrs.getAttributeName(i)

            when (name) {
                "text" -> {
                    val resId = attrs.getAttributeResourceValue(i, 0)

                    if (resId != 0) {
                        iconText.text = context.getString(resId)
                    } else {
                        iconText.text = attrs.getAttributeValue(i)
                    }
                }

                "icon" -> {
                    val resId = attrs.getAttributeResourceValue(i, 0)

                    if (resId != 0) {
                        iconImage.setImageResource(resId)
                    }
                }
            }
        }
    }

    fun setText(text: String) {
        iconText.text = text
    }

    fun setIcon(resId: Int) {
        iconImage.setImageResource(resId)
    }
}