package yoloyoj.pub.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_chat.*
import yoloyoj.pub.R
import yoloyoj.pub.web.apiClient
import yoloyoj.pub.web.handlers.MessageSender

const val MY_USER_ID = 1

const val EXTRA_CHATID = "chatid"

class ChatActivity : AppCompatActivity() {

    private lateinit var viewModel: ChatViewModel
    private lateinit var messages: MessagesData

    private lateinit var messageSender: MessageSender

    private var chatid: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatid = intent.getIntExtra(EXTRA_CHATID, 0)

        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        viewModel.chatid = chatid

        messages = viewModel.messages
    }

    override fun onStart() {
        viewModel.messageGetter.start(
            chatid!!,
            0
        )

        messageSender = MessageSender(sendButton)

        messages.observeForever { loadAdapter() }

        messagesView.layoutManager = LinearLayoutManager(this)

        loadOnClicks()

        super.onStart()
    }

    private fun loadAdapter() {
        try {
            messagesView?.adapter = ChatAdapter(
                messages.value!!
            )
            messagesView.scrollToPosition(
                messagesView.adapter!!.itemCount - 1
            )
        } catch (e: Exception) {}
    }

    private fun loadOnClicks() {
        sendButton.setOnClickListener { sendMessage() }
    }

    private fun sendMessage() {
        apiClient.putMessage(
            messageView.text.toString(),
            MY_USER_ID,
            chatid!!
        )?.enqueue(messageSender)
        messageView.text.clear()
    }
}
