package yoloyoj.pub.models

import yoloyoj.pub.utils.dateToString
import yoloyoj.pub.utils.timeToString
import yoloyoj.pub.utils.tryDefault
import java.util.Date as JDate

class Date(
    var year: Int? = null,
    var month: Int? = null,
    var day: Int? = null,
    var hour: Int? = null,
    var minute: Int? = null
) {

    val time: String
        get() = timeToString(hour?:0, minute?:0)

    val date: String
        get() = dateToString(day?:0, month?:0, (year?:0)%100)

    override fun toString(): String {
        return "$date $time"
    }

    val javaDate: JDate get() =
        tryDefault(JDate()) {
            JDate(year!!, month!!, day!!, hour!!, minute!!)
        }
}
