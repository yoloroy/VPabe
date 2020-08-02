package yoloyoj.pub.models

class Message {
    var _rowid_: Int? = null
    var text: String? = null
    var chatid: Int? = null
    var sender: String? = null
    var senderId: Int? = null
    var avatar: String? = null
    var attachments: List<Attachment>? = null

    fun hasNulls(): Boolean =
        (text == null) or (sender == null)
}
