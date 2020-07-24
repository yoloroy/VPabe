package yoloyoj.pub.ui.event.view

import androidx.lifecycle.ViewModel
import yoloyoj.pub.ui.chat.view.MY_USER_ID
import yoloyoj.pub.ui.event.EventData
import yoloyoj.pub.web.handlers.EventGetter

class EventViewModel : ViewModel() {
    private lateinit var eventGetter: EventGetter

    var events = EventData().apply {
        value = emptyList()
    }

    init {
        loadHandlers()
        eventGetter.start(
            MY_USER_ID
        )
    }




    private fun loadHandlers() {
        eventGetter = EventGetter().apply {
            eventListener = { updEvents ->
                events.value = events.value!! + updEvents

                if (updEvents.isNotEmpty()) {
                    eventGetter.start()
                }
            }
        }
    }
}
