package yoloyoj.pub.models

class ChatView (
    val chatid: Int? = null,
    val userid: Int? = null,
    val chatAvatar: String? = null,
    val lastMessage: Message? = null,
    val chatName: String? = null
) {
    val sender: String
        get() = if (!lastMessage!!.sender.isNullOrEmpty()) "${lastMessage.sender}: " else ""

    val text: String
        get() = lastMessage!!.text!!

    override fun toString(): String = "{\n\tchatid=$chatid,\n\tuserid=$userid,\n\tchatAvatarLink=$chatAvatar,\n\tlastMessage=$lastMessage\n}"
}
