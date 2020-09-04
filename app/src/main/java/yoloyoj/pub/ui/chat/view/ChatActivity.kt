package yoloyoj.pub.ui.chat.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_chat.*
import yoloyoj.pub.MainActivity
import yoloyoj.pub.R
import yoloyoj.pub.models.Attachment
import yoloyoj.pub.models.User.Companion.ID_ANONYMOUS_USER
import yoloyoj.pub.storage.Storage
import yoloyoj.pub.ui.utils.attachment.preview.AttachmentPreviewAdapter
import yoloyoj.pub.web.utils.CODE_GET_PICTURE
import yoloyoj.pub.web.utils.chooseImage
import yoloyoj.pub.web.utils.putImage

const val EXTRA_CHATID = "chatid"

class ChatActivity : AppCompatActivity() {

    private lateinit var viewModel: ChatViewModel
    private lateinit var messages: MessagesData
    private lateinit var attachments: MutableLiveData<MutableList<Attachment>>

    private var chatid: String? = null
    private var userid: String = "0"

    private val currentMessage: String // TODO?: convert to Message
        get() = editMessage.text.toString()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null)
        when (requestCode) {
            CODE_GET_PICTURE -> putImage(data.data!!) { onImagePutted(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatid = intent.getStringExtra(EXTRA_CHATID)

        userid = getSharedPreferences(MainActivity.PREFERENCES_USER, Context.MODE_PRIVATE)
            .getString(MainActivity.PREFERENCES_USERID, "0")!!

        if (userid == ID_ANONYMOUS_USER) {
            //messageSendingLayout.visibility = View.GONE
            // return  // TODO: return returning
        }

        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        viewModel.chatid = chatid

        viewModel.messages.value = mutableListOf()
        messages = viewModel.messages
        attachments = viewModel.attachments
    }

    override fun onStart() {
        viewModel.startMessageObserving(chatid!!)

        messages.observeForever { loadAdapter() }

        messagesView.apply {
            layoutManager = LinearLayoutManager(context)
        }

        loadAttachmentPreview()

        loadWriteWatcher()

        loadOnClicks()

        super.onStart()
    }

    override fun onResume() {
        messages.value = emptyList()

        super.onResume()
    }

    private fun loadAttachmentPreview() {
        attachmentsPreView.adapter = AttachmentPreviewAdapter(
            attachmentsPreView,
            attachments
        )

        attachments.observeForever {
            (attachmentsPreView.adapter as AttachmentPreviewAdapter).apply {
                items = attachments
                notifyDataSetChanged()

                if (items.value!!.isEmpty()) {
                    attachmentsPreView.visibility = View.GONE
                    addAttachment.drawable.setTint(resources.getColor(R.color.colorAccent))
                } else if (attachmentsPreView.visibility == View.GONE) {
                    attachmentsPreView.visibility = View.VISIBLE
                    addAttachment.drawable.setTint(resources.getColor(R.color.colorAccent))
                }
            }
        }
    }

    private fun loadWriteWatcher() {
        editMessage.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s!!.isBlank())
                    sendButton.visibility = View.GONE
                else
                    sendButton.visibility = View.VISIBLE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })
    }

    private fun loadAdapter() {
        try {
            messagesView?.adapter = ChatAdapter(
                messages.value!!
            )
            messagesView.scrollToPosition(
                messagesView.adapter!!.itemCount - 1
            )
        } catch (e: Exception) {
        }
    }

    private fun loadOnClicks() {
        sendButton.setOnClickListener { sendMessage() }
    }

    private fun sendMessage() {
        Storage.sendMessage(
            currentMessage,
            userid,
            chatid!!,
            attachments.value!!
        ) {
            if (it)
                onImageSent()
            else
                Snackbar.make(sendButton, "Не удалось отправить сообщение...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun addAttachment(view: View) = chooseImage()

    private fun onImagePutted(link: String) {
        attachments.value = (attachments.value!! + Attachment(
            "image",
            link
        )).toMutableList()

        (attachmentsPreView.adapter as AttachmentPreviewAdapter).apply {
            items = attachments // temporary

            if (recyclerView.visibility == View.GONE)
                recyclerView.visibility = View.VISIBLE
        }
    }

    private fun onImageSent() {
        editMessage.text.clear()

        addAttachment.drawable.setTint(resources.getColor(R.color.colorAccent))

        attachments.value = mutableListOf()
    }
}
