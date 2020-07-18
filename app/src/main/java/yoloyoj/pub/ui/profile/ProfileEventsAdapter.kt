package yoloyoj.pub.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import yoloyoj.pub.R

class ProfileEventsAdapter(
    var items: List<Any>
) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(
            R.layout.item_profile_event,
            parent,
            false
        )
        v.setOnClickListener {
            // Open event fragment
        }
        return ProfileEventItemHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ProfileEventItemHolder).bind(items[position] as ProfileEventItem)
    }

    override fun getItemCount(): Int = items.size
}
