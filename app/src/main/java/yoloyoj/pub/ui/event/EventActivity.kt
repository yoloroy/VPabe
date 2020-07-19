package yoloyoj.pub.ui.event

import android.os.Bundle
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import yoloyoj.pub.R

class EventActivity : AppCompatActivity() {

    private lateinit var eventViewModel: EventViewModel
    private lateinit var events: EventData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        eventViewModel = ViewModelProviders.of(this).get(EventViewModel::class.java)
        events = eventViewModel.events
        eventGetInfo()
    }

    private fun eventGetInfo() {
        val eventName:TextView = findViewById(R.id.event_name_header)
        val eventDescribe:TextView = findViewById(R.id.event_describe_header)
        val eventDate:TextView = findViewById(R.id.event_date_header)
        val eventPlace:TextView = findViewById(R.id.event_place_header)
     //get event info
        val intent = intent
        val eventNameGet = intent.getStringExtra("event name")
        val eventDescribeGet = intent.getStringExtra("event description")
        val eventDateGet = intent.getStringExtra("event date")
        val eventPlaceGet = intent.getStringExtra("event place")
     //view event info
        eventName.text = eventNameGet
        eventDescribe.text = eventDescribeGet
        eventDate.text = eventDateGet
        eventPlace.text = eventPlaceGet
    }
}




