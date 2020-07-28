package yoloyoj.pub.ui.event.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_event.*
import yoloyoj.pub.MainActivity
import yoloyoj.pub.R
import yoloyoj.pub.ui.chat.view.ChatActivity
import yoloyoj.pub.ui.chat.view.EXTRA_CHATID
import yoloyoj.pub.ui.enter.login.LoginActivity
import yoloyoj.pub.utils.dateToString
import yoloyoj.pub.utils.timeToString
import yoloyoj.pub.web.apiClient
import yoloyoj.pub.web.handlers.*

const val STANDARD_EVENT_IMAGE = "https://static.tildacdn.com/tild3630-6536-4534-a235-346239306632/45-459030_download-s.png"

class EventActivity : AppCompatActivity() {

    private var eventId: Int? = 0
    private var userId: Int? = 0
    private var editMenu: Menu? = null
    private var chatId: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

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

        apiClient.getSingleEvent(
            eventId!!
        )?.enqueue(
            SingleEventGetter(
                applicationContext
            ) {
                if (it == null) {
                    Toast.makeText(applicationContext, "Ошибка при получении данных", Toast.LENGTH_LONG).show()
                    finish()
                    return@SingleEventGetter
                }
                event_name_header.text = it.name
                event_describe_header.text = it.description
                event_date_header.text = dateToString(
                        it.date?.day?:0,
                        it.date?.month?:0,
                        it.date?.year?:0
                    ) + " " + timeToString(
                        it.date?.hour?:0,
                        it.date?.minute?:0
                    ) // TODO: Replace with a resource string
                event_place_header.text = it.place
                if (it.avatar.isNullOrEmpty()) {
                    Picasso.get().load(STANDARD_EVENT_IMAGE).into(event_image)
                } else {
                    Picasso.get().load(it.avatar).into(event_image)
                }
                if (userId == it.authorid) {
                    editMenu?.setGroupVisible(0, true)
                }
                lateinit var userGetter: UserGetter

                userGetter = UserGetter { author ->
                    if (author == null) {
                        userGetter.start(it.authorid!!)
                        return@UserGetter
                    }

                    eventAuthorName.text = getString(R.string.event_author_name, author.username)
                }
                userGetter.start(it.authorid!!)

                apiClient.checkSubscribe(
                    eventid = eventId!!,
                    userid = userId!!
                )?.enqueue(
                    EventRegistrationChecker(applicationContext) { isUserSubscribedOnEvent ->
                        if (isUserSubscribedOnEvent == null) {
                            finish()
                            return@EventRegistrationChecker
                        }
                        if (!isUserSubscribedOnEvent) {
                            event_subscribe_button.isVisible = true
                            event_subscribe_button.setOnClickListener {
                                apiClient.subscribeOnEvent(
                                    eventid = eventId!!,
                                    userid = userId!!
                                )?.enqueue(
                                    EventSubscriber(applicationContext) {
                                        event_subscribe_button.isVisible = false
                                    }
                                )
                            }
                        } else {
                            // TODO: Add unsubscribe ability (server is already supported this action)
                        }
                    }
                )

                apiClient.getChatByEvent(
                    eventId!!
                )?.enqueue(
                    ChatGetter(applicationContext) { chatid ->
                        if (chatid == null) {
                            finish()
                            return@ChatGetter
                        }
                        chatId = chatid

                        apiClient.isUserInChat(
                            userid = userId!!,
                            chatid = chatId!!
                        )?.enqueue(
                            UserInChatChecker(applicationContext) { isUserInChat ->
                                if (isUserInChat == null) {
                                    finish()
                                    return@UserInChatChecker
                                }

                                if (isUserInChat) {
                                    event_chat_button.setOnClickListener {
                                        val intent = Intent(applicationContext, ChatActivity::class.java)
                                        intent.putExtra(EXTRA_CHATID, chatId!!)
                                        startActivity(intent)
                                    }
                                    event_chat_button.isVisible = true
                                } else {
                                    event_chat_button.setOnClickListener {
                                        apiClient.addUserToChat(
                                            chatid = chatId!!,
                                            userid = userId!!
                                        )?.enqueue(
                                            AddToChatSender(applicationContext) {
                                                event_chat_button.setOnClickListener {
                                                    val intent = Intent(applicationContext, ChatActivity::class.java)
                                                    intent.putExtra(EXTRA_CHATID, chatId!!)
                                                    startActivity(intent)
                                                }
                                                val intent = Intent(applicationContext, ChatActivity::class.java)
                                                intent.putExtra(EXTRA_CHATID, chatId!!)
                                                startActivity(intent)
                                            }
                                        )
                                    }
                                    event_chat_button.isVisible = true
                                }
                            }
                        )
                    }
                )
            }
        )
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







