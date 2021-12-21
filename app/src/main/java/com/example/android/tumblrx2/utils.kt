package com.example.android.tumblrx2

import android.view.View

/**
 * changes the visibility of a view according to the passed [isVisible] parameter
 */
fun View.visibile(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

/**
 * changes the color of an enabled/disabled view according to [enabled]
 */
fun View.enable(enabled: Boolean) {
    isEnabled = enabled
    alpha = if (enabled) 1f else 0.5f
}

