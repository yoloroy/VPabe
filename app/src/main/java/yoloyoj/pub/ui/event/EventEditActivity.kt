package yoloyoj.pub.ui.event

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_event_edit.*
import yoloyoj.pub.MainActivity
import yoloyoj.pub.R
import yoloyoj.pub.ui.login.LoginActivity
import yoloyoj.pub.web.apiClient
import yoloyoj.pub.web.handlers.EventSender
import java.util.*

class EventEditActivity: AppCompatActivity() {
    private lateinit var eventSender: EventSender

    private var eMonth: Int = 0
    private var eDay: Int = 0
    private var eHour: Int = 0
    private var eMinute: Int = 0
    private var userId: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_edit)

        userId = getSharedPreferences(MainActivity.PREFERENCES_USER, Context.MODE_PRIVATE)
            ?.getInt(MainActivity.PREFERENCES_USERID, 1)
        if (userId == null || userId == 0){
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }

        val c = Calendar.getInstance()

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(false)

        pickDateBtn.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                tvDate.text = "$dayOfMonth.$monthOfYear.$year"
                eMonth = monthOfYear
                eDay = dayOfMonth
            },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            )
            dpd.show()
        }

        pickTimeBtn.setOnClickListener {
            val tpd = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hour, minute ->
                tvTime.text = "$hour:$minute"
                eHour = hour
                eMinute = minute
            },
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                true
            )
            tpd.show()
        }
    }

    override fun onStart() {
        eventSender = EventSender(event_set_btn)
        loadOnClicks()
        super.onStart()
    }


    private fun loadOnClicks() {
        event_set_btn.setOnClickListener { sendEvent() }
    }

    private fun sendEvent() {
       apiClient.putEvent(
           event_describe_header_edit.text.toString(),
           event_header_edit.text.toString(),
           eMonth,
           eDay,
           eHour,
           eMinute,
           event_place_header_edit.text.toString(),
           userId!!
        )?.enqueue(eventSender)
    }
}
