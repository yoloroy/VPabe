package yoloyoj.pub.ui.chat

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_message.view.*
import yoloyoj.pub.R
import yoloyoj.pub.models.Attachment
import yoloyoj.pub.models.Message
import yoloyoj.pub.ui.attachment.view.AttachmentsViewActivity
import yoloyoj.pub.utils.toDp

const val ATTACHMENTS_TYPES = "at"
const val ATTACHMENTS_LINKS = "al"

class MessageItemHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(message: Message, showUserName: Boolean, showUserAvatar: Boolean) {
        if (!showUserName and message.text.isNullOrBlank())
            view.messageView.visibility = View.GONE
        else
            view.messageView.visibility = View.VISIBLE

        view.textView.text = message.text

        if (showUserName) {
            view.senderView.visibility = View.VISIBLE
            view.senderView.text = message.sender
        } else {
            view.senderView.visibility = View.GONE
        }

        if (showUserAvatar) {
            view.showAvatar()

            if (message.avatar!!.isNotEmpty())
                Picasso.get()
                    .load(message.avatar)
                    .placeholder(R.drawable.ic_person)
                    .into(view.userView)
        } else {
            view.hideAvatar()
        }

        if (message.attachments!!.isNotEmpty()) {
            view.showAttachments(message.attachments!!)
        } else {
            view.attachmentButton.visibility = View.GONE
        }
    }

    private fun View.hideAvatar() {
        userView.visibility = View.INVISIBLE
        userView.layoutParams.height = 0

        avatarArrow.visibility = View.GONE
    }

    private fun View.showAvatar() {
        userView.visibility = View.VISIBLE
        userView.layoutParams.height = 48.toDp(userView)

        avatarArrow.visibility = View.VISIBLE
    }

    private fun View.showAttachments(attachments: List<Attachment>) {
        attachmentButton.visibility = View.VISIBLE

        attachmentButton.setOnClickListener {
            val intent = Intent(context, AttachmentsViewActivity::class.java)
            intent.putExtra(
                ATTACHMENTS_TYPES,
                attachments.map { it.attachment_type!! }.toTypedArray()
            )
            intent.putExtra(
                ATTACHMENTS_LINKS,
                attachments.map { it.attachment_link!! }.toTypedArray()
            )

            context.startActivity(intent)
        }
    }
}