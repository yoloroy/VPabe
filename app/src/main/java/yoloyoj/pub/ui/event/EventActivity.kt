package yoloyoj.pub.ui.event

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_event.*
import yoloyoj.pub.MainActivity
import yoloyoj.pub.R
import yoloyoj.pub.ui.login.LoginActivity
import yoloyoj.pub.web.apiClient
import yoloyoj.pub.web.handlers.SingleEventGetter

class EventActivity : AppCompatActivity() {

    private var eventId: Int? = 0
    private var userId: Int? = 0
    private var editMenu: Menu? = null

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
                }
                event_name_header.text = it?.name
                event_describe_header.text = it?.description
                event_date_header.text = "${it?.date?.day}.${it?.date?.month} ${it?.date?.hour}:${it?.date?.minute}"
                event_place_header.text = it?.place
                // Picasso.get().load(it.photo).into(event_image)

                if (userId == it?.authorid) {
                    editMenu?.setGroupVisible(0, true)
                }
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







