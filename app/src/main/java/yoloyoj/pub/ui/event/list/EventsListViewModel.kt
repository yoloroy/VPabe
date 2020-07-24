package yoloyoj.pub.ui.event.list

import androidx.lifecycle.ViewModel
import yoloyoj.pub.ui.event.EventData
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
