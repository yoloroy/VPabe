package yoloyoj.pub.ui.attachment.preview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import yoloyoj.pub.R
import yoloyoj.pub.models.Attachment
import yoloyoj.pub.ui.attachment.view.AttachmentViewHolder

class AttachmentPreviewAdapter (
    val recyclerView: RecyclerView,
    var items: MutableLiveData<MutableList<Attachment>>
) : RecyclerView.Adapter<AttachmentPreviewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentPreviewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return AttachmentPreviewHolder(
            inflater.inflate(
                R.layout.item_image,
                parent,
                false
            ) as ImageView
        ) { position ->
            items.value!!.removeAt(position)
            items.value = items.value
        }
    }

    override fun getItemCount(): Int = items.value!!.size

    override fun onBindViewHolder(holder: AttachmentPreviewHolder, position: Int) {
        holder.bind(items.value!![position], position)
    }

}

class AttachmentPreviewHolder(
    override val view: ImageView,
    val onDeleteListener: (position: Int) -> Unit
) : AttachmentViewHolder(view) {

    fun bind(attachment: Attachment, position: Int) {
        super.bind(attachment)
        view.setOnClickListener { onDeleteListener(position) }
    }
}
