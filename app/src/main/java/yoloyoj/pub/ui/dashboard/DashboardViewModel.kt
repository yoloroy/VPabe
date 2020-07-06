package yoloyoj.pub.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import yoloyoj.pub.models.Action
import yoloyoj.pub.models.Message
import yoloyoj.pub.web.handlers.ActionGetter
import yoloyoj.pub.web.handlers.MessageGetter

class DashboardViewModel : ViewModel() {
    private lateinit var messageGetter: MessageGetter
    private lateinit var actionGetter: ActionGetter

    var messages = MessagesData()

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

class MessagesData: MutableLiveData<List<Message>>() {
    val texts get() = value?.map { it.text!! }
}
