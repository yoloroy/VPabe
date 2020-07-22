package yoloyoj.pub.ui.list

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_events_list.view.*
import yoloyoj.pub.models.Event
import yoloyoj.pub.ui.event.EventActivity
import yoloyoj.pub.ui.event.STADNARD_EVENT_IMAGE

class EventItemHolder(val view: View) : RecyclerView.ViewHolder(view) {

        lateinit var eventItem: Event
        fun bind(eventItem: Event) {
            view.apply {

                event_name_header.text = eventItem.name
                event_describe_header.text = eventItem.description
                event_date_header.text = "${eventItem.date?.day}.${eventItem.date?.month} ${eventItem.date?.hour}:${eventItem.date?.minute}"
                event_place_header.text = eventItem.place
                if (eventItem.avatar.isNullOrEmpty()) {
                    Picasso.get().load(STADNARD_EVENT_IMAGE).into(event_image)
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


