package com.example.part5_chapter2.extension

import android.content.res.Resources

internal fun Float.fromDpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}