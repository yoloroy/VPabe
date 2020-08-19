package yoloyoj.pub.ui.event.list

import androidx.lifecycle.ViewModel
import yoloyoj.pub.storage.Storage
import yoloyoj.pub.ui.event.EventData
import yoloyoj.pub.ui.event.MutableLocation
import yoloyoj.pub.ui.event.list.EventsListFragment.Companion.LATLNG_DISTANCE

class EventsListViewModel : ViewModel() {
    var events = EventData().apply {
        value = emptyList()
    }

    var location = MutableLocation()

    init {
        loadHandlers()
    }

    private fun loadHandlers() {
        Storage.observeEventsNearMutableLocation(
            location, LATLNG_DISTANCE
        ) { updEvents ->
            events.value = updEvents
        }
    }
}
