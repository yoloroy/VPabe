package yoloyoj.pub.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_dashboard.*
import yoloyoj.pub.R
import yoloyoj.pub.web.apiClient
import yoloyoj.pub.web.handlers.MessageSender

class DashboardFragment : Fragment() {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var messages: LiveData<MutableList<String>>

    private lateinit var messageSender: MessageSender

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)

        messages = viewModel.messages

        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onStart() {
        messageSender = MessageSender(view!!)

        messages.observeForever {
            messagesView.adapter = ArrayAdapter<String>(context!!,
                android.R.layout.simple_list_item_1, viewModel.messages.value!!)
        }

        loadOnClicks()

        super.onStart()
    }

    private fun loadOnClicks() {
        sendButton.setOnClickListener { sendMessage() }
    }

    private fun sendMessage() {
        apiClient.putMessage(messageView.text.toString())?.enqueue(messageSender)
        messageView.text.clear()
    }
}
