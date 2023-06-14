package com.example.furnichic.util

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.example.furnichic.R

class ToggleTextView : AppCompatTextView {
    private var mfilterSelected = true
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, checkableId: Int) : super(context) {}

    constructor(context: Context) : super(context) {}

    fun isFilterSelected(): Boolean {
        return mfilterSelected
    }

    fun toggleFilterState() {
        if (mfilterSelected) {
            background = resources.getDrawable(R.drawable.toggle_1)
            setTextColor(resources.getColor(R.color.g_gray500))
            mfilterSelected = false
        } else {
            background = resources.getDrawable(R.drawable.toggle_2)
            setTextColor(resources.getColor(R.color.white))
            mfilterSelected = true
        }
    }
}
