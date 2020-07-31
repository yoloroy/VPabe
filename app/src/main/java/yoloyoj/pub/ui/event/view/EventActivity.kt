package yoloyoj.pub.ui.event.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_event.*
import yoloyoj.pub.MainActivity
import yoloyoj.pub.R
import yoloyoj.pub.models.Event
import yoloyoj.pub.storage.Storage
import yoloyoj.pub.ui.chat.view.ChatActivity
import yoloyoj.pub.ui.chat.view.EXTRA_CHATID
import yoloyoj.pub.ui.enter.login.LoginActivity

const val STANDARD_EVENT_IMAGE = "https://static.tildacdn.com/tild3630-6536-4534-a235-346239306632/45-459030_download-s.png"

class EventActivity : AppCompatActivity() {

    private var eventId: Int? = 0
    private var userId: Int? = 0
    private var editMenu: Menu? = null
    private var chatId: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        loadOther()
        loadView()
    }

    private fun loadOther() {
        userId = getSharedPreferences(MainActivity.PREFERENCES_USER, Context.MODE_PRIVATE)
            ?.getInt(MainActivity.PREFERENCES_USERID, 1)

        if (userId == null || userId == 0){
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(false)
        event_subscribe_button.isVisible = false
        event_chat_button.isVisible = false

        eventId = intent?.getIntExtra("eventid", 0)
        if (eventId == null || eventId == 0) {
            finish()
        }
    }

    private fun loadView() {
        Storage.getEvent(eventId!!) {
            loadInfo(it)
            loadSubscribe()
            loadChat()
        }
    }

    private fun loadInfo(event: Event) {
        event_name_header.text = event.name
        event_describe_header.text = event.description
        event_date_header.text = event.date.toString()
        event_place_header.text = event.place

        if (event.avatar.isNullOrEmpty()) {
            Picasso.get().load(STANDARD_EVENT_IMAGE).into(event_image)
        } else {
            Picasso.get().load(event.avatar).into(event_image)
        }

        if (userId == event.authorid) {
            editMenu?.setGroupVisible(0, true)
        }

        Storage.getUser(userid = event.authorid!!) { author ->
            eventAuthorName.text = getString(R.string.event_author_name, author.username)
        }
    }

    private fun loadSubscribe() {
        Storage.checkIsUserSubscribed(
            userId!!, eventId!!
        ) { isUserSubscribedOnEvent ->
            if (!isUserSubscribedOnEvent) {
                event_subscribe_button.isVisible = true
                event_subscribe_button.setOnClickListener {
                    Storage.subscribe(
                        userId!!, eventId!!
                    ) { event_subscribe_button.isVisible = false }
                }
            } // TODO: Add unsubscribe ability (server is already supported this action)
        }
    }

    private fun loadChat() {
        Storage.getChatId(eventId!!) { chatid ->
            chatId = chatid

            Storage.checkIsUserInChat(userId!!, chatid) { isUserInChat ->
                if (isUserInChat)
                    event_chat_button.setOnClickListener { goToChat(chatid) }
                else
                    event_chat_button.setOnClickListener {
                        Storage.addUserToChat(userId!!, chatid) {
                            event_chat_button.setOnClickListener { goToChat(chatid) }
                            goToChat(chatid)
                        }
                    }
                event_chat_button.isVisible = true
            }
        }
    }

    private fun goToChat(chatId: Int) {
        val intent = Intent(applicationContext, ChatActivity::class.java)
        intent.putExtra(EXTRA_CHATID, chatId)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.event_edit_menu, menu)
        editMenu = menu
        editMenu?.setGroupVisible(0, false)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.editEventItem -> {
                val intent = Intent(this, EventEditActivity::class.java)
                intent.putExtra("eventid", eventId)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}







