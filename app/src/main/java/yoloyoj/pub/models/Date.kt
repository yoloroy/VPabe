package yoloyoj.pub.models

import yoloyoj.pub.utils.dateToString
import yoloyoj.pub.utils.timeToString
import yoloyoj.pub.utils.tryDefault
import java.util.Date as JDate

class Date(
    var dateid: String? = null,
    var year: Int? = null,
    var month: Int? = null,
    var day: Int? = null,
    var hour: Int? = null,
    var minute: Int? = null
) {
    constructor(
        year: Int? = null,
        month: Int? = null,
        day: Int? = null,
        hour: Int? = null,
        minute: Int? = null
    ) : this(null, year, month, day, hour, minute)

    val time: String
        get() = timeToString(hour?:0, minute?:0)

    val date: String
        get() = dateToString(day?:0, month?:0, year?:0)

    override fun toString(): String {
        return "$date $time"
    }

    fun toJsonString(): String {
        return "{ \n" +
                "dateid=${this.dateid},\n" +
                "month=${this.month},\n" +
                "day=${this.day},\n" +
                "time=${this.time}\n" +
                " }"
    }

    val javaDate: JDate get() =
        tryDefault(JDate()) {
            JDate(year!!, month!!, day!!, hour!!, minute!!)
        }
}
