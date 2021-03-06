package yoloyoj.pub.ui.profile.fragment

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_profile_event.view.*

class ProfileEventItemHolder(val view: View) : RecyclerView.ViewHolder(view) {
    lateinit var eventItem: ProfileEventItem
    fun bind(eventItem: ProfileEventItem) {
        Picasso.get().load(eventItem.eventImageLink).into(view.eventImage)
        view.eventName.text = eventItem.eventName
        this.eventItem = eventItem
    }
}
