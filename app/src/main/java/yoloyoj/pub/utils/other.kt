package yoloyoj.pub.utils

import android.util.TypedValue
import android.view.View

fun Number.toDp(view: View): Int =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        view.resources.displayMetrics
    ).toInt()

fun timeToString(hour: Int, minute: Int): String =
    "${if (hour < 10) "0" else ""}$hour:${if (minute < 10) "0" else ""}$minute"

fun dateToString(day: Int, month: Int, year: Int): String =
    "${if (day < 10) "0" else ""}$day.${if (month < 10) "0" else ""}$month.${if (year < 10) "0" else ""}$year"
