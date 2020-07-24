package yoloyoj.pub.ui.utils.attachment.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import yoloyoj.pub.R
import yoloyoj.pub.models.Attachment

class AttachmentViewAdapter(
    private val items: List<Attachment>
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(parent!!.context)

        AttachmentViewHolder(
            inflater.inflate(
                R.layout.item_image,
                parent,
                false
            ) as ImageView
        ).apply {
            bind(items[position])
            return view
        }
    }

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = 0

    override fun getCount(): Int = items.size

}
