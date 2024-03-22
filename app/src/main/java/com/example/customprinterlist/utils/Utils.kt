package com.example.customprinterlist.utils

import android.util.Log

fun log(message: String, tag: String = "PrinterService") {
    Log.d(tag, "$message")
}