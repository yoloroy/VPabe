package yoloyoj.pub.models

open class Event(
    open val eventid: String? = null,
    open val name: String? = null,
    open val description: String? = null,
    open val date: Date? = null,
    open val like: Like? = null,
    open val authorid: String? = null,
    open val place: String? = null,
    open val lat: Double? = null,
    open val lng: Double? = null,
    open val avatar: String? = null
) {
    override fun toString(): String {
        return "{ \n" +
                "eventid=${this.eventid},\n" +
                "place=${this.place},\n" +
                "name=${this.name},\n" +
                "description=${this.description},\n" +
                "date=${this.date!!.toJsonString().replace("\n", "\n\t")},\n" +
                "like=${this.like.toString().replace("\n", "\n\t")},\n" +
                "authorid=${this.authorid}\n" +
                " }"
    }
}
