package yoloyoj.pub.ui.event

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_event_edit.*
import yoloyoj.pub.R
import yoloyoj.pub.web.apiClient
import yoloyoj.pub.web.handlers.EventSender
import java.util.*
const val MY_USER_ID = 0

class EventEditActivity: AppCompatActivity() {
    private lateinit var eventSender: EventSender
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_edit)
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
        val name : EditText = findViewById(R.id.event_header_edit)
        val place : EditText = findViewById(R.id.event_place_header_edit)
        val describe : EditText = findViewById(R.id.event_describe_header_edit)


        val mPickTimeBtn = findViewById<Button>(R.id.pickDateBtn)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val textView = findViewById<TextView>(R.id.text_date)

        mPickTimeBtn.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                textView.setText("Your date: $dayOfMonth.$month.$year")
            }, year, month, day)
            dpd.show()
        }

        apiClient.putEvent(
            name.text.toString(),
            describe.text.toString(),
            month.toString(),
            day.toString(),
            place.text.toString(),
            MY_USER_ID
        )?.enqueue(eventSender)
    }
}
