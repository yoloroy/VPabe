package yoloyoj.pub.ui.chatlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_chat.view.*
import yoloyoj.pub.R
import yoloyoj.pub.models.ChatView

class ChatListAdapter(
    private val items: List<ChatView>
) : RecyclerView.Adapter<ChatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ChatViewHolder(
            inflater.inflate(
                R.layout.item_chat,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

class ChatViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(chatView: ChatView) {
        // view.chatAvatar TODO: set image(or holder) from picasso
        view.apply {
            chatName.text = chatView.chatid.toString() // TODO: add chat names

            lastSender.text = chatView.sender
            lastMessage.text = chatView.text
        }
    }
}
