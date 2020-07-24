package yoloyoj.pub.ui.attachment.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.arialyy.aria.core.Aria
import com.squareup.picasso.Picasso
import yoloyoj.pub.R
import yoloyoj.pub.models.Attachment
import yoloyoj.pub.models.TYPE_IMAGE
import yoloyoj.pub.ui.imageview.EXTRA_IMAGE_LINK
import yoloyoj.pub.ui.imageview.ImageViewActivity
import yoloyoj.pub.utils.tryDefault

open class AttachmentViewHolder(open val view: ImageView) : RecyclerView.ViewHolder(view) {

    open fun bind(attachment: Attachment) {
        when (attachment.attachment_type) {
            TYPE_IMAGE -> bindImage(attachment)
            else -> {
                view.setOnClickListener { TODO("Download and open something") }

                view.setImageResource(R.drawable.ic_document)
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

        tryDefault(Unit) {
            Picasso.get()
                .load(attachment.attachment_link)
                .noPlaceholder()
                .into(view)
        }
    }

    private fun bindSomething(attachment: Attachment) {
        val root = Environment.getExternalStorageDirectory()

        val permission =
            ActivityCompat.checkSelfPermission(view.context, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) { // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                view.context as Activity,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                1
            )
        }

        Aria.download(this)
            .load(attachment.attachment_link!!)
            .setDownloadPath(
                root.absolutePath +
                    "/download/" +
                    attachment.attachment_link!!.split("/").last() // file name
            )
            .start()
    }
}
