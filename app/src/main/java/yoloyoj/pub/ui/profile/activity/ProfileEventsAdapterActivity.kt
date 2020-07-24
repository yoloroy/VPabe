package yoloyoj.pub.ui.profile.activity

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import yoloyoj.pub.R
import yoloyoj.pub.ui.event.view.EventActivity

class ProfileEventsAdapterActivity(
    var items: List<ProfileEventItemActivity>
) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(
            R.layout.item_profile_event,
            parent,
            false
        )
        return ProfileEventItemHolderActivity(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ProfileEventItemHolderActivity).bind(items[position])
        holder.view.setOnClickListener {
            val intent = Intent(it.context, EventActivity::class.java)
            intent.putExtra("eventid", items[position].eventId)
            (it.context as ProfileActivity).startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}
