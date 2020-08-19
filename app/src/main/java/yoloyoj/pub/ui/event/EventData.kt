package yoloyoj.pub.ui.event

import android.location.Location
import androidx.lifecycle.MutableLiveData
import yoloyoj.pub.models.Event

class EventData : MutableLiveData<List<Event>>()

typealias MutableLocation = MutableLiveData<Location>
