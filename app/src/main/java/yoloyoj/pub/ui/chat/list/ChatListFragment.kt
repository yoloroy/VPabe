package yoloyoj.pub.ui.chat.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_chat_list.*
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USER
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USERID
import yoloyoj.pub.R
import yoloyoj.pub.models.Event

class ChatListFragment : Fragment() {
    private lateinit var viewModel: ChatListViewModel
    private lateinit var chats: LiveData<List<Event>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(ChatListViewModel::class.java)

        chats = viewModel.chats
        viewModel.start(
            activity!!.getSharedPreferences(PREFERENCES_USER, Context.MODE_PRIVATE)
                .getString(PREFERENCES_USERID, "0")!!
        )

        return inflater.inflate(R.layout.fragment_chat_list, container, false)
    }

    override fun onStart() {
        chats.observeForever { loadAdapter() }

        chatList.layoutManager = LinearLayoutManager(context)

        super.onStart()
    }

    private fun loadAdapter() {
        chatList?.adapter =
            ChatListAdapter(chats.value!!)
    }
}
