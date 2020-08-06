package yoloyoj.pub.ui.chat.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import yoloyoj.pub.models.Attachment
import yoloyoj.pub.models.Message
import yoloyoj.pub.storage.Storage

class ChatViewModel : ViewModel() {
    var chatid: String? = null

    var messages = MessagesData().apply {
        value = emptyList()
    }

    var attachments = MutableLiveData<MutableList<Attachment>>().apply {
        value = mutableListOf()
    }

    fun startMessageObserving(chatid: String) {
        Storage.observeNewMessages(chatid, 0) { newMessages ->
            messages.value = messages.value!! + newMessages
        }
    }
}

class MessagesData: MutableLiveData<List<Message>>()
