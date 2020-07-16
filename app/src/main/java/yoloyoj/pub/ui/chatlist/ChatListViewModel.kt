package yoloyoj.pub.ui.chatlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import yoloyoj.pub.models.ChatView
import yoloyoj.pub.ui.chat.MY_USER_ID
import yoloyoj.pub.web.handlers.ChatListGetter

class ChatListViewModel : ViewModel() {
    private var chatListGetter: ChatListGetter? = null

    var chats = MutableLiveData<List<ChatView>>()

    init {
        chatListGetter = ChatListGetter(MY_USER_ID) {
            chats.value = it

            chatListGetter!!.start()
        }

        chatListGetter!!.start()
    }
}