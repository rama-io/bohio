package com.rama.bohio.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

class WdRadioGroup : LinearLayout {

    private lateinit var radioContainer: LinearLayout
    private var checkedId = -1
    private var nextOptionId = 1
    private var protectFromCheckedChange = false
    private var listener: OnCheckedChangeListener? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        orientation = VERTICAL

        radioContainer = LinearLayout(context).apply {
            orientation = VERTICAL
        }

        super.addView(
            radioContainer,
            LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        )
    }

    fun addOption(text: String): WdRadio {
        val radio = WdRadio(context).apply {
            id = nextOptionId++
            this.setText(text)
        }

        addView(radio)
        return radio
    }

    fun getCheckedIndex(): Int {
        if (checkedId == -1) {
            return -1
        }

        val checked = findViewById<View>(checkedId)
        return if (checked == null) {
            -1
        } else {
            radioContainer.indexOfChild(checked)
        }
    }

    fun check(id: Int) {
        if (id == -1) {
            clearCheck()
            return
        }

        val view = findViewById<View>(id)

        if (view !is WdRadio) {
            return
        }

        val radio = view

        if (checkedId == id && radio.isChecked()) {
            return
        }

        protectFromCheckedChange = true

        if (checkedId != -1) {
            val oldView = findViewById<View>(checkedId)

            if (oldView is WdRadio) {
                oldView.setChecked(false)
            }
        }

        radio.setChecked(true)

        protectFromCheckedChange = false
        setCheckedId(id)
    }

    fun clearCheck() {
        if (checkedId == -1) {
            return
        }

        protectFromCheckedChange = true

        val view = findViewById<View>(checkedId)

        if (view is WdRadio) {
            view.setChecked(false)
        }

        protectFromCheckedChange = false
        setCheckedId(-1)
    }

    private fun setCheckedId(id: Int) {
        if (checkedId == id) {
            return
        }

        checkedId = id
        listener?.onCheckedChanged(this, checkedId)
    }

    fun setOnCheckedChangeListener(listener: OnCheckedChangeListener?) {
        this.listener = listener
    }

    override fun addView(
        child: View,
        index: Int,
        params: ViewGroup.LayoutParams
    ) {
        if (child === radioContainer) {
            super.addView(child, index, params)
            return
        }

        if (child !is WdRadio) {
            return
        }

        val radio = child

        radioContainer.addView(radio, params)

        radio.setInternalCheckedChangeListener { internalRadioBtn, checked ->
            if (protectFromCheckedChange) {
                return@setInternalCheckedChangeListener
            }

            if (checked) {
                check(internalRadioBtn.id)
            } else if (checkedId == internalRadioBtn.id) {
                protectFromCheckedChange = true
                internalRadioBtn.setChecked(true)
                protectFromCheckedChange = false
            }
        }

        if (radio.isChecked()) {
            check(radio.id)
        }
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(group: WdRadioGroup, checkedId: Int)
    }
}
