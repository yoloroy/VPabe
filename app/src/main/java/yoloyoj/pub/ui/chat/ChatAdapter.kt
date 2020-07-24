package yoloyoj.pub.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import yoloyoj.pub.R
import yoloyoj.pub.models.Message
import yoloyoj.pub.utils.tryDefault

class ChatAdapter(
    private val items: List<Message>
) : RecyclerView.Adapter<MessageItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageItemHolder {
        val inflater = LayoutInflater.from(parent.context)

        return MessageItemHolder(
            inflater.inflate(
                R.layout.item_message,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MessageItemHolder, position: Int) {
        holder.bind(
            items[position],
            tryDefault(true) {
                items[position - 1].sender != items[position].sender
            },
            tryDefault(true) {
                items[position].sender != items[position + 1].sender
            }
        )
    }

    override fun getItemCount(): Int = items.size
}
