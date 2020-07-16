package yoloyoj.pub.ui.attachment.view

import android.content.Intent
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import yoloyoj.pub.R
import yoloyoj.pub.models.Attachment
import yoloyoj.pub.models.TYPE_DOCUMENT
import yoloyoj.pub.models.TYPE_IMAGE
import yoloyoj.pub.ui.imageview.EXTRA_IMAGE_LINK
import yoloyoj.pub.ui.imageview.ImageViewActivity

class AttachmentViewHolder(val view: ImageView) : RecyclerView.ViewHolder(view) {

    fun bind(attachment: Attachment) {
        when(attachment.attachment_type) {
            TYPE_IMAGE -> bindImage(attachment)
            TYPE_DOCUMENT -> {
                view.setOnClickListener { TODO("Show document or download") }

                view.setImageResource(R.drawable.ic_document)
            }
            else -> {
                view.setOnClickListener { TODO("Download and open something") }

                view.setImageResource(R.drawable.ic_file_download)
            }
        }
    }

    private fun bindImage(attachment: Attachment) {
        view.setOnClickListener {
            val intent = Intent(view.context, ImageViewActivity::class.java)
            intent.putExtra(EXTRA_IMAGE_LINK, attachment.attachment_link)

            view.context.startActivity(intent)
        }
        view.setPadding(0, 0, 0, 0)
        view.alpha = 1f

        Picasso.get()
            .load(attachment.attachment_link)
            .noPlaceholder()
            .into(view)
    }
}
