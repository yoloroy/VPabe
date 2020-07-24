package yoloyoj.pub.ui.event

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import yoloyoj.pub.models.Event
import yoloyoj.pub.ui.chat.MY_USER_ID
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

class EventData : MutableLiveData<List<Event>>()
