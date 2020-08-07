package yoloyoj.pub.models.firebase

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import yoloyoj.pub.models.Date
import yoloyoj.pub.models.Event
import yoloyoj.pub.models.Like

typealias FEvent = yoloyoj.pub.models.firebase.Event

public data class Event (
    val author: DocumentReference? = null,
    val avatar: String? = null,
    val name: String? = null,
    val description: String? = null,
    val likes: Int? = null,
    val messages: List<Message>? = null,
    val place: String? = null,
    val latlng: GeoPoint? = null,
    val date: Timestamp? = null,
    val subscribers: List<DocumentReference>? = null
) {
    val javaDate: java.util.Date? get() = date?.toDate()

    companion object {
        const val AUTHOR = "author"
        const val AVATAR = "avatar"
        const val NAME = "name"
        const val DESCRIPTION = "description"
        const val LIKES = "likes"
        const val MESSAGES = "messages"
        const val PLACE = "place"
        const val LATLNG = "latlng"
        const val DATE = "date"
        const val SUBSCRIBERS = "subscribers"
    }

    constructor() : this(null)

    fun toApp(id: String): Event =
        Event(
            eventid = id,
            name = name,
            description = description,
            like = Like(null, likes),
            authorid = author?.id,
            place = place,
            lat = latlng?.latitude,
            lng = latlng?.longitude,
            avatar = avatar,
            date = with(javaDate!!) {
                Date(year, month, day, hours, minutes)
            }
        )
}
