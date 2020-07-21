package yoloyoj.pub.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_events_list.view.*
import yoloyoj.pub.R
import yoloyoj.pub.models.Event


class EventListAdapter(
    private val items: List<Event>
): RecyclerView.Adapter<EventItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventItemHolder {
        val inflater = LayoutInflater.from(parent.context)

        return EventItemHolder(
            inflater.inflate(
                R.layout.item_events_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EventItemHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}