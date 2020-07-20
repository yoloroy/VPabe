package yoloyoj.pub.ui.list

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_events_list.view.*
import yoloyoj.pub.models.Event
import yoloyoj.pub.ui.event.EventActivity

class EventItemHolder(val view: View) : RecyclerView.ViewHolder(view) {

        lateinit var eventItem: Event
        fun bind(eventItem: Event) {
            view.apply {

                event_name_header.text = eventItem.name
                event_describe_header.text = eventItem.description
                event_date_header.text = eventItem.date?.day.toString()
                event_place_header.text = eventItem.place



                setOnClickListener {
                    val intent = Intent(context,  EventActivity ::class.java)
                    intent.putExtra("event name",
                        eventItem.name
                    )
                    intent.putExtra("event description",
                        eventItem.description
                    )

                    intent.putExtra("event date",
                        eventItem.date?.day.toString()
                    )
                    intent.putExtra("event place",
                        eventItem.place
                    )


                    context.startActivity(intent)
                }
            }

            this.eventItem = eventItem
        }
    }


