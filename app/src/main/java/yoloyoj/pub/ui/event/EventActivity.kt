package yoloyoj.pub.ui.event

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_event.*
import yoloyoj.pub.R
import yoloyoj.pub.web.apiClient
import yoloyoj.pub.web.handlers.SingleEventGetter

class EventActivity : AppCompatActivity() {

    private lateinit var eventViewModel: EventViewModel
    private lateinit var events: EventData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        eventViewModel = ViewModelProviders.of(this).get(EventViewModel::class.java)
        events = eventViewModel.events
        eventGetInfo()

        supportActionBar?.setDisplayHomeAsUpEnabled(false);
        supportActionBar?.setHomeButtonEnabled(false);
    }

    private fun eventGetInfo() {
        val eventId = intent?.getIntExtra("eventid", -1)
        if (eventId == null || eventId == -1) {
            finish()
        }
        apiClient.getSingleEvent(
            eventId!!
        )?.enqueue(
            SingleEventGetter(
                this
            ) {
                event_name_header.text = it.name
                event_describe_header.text = it.description
                event_date_header.text = it.date?.day.toString()
                event_place_header.text = it.place
                // Picasso.get().load(it.photo).into(event_image)
            }
        )
    }
}







