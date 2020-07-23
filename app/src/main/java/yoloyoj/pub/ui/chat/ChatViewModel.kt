package yoloyoj.pub.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import yoloyoj.pub.models.Attachment
import yoloyoj.pub.models.Message
import yoloyoj.pub.web.handlers.MessageGetter

class ChatViewModel : ViewModel() {
    lateinit var messageGetter: MessageGetter
    var chatid: Int? = null

    var messages = MessagesData().apply {
        value = emptyList()
    }

    var attachments = MutableLiveData<MutableList<Attachment>>().apply {
        value = mutableListOf()
    }

    init {
        loadHandlers()
    }

    private fun loadHandlers() {
        messageGetter = MessageGetter { updMessages ->
            if (updMessages.isNotEmpty()) {
                messages.value = messages.value!! + updMessages
                messageGetter.start(
                    updMessages.last().chatid!!,
                    updMessages.last()._rowid_!!
                )
            } else
                messageGetter.start(
                    chatid!!,
                    when (messages.value) {
                        mutableListOf<Attachment>() -> 0
                        null -> 0
                        else -> messages.value!!.last()._rowid_!!
                    }
                )
        }
    }
}

class MessagesData: MutableLiveData<List<Message>>()
