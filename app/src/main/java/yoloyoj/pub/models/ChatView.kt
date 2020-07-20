package yoloyoj.pub.models

class ChatView (
    val chatid: Int? = null,
    val userid: Int? = null,
    val chatAvatarLink: String? = null,
    val lastMessage: Message? = null
) {
    val sender: String
        get() = "${lastMessage!!.sender}: "

    val text: String
        get() = lastMessage!!.text!!

    override fun toString(): String = "{\n\tchatid=$chatid,\n\tuserid=$userid,\n\tchatAvatarLink=$chatAvatarLink,\n\tlastMessage=$lastMessage\n}"
}
