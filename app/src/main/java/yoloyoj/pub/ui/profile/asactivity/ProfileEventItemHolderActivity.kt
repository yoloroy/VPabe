package yoloyoj.pub.ui.profile.asactivity

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_profile_event.view.*

class ProfileEventItemHolderActivity(val view: View) : RecyclerView.ViewHolder(view) {
    lateinit var eventItem: ProfileEventItemActivity
    fun bind(eventItem: ProfileEventItemActivity) {
        Picasso.get().load(eventItem.eventImageLink).into(view.eventImage)
        view.eventName.text = eventItem.eventName
        this.eventItem = eventItem
    }
}
