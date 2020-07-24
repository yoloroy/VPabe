package yoloyoj.pub.ui.list

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_events_list.view.*
import yoloyoj.pub.models.Event
import yoloyoj.pub.ui.event.EventActivity
import yoloyoj.pub.ui.event.STANDARD_EVENT_IMAGE
import yoloyoj.pub.utils.dateToString
import yoloyoj.pub.utils.timeToString

class EventItemHolder(val view: View) : RecyclerView.ViewHolder(view) {

    lateinit var eventItem: Event
    fun bind(eventItem: Event) {
        view.apply {

            event_name_header.text = eventItem.name
            event_describe_header.text = eventItem.description
            event_date_header.text = dateToString(
                eventItem.date?.day ?: 0,
                eventItem.date?.month ?: 0,
                eventItem.date?.year ?: 0
            ) + " " + timeToString(
                eventItem.date?.hour ?: 0,
                eventItem.date?.minute ?: 0
            ) // TODO: Replace with a resource string
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
