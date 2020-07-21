package yoloyoj.pub.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import yoloyoj.pub.models.Event
import yoloyoj.pub.ui.chat.MY_USER_ID
import yoloyoj.pub.web.handlers.EventGetter

class EventsListViewModel : ViewModel() {
    private lateinit var eventGetter: EventGetter

    var events = EventData().apply {
        value = emptyList()
    }

    init {
        loadHandlers()

        eventGetter.start(
            eventid = 0
        )
    }

    private fun loadHandlers() {
        eventGetter = EventGetter().apply {
            eventListener = { updEvents ->
                if (updEvents.isNotEmpty()) {
                    events.value = updEvents
                }
                eventGetter.start(
                    eventid = events.value?.map { it.eventid!! }?.maxBy { it }!!
                )
            }
        }
    }
}

class EventData: MutableLiveData<List<Event>>()