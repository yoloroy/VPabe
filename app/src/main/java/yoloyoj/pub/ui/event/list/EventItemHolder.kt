package yoloyoj.pub.ui.event.list

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_events_list.view.*
import yoloyoj.pub.R
import yoloyoj.pub.models.Event
import yoloyoj.pub.ui.event.view.EventActivity
import yoloyoj.pub.ui.event.view.STANDARD_EVENT_IMAGE

class EventItemHolder(val view: View) : RecyclerView.ViewHolder(view) {

    lateinit var eventItem: Event
    fun bind(eventItem: Event) {
        view.apply {
            event_name_header.text = eventItem.name
            event_describe_header.text = eventItem.description
            event_date_header.text = resources.getString(
                R.string.date_time,
                eventItem.date?.day ?: 0,
                eventItem.date?.month ?: 0,
                eventItem.date?.year ?: 0,
                eventItem.date?.hour ?: 0,
                eventItem.date?.minute ?: 0
            )
            event_place_header.text = eventItem.place
            if (eventItem.avatar.isNullOrEmpty()) {
                Picasso.get().load(STANDARD_EVENT_IMAGE).into(event_image)
            } else {
                Picasso.get().load(eventItem.avatar).into(event_image)
            }
            setOnClickListener {
                val intent = Intent(context,  EventActivity::class.java)
                intent.putExtra("eventid", eventItem.eventid)
                context.startActivity(intent)
            }
        }

        this.eventItem = eventItem
    }
}
