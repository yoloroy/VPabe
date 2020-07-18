package yoloyoj.pub.ui.profile

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_profile_event.view.*

class ProfileEventItemHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    lateinit var eventItem: ProfileEventItem
    fun bind(eventItem: ProfileEventItem) {
        Picasso.get().load(eventItem.eventImageLink).into(view.eventImage)
        view.eventDescription.text = eventItem.eventDescription
        this.eventItem = eventItem
    }
}
