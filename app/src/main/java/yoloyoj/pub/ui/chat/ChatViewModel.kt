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
    private lateinit var userGetter: UserGetter

    var messages = MessagesData()
    var users = MutableLiveData<MutableMap<Int, User>>().apply {
        value = mutableMapOf()
    }

    val texts: List<String>?
        get() = messages.value?.map {
            if (it.sender!! !in users.value?.keys!!)
                userGetter.start(it.sender!!)

            "${users.value!![it.sender!!]!!.username}: ${it.text}"
        }

    init {
        loadHandlers()

        actionGetter.start()
        messageGetter.start()
        userGetter.start()
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

        userGetter = UserGetter {
            // will be removed after converting to recycleView
            messages.value = messages.value

            users.value?.put(it.userid!!, it)
        }
    }
}

class MessagesData: MutableLiveData<List<Message>>()
