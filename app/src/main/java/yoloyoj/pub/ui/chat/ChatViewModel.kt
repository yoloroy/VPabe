package yoloyoj.pub.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import yoloyoj.pub.models.Action
import yoloyoj.pub.models.Message
import yoloyoj.pub.models.User
import yoloyoj.pub.web.handlers.ActionGetter
import yoloyoj.pub.web.handlers.MessageGetter
import yoloyoj.pub.web.handlers.UserGetter

class ChatViewModel : ViewModel() {
    private lateinit var messageGetter: MessageGetter
    private lateinit var actionGetter: ActionGetter

    var messages = MessagesData()
    var users = MutableLiveData<MutableMap<Int, User>>().apply {
        value = mutableMapOf()
    }

    init {
        loadHandlers()

        actionGetter.start()
        messageGetter.start()
    }

    private fun loadHandlers() {
        messageGetter = MessageGetter().apply {
            messageUpdater = { updMessages ->
                messages.value = updMessages
            }
        }

        actionGetter = ActionGetter().apply {
            actionListener = { action -> when (action) {
                    Action.MESSAGE -> messageGetter.start()
            }}
        }
    }
}

class MessagesData: MutableLiveData<List<Message>>()
