package yoloyoj.pub.ui.event.list

import androidx.lifecycle.ViewModel
import yoloyoj.pub.storage.Storage
import yoloyoj.pub.ui.event.EventData

class EventsListViewModel : ViewModel() {
    var events = EventData().apply {
        value = emptyList()
    }

    init {
        loadHandlers()
    }

    private fun loadHandlers() {
        Storage.observeAllEvents { updEvents ->
                if (updEvents.isNotEmpty()) {
                    events.value = updEvents
                }
        }
    }
}
