package yoloyoj.pub.ui.chat

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_message.view.*
import yoloyoj.pub.models.Message

class MessageItemHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(message: Message, showUserName: Boolean, showUserAvatar: Boolean) {
        view.textView.text = message.text

        if (showUserName)
            view.senderView.text = message.sender
        else
            view.senderView.visibility = View.GONE

        if (showUserAvatar)
            // for future avatar viewing
        else {
            view.hideAvatar()
        }
    }

    private fun View.hideAvatar() {
        userView.visibility = View.INVISIBLE
        userView.layoutParams.height = 0

        avatarArrow.visibility = View.INVISIBLE
        messageBox.apply {
            setPadding(
                paddingLeft,
                paddingTop,
                paddingRight,
                0
            )
        }
    }
}