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
            // TODO: Convert to long-pull
            if (it != null) {
                if (
                    chats.value?.joinToString { chat ->
                        "${chat.chatid}${chat.lastMessage?.text}" }
                    !=
                    it.joinToString { chat ->
                        "${chat.chatid}${chat.lastMessage?.text}" }
                )
                    chats.value = it
                else
                    Thread.sleep(500)
            }

            chatListGetter!!.start()
        }

        chatListGetter!!.start()
    }
}