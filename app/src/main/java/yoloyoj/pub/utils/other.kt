package yoloyoj.pub.utils

import android.util.TypedValue
import android.view.View

fun Number.toDp(view: View): Int =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        view.resources.displayMetrics
    ).toInt()
