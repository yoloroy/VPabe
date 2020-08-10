package yoloyoj.pub.ui.chat.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import yoloyoj.pub.models.Event
import yoloyoj.pub.storage.Storage

class ChatListViewModel : ViewModel() {
    var chats = MutableLiveData<List<Event>>().apply {
        value = emptyList()
    }

    fun start(userid: String) {
        Storage.observeChatList(userid, handler = { updChats ->
            chats.value = updChats
        })
    }
}
