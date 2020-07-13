package yoloyoj.pub.ui.chat

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_message.view.*
import yoloyoj.pub.models.Message
import yoloyoj.pub.utils.toDp

class MessageItemHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(message: Message, showUserName: Boolean, showUserAvatar: Boolean) {
        view.textView.text = message.text

        if (showUserName) {
            view.senderView.visibility = View.VISIBLE
            view.senderView.text = message.sender
        } else {
            view.senderView.visibility = View.GONE
        }

        if (showUserAvatar) {
            view.showAvatar()
        } else {
            view.hideAvatar()
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
}