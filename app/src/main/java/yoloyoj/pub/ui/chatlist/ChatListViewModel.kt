package yoloyoj.pub.ui.chatlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import yoloyoj.pub.models.ChatView
import yoloyoj.pub.ui.chat.MY_USER_ID
import yoloyoj.pub.web.handlers.ChatListGetter

class ChatListViewModel : ViewModel() {
    private var chatListGetter: ChatListGetter? = null

    var chats = MutableLiveData<List<ChatView>>().apply {
        value = emptyList()
    }

    init {
        chatListGetter = ChatListGetter(MY_USER_ID) { updChats ->
            if (
                (chats.value?.messagesSum() != updChats.messagesSum())
                and
                (updChats.isNotEmpty())
            )
                chats.value = updChats

            chatListGetter!!.start(
                chats.value!!.count(),
                chats.value!!.map { it.lastMessage?._rowid_ }.sumBy { it!! }
            )
        }

        chatListGetter!!.start()
    }

    fun List<ChatView>.messagesSum(): String = joinToString { chat ->
        "${chat.chatid}${chat.lastMessage?.text}" }
}