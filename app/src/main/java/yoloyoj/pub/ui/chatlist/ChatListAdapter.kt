package yoloyoj.pub.ui.chatlist

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_chat.view.*
import yoloyoj.pub.R
import yoloyoj.pub.models.ChatView
import yoloyoj.pub.ui.chat.ChatActivity
import yoloyoj.pub.ui.chat.EXTRA_CHATID
import yoloyoj.pub.ui.event.STANDARD_EVENT_IMAGE
import yoloyoj.pub.utils.tryDefault

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
        if (chatView.chatAvatar.isNullOrEmpty()) {
            tryDefault(true) {
                Picasso.get().load(STANDARD_EVENT_IMAGE).into(view.chatAvatar)
            }
        } else {
            tryDefault(true) {
                Picasso.get().load(chatView.chatAvatar).into(view.chatAvatar)
            }
        }

        view.apply {
            chatName.text = chatView.chatName
            lastSender.text = chatView.sender
            lastMessage.text = chatView.text

            setOnClickListener {
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra(
                    EXTRA_CHATID,
                    chatView.chatid
                )


                context.startActivity(intent)
            }
        }
    }
}
