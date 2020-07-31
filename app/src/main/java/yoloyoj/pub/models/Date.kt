package yoloyoj.pub.models

import yoloyoj.pub.utils.dateToString
import yoloyoj.pub.utils.timeToString

class Date(
    var dateid: Int? = null,
    var year: Int? = null,
    var month: Int? = null,
    var day: Int? = null,
    var hour: Int? = null,
    var minute: Int? = null
) {
    val time: String
        get() = "$hour:$minute"

    val date: String
        get() = "$day.$month.$year"

    override fun toString(): String {
        return "${dateToString(day?:0, month?:0, year?:0)} ${timeToString(hour?:0, minute?:0)}"
    }

    fun toJsonString(): String {
        return "{ \n" +
                "dateid=${this.dateid},\n" +
                "month=${this.month},\n" +
                "day=${this.day},\n" +
                "time=${this.time}\n" +
                " }"
    }
}
