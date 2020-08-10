package yoloyoj.pub.models

import com.google.firebase.firestore.DocumentReference

public class Message (
    val sender: DocumentReference? = null,
    val text: String? = null,
    val attachments: List<Attachment>? = null
) {
    var _sender: User? = null

    constructor() : this(null)
}
