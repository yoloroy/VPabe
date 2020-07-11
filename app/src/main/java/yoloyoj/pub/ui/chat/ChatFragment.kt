package yoloyoj.pub.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_chat.*
import yoloyoj.pub.R
import yoloyoj.pub.web.apiClient
import yoloyoj.pub.web.handlers.MessageSender
import kotlin.Exception

const val MY_USER_ID = 1

class ChatFragment : Fragment() {

    private lateinit var viewModel: ChatViewModel
    private lateinit var messages: MessagesData

    private lateinit var messageSender: MessageSender

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)

        messages = viewModel.messages

        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onStart() {
        messageSender = MessageSender(view!!)

        messages.observeForever { loadAdapter() }
        viewModel.users.observeForever { loadAdapter() }

        loadOnClicks()

        super.onStart()
    }

    private fun loadAdapter() {
        try {
            messagesView?.adapter = ArrayAdapter<String>(
                context!!,
                android.R.layout.simple_list_item_1, viewModel.texts!!
            )
        } catch (e: Exception) {}
    }

    private fun loadOnClicks() {
        sendButton.setOnClickListener { sendMessage() }
    }

    private fun sendMessage() {
        apiClient.putMessage(messageView.text.toString(), MY_USER_ID)?.enqueue(messageSender)
        messageView.text.clear()
    }
}
