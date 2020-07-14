package yoloyoj.pub.models

class Date(
    var dateid: Int? = null,
    var month: Int? = null,
    var day: Int? = null,
    var hour: Int? = null,
    var minute: Int? = null
) {
    val time: String
        get() = "$hour:$minute"
}
