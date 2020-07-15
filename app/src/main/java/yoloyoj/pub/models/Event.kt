package yoloyoj.pub.models

class Event(
    var eventid: Int? = null,
    var name: String? = null,
    var description: String? = null,
    var date: Date? = null,
    var like: Like? = null,
    var authorid: Int? = null
) {
    override fun toString(): String {
        return "{ \n" +
                "eventid=${this.eventid},\n" +
                "name=${this.name},\n" +
                "description=${this.description},\n" +
                "date=${this.date.toString().replace("\n", "\n\t")},\n" +
                "like=${this.like.toString().replace("\n", "\n\t")},\n" +
                "authorid=${this.authorid}\n" +
                " }"
    }
}
