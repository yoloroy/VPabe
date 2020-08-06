package yoloyoj.pub.models.firebase

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint

public class Event (
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
    constructor() : this(null)
}
