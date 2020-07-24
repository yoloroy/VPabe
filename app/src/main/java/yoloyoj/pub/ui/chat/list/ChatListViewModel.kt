package yoloyoj.pub.ui.chat.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import yoloyoj.pub.models.ChatView
import yoloyoj.pub.utils.tryDefault
import yoloyoj.pub.web.handlers.ChatListGetter

class ChatListViewModel : ViewModel() {
    private var chatListGetter: ChatListGetter? = null

    var chats = MutableLiveData<List<ChatView>>().apply {
        value = emptyList()
    }

    fun start(userid: Int) {
        chatListGetter = ChatListGetter(userid) { updChats ->
            if (
                (chats.value?.messagesSum() != updChats.messagesSum())
                and
                (updChats.isNotEmpty())
            )
                chats.value = updChats

            chatListGetter!!.start(
                chats.value!!.count(),
                tryDefault(0) {
                    chats.value!!.map { it.lastMessage?._rowid_ }.sumBy { it!! }
                }
            )
        }

        chatListGetter!!.start()
    }

    fun List<ChatView>.messagesSum(): String = joinToString { chat ->
        "${chat.chatid}${chat.lastMessage?.text}" }
}