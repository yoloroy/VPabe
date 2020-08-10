package yoloyoj.pub.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint

public data class Event (
    val author: DocumentReference? = null,
    val avatar: String? = null,
    val name: String? = null,
    val description: String? = null,
    val likes: Int? = 0,
    val messages: List<Message>? = emptyList(),
    val place: String? = null,
    val latlng: GeoPoint? = null,
    val date: Timestamp? = null,
    val subscribers: List<DocumentReference>? = emptyList()
) {
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

    lateinit var id: String

    val sender: String
        get() = "${messages!!.last()._sender?.name?: ""}: "

    val lastMessage: Message
        get() = messages!!.last()

    val javaDate: java.util.Date? get() = date?.toDate()

    val beautyDate: String get() =
        with(javaDate!!) {
            Date(year, month, day, hours, minutes).toString()
        }

    constructor() : this(null)
}
