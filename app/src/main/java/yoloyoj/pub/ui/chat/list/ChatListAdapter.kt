package yoloyoj.pub.ui.chat.list

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_chat.view.*
import yoloyoj.pub.R
import yoloyoj.pub.models.Event
import yoloyoj.pub.ui.chat.view.ChatActivity
import yoloyoj.pub.ui.chat.view.EXTRA_CHATID
import yoloyoj.pub.ui.event.view.STANDARD_EVENT_IMAGE
import yoloyoj.pub.utils.tryDefault

class ChatListAdapter(
    private val items: List<Event>
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
    fun bind(chatView: Event) {
        if (chatView.avatar.isNullOrEmpty()) {
            tryDefault(true) {
                Picasso.get().load(STANDARD_EVENT_IMAGE).into(view.chatAvatar)
            }
        } else {
            tryDefault(true) {
                Picasso.get().load(chatView.avatar).into(view.chatAvatar)
            }
        }

        view.apply {
            chatName.text = chatView.name
            chatView.callForLastSenderName { lastSender.text = "$it: " }
            lastMessage.text = chatView.lastMessage.text

            setOnClickListener {
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra(
                    EXTRA_CHATID,
                    chatView.id
                )

                context.startActivity(intent)
            }
        }
    }
}
