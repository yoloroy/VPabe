package yoloyoj.pub.utils

fun <T> tryDefault(default: T, expression: () -> T): T =
    try {
        expression()
    } catch (e: Exception) {
        default
    }
