package yoloyoj.pub.ui.event.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_event_edit.*
import yoloyoj.pub.MainActivity
import yoloyoj.pub.R
import yoloyoj.pub.models.Date
import yoloyoj.pub.storage.Storage
import yoloyoj.pub.storage.Storage.Companion.users
import yoloyoj.pub.ui.enter.login.LoginActivity
import yoloyoj.pub.ui.utils.loacation.getter.LocationGetterActivity
import yoloyoj.pub.utils.dateToString
import yoloyoj.pub.utils.timeToString
import yoloyoj.pub.web.utils.CODE_GET_PICTURE
import yoloyoj.pub.web.utils.chooseImage
import yoloyoj.pub.web.utils.putImage
import java.util.*
import yoloyoj.pub.models.Event as FEvent

const val LOCATION_REQUEST_CODE = 127

class EventEditActivity: AppCompatActivity() {

    private var eventImageLink = STANDARD_EVENT_IMAGE
    private var eYear: Int = 0
    private var eMonth: Int = 0
    private var eDay: Int = 0
    private var eHour: Int = 0
    private var eMinute: Int = 0
    private var userId: String? = "0"
    private var eventId: String? = "0"
    private var ePlace: String? = ""
    private var eLat: Double? = 0.0
    private var eLng: Double? = 0.0

    private val event: FEvent
        get() = FEvent(
            author = users.document(userId!!),
            avatar = eventImageLink,
            name = event_header_edit.text.toString(),
            description = event_describe_header_edit.text.toString(),
            place = ePlace?:"",
            latlng = GeoPoint(eLat!!, eLng!!),
            date = Timestamp(
                Date(
                    eYear,
                    eMonth,
                    eDay,
                    eHour,
                    eMinute
                ).javaDate
            )
        )

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        when (requestCode) {
            CODE_GET_PICTURE -> putImage(data.data!!) { onImagePutted(it) }
            LOCATION_REQUEST_CODE -> {
                ePlace = data.getStringExtra("address")
                eLat = data.getDoubleExtra("lat", 0.0)
                eLng = data.getDoubleExtra("lng", 0.0)
                tvEventPlace.text = ePlace
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun addAttachment() = chooseImage()

    private fun onImagePutted(link: String) {
        Picasso.get().load(link).into(event_image)
        eventImageLink = link
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_edit)

        userId = getSharedPreferences(MainActivity.PREFERENCES_USER, Context.MODE_PRIVATE)
            ?.getString(MainActivity.PREFERENCES_USERID, "1")
        if (userId == null || userId == "0"){
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }

        eventId = intent?.getStringExtra("eventid")

        if (eventId != null && eventId != "0") {
            Storage.getEvent(eventId!!) {
                event_header_edit.setText(it.name)
                event_describe_header_edit.setText(it.description)
                with(it.javaDate?:java.util.Date()) {
                    eYear = year
                    eMonth = month
                    eDay = day
                    tvDate.text = dateToString(eDay, eMonth, eYear)
                    eHour = hours
                    eMinute = minutes
                    tvTime.text = timeToString(eHour, eMinute)
                }
                tvEventPlace.text = it.place
                if (it.avatar.isNullOrEmpty()) {
                    Picasso.get().load(STANDARD_EVENT_IMAGE).into(event_image)
                    eventImageLink =
                        STANDARD_EVENT_IMAGE
                } else {
                    Picasso.get().load(it.avatar).into(event_image)
                    eventImageLink = it.avatar
                }
                ePlace = it.place ?:""
                with(it.latlng!!) {
                    eLat = latitude
                    eLng = longitude
                }
                event_set_btn.text = getString(R.string.button_save_edit_profile)
                event_set_btn.setOnClickListener { updateEvent() }
                supportActionBar?.title = getString(R.string.title_edit_event)
            }
        } else {
            event_set_btn.setOnClickListener { sendEvent() }
            Picasso.get().load(STANDARD_EVENT_IMAGE).into(event_image)
            supportActionBar?.title = getString(R.string.title_create_event)
        }



        event_image.setOnClickListener { addAttachment() }

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(false)

        val c = Calendar.getInstance()

        pickDateBtn.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                eYear = year
                eMonth = monthOfYear + 1
                eDay = dayOfMonth
                tvDate.text = dateToString(eDay, eMonth, eYear)
            },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            )
            dpd.show()
        }

        pickTimeBtn.setOnClickListener {
            val tpd = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hour, minute ->
                eHour = hour
                eMinute = minute
                tvTime.text = timeToString(eHour, eMinute)
            },
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                true
            )
            tpd.show()
        }

        getLocationButton.setOnClickListener {
            val intent = Intent(this, LocationGetterActivity::class.java)
            startActivityForResult(intent,
                LOCATION_REQUEST_CODE
            )
        }
    }

    private fun sendEvent() {
        Storage.putEvent(event) {
            val intent = Intent(this, EventActivity::class.java)
            intent.putExtra("eventid", it)
            startActivity(intent)
            finish()
        }
    }

    private fun updateEvent() {
        Storage.updateEvent(eventId!!, event) {
            if (it) {
                val intent = Intent(this, EventActivity::class.java)
                intent.putExtra("eventid", eventId!!)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(applicationContext, "Ошибка при сохранении данных", Toast.LENGTH_LONG).show()
            }
        }
    }
}
