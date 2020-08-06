package yoloyoj.pub.ui.chat.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import yoloyoj.pub.models.ChatView
import yoloyoj.pub.storage.Storage

class ChatListViewModel : ViewModel() {
    var chats = MutableLiveData<List<ChatView>>().apply {
        value = emptyList()
    }

    fun start(userid: String) {
        Storage.observeChatList(userid, handler = { updChats ->
            if (
                (chats.value?.messagesSum() != updChats.messagesSum())
                and
                (updChats.isNotEmpty())
            )
                chats.value = updChats
        })
    }

    fun List<ChatView>.messagesSum(): String = joinToString { chat ->
        "${chat.chatid}${chat.lastMessage?.text}" }
}
