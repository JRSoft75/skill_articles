package ru.skillbranch.skillarticles.extensions

import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop


fun View.setMarginOptionally(
    left: Int = marginLeft,
    top: Int = marginTop,
    right: Int = marginRight,
    bottom: Int = marginBottom
) {
    val lp: ViewGroup.MarginLayoutParams = this.getLayoutParams() as ViewGroup.MarginLayoutParams
    lp.setMargins(
        left ?: marginLeft,
        top ?: marginTop,
        right ?: marginRight,
        bottom ?: marginBottom
    );
    this.requestLayout();
}

fun View.setPaddingOptionally(
    left: Int = paddingLeft,
    top: Int = paddingTop,
    right: Int = paddingRight,
    bottom: Int = paddingBottom
) {
//    val lp: ViewGroup.LayoutParams = this.getLayoutParams() as ViewGroup.LayoutParams
    this.setPadding(
        left ?: paddingLeft,
        top ?: paddingTop,
        right ?: paddingRight,
        bottom ?: paddingBottom
    );
    this.requestLayout();
}