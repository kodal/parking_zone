package com.netfok.parkzone.extensions

fun Long.toHoursWithMinutes(): String {
    val seconds = this / 1000
    val minutes = seconds / 60 % 60
    val hours = seconds / 60 / 60
    return "$hours:${minutes / 10}${minutes % 10}"
}