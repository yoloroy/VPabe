package yoloyoj.pub.models.firebase

import com.google.firebase.firestore.DocumentReference

public class Message (
    val sender: DocumentReference? = null,
    val text: String? = null,
    val attachments: List<Attachment>? = null
) {
    constructor() : this(null)
}
