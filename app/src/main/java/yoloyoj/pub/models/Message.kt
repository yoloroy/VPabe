package yoloyoj.pub.models

class Message {
    var _rowid_: Int? = null
    var text: String? = null
    var chatid: String? = null
    var sender: String? = null
    var senderId: String? = null
    var avatar: String? = null
    var attachments: List<Attachment>? = null

    fun hasNulls(): Boolean {
        return (text == null) or (sender == null)
    }
}
