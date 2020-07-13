package yoloyoj.pub.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import yoloyoj.pub.models.Message
import yoloyoj.pub.web.handlers.MessageGetter

class ChatViewModel : ViewModel() {
    private lateinit var messageGetter: MessageGetter

    var messages = MessagesData().apply {
        value = emptyList()
    }

    init {
        loadHandlers()

        messageGetter.start(
            MY_CHAT_ID,
            0
        )
    }

    private fun loadHandlers() {
        messageGetter = MessageGetter().apply {
            messageUpdater = { updMessages ->
                messages.value = messages.value!! + updMessages

                if (updMessages.isNotEmpty())
                    messageGetter.start(
                        updMessages.last().chatid!!,
                        updMessages.last()._rowid_!!
                    )
            }
        }
    }
}

class MessagesData: MutableLiveData<List<Message>>()
