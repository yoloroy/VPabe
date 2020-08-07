package yoloyoj.pub.models.firebase

import com.google.firebase.firestore.DocumentReference
import yoloyoj.pub.models.Message

public class Message (
    val sender: DocumentReference? = null,
    val text: String? = null,
    val attachments: List<Attachment>? = null
) {
    var _sender: User? = null

    constructor() : this(null)

    fun toApp(i: Int, eventid: String): Message {
        return Message().apply {
            _rowid_ = i
            text = this@Message.text
            chatid = eventid
            sender = this@Message._sender?.name
            senderId = this@Message.sender?.id
            avatar = this@Message._sender?.avatar
            attachments = this@Message.attachments?.map { it.toApp() }
        }
    }
}
