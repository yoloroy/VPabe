package yoloyoj.pub.ui.event.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_event_edit.*
import yoloyoj.pub.MainActivity
import yoloyoj.pub.R
import yoloyoj.pub.ui.chat.view.CODE_GET_PICTURE
import yoloyoj.pub.ui.enter.login.LoginActivity
import yoloyoj.pub.ui.utils.loacation.getter.LocationGetterActivity
import yoloyoj.pub.utils.dateToString
import yoloyoj.pub.utils.timeToString
import yoloyoj.pub.web.apiClient
import yoloyoj.pub.web.handlers.EventSender
import yoloyoj.pub.web.handlers.EventUpdater
import yoloyoj.pub.web.handlers.SingleEventGetter
import java.io.File
import java.util.Calendar

const val LOCATION_REQUEST_CODE = 127

class EventEditActivity : AppCompatActivity() {

    private var eventImageLink = STANDARD_EVENT_IMAGE
    private var eYear: Int = 0
    private var eMonth: Int = 0
    private var eDay: Int = 0
    private var eHour: Int = 0
    private var eMinute: Int = 0
    private var userId: Int? = 0
    private var eventId: Int? = 0
    private var ePlace: String? = ""
    private var eLat: Double? = 0.0
    private var eLng: Double? = 0.0

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        when (requestCode) {
            CODE_GET_PICTURE -> putImage(data.data!!)
            LOCATION_REQUEST_CODE -> {
                ePlace = data.getStringExtra("address")
                eLat = data.getDoubleExtra("lat", 0.0)
                eLng = data.getDoubleExtra("lng", 0.0)
                tvEventPlace.text = ePlace
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun addAttachment() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }

        startActivityForResult(
            Intent.createChooser(intent, "Select picture"),
            CODE_GET_PICTURE
        )
    }

    private fun putImage(uri: Uri) {
        val file = File(uri.path)

        val storage = FirebaseStorage.getInstance()
        val storageReference = storage
            .getReferenceFromUrl("gs://vpabe-75c05.appspot.com") // TODO: remove hardcode
            .child("${file.hashCode()}.${uri.path!!.split(".").last()}")

        storageReference.putFile(uri)
        storageReference.downloadUrl.addOnSuccessListener {
            onImagePutted(it.toString())
        }
    }

    private fun onImagePutted(link: String) {
        Picasso.get().load(link).into(event_image)
        eventImageLink = link
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_edit)

        userId = getSharedPreferences(MainActivity.PREFERENCES_USER, Context.MODE_PRIVATE)
            ?.getInt(MainActivity.PREFERENCES_USERID, 1)
        if (userId == null || userId == 0) {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }

        eventId = intent?.getIntExtra("eventid", 0)

        if (eventId != null && eventId != 0) {
            apiClient.getSingleEvent(
                eventId!!
            )?.enqueue(
                SingleEventGetter(
                    this
                ) {
                    if (it == null) {
                        Toast.makeText(applicationContext, "Ошибка при получении данных", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    event_header_edit.setText(it?.name)
                    event_describe_header_edit.setText(it?.description)
                    eYear = it?.date?.year ?: 0
                    eMonth = it?.date?.month ?: 0
                    eDay = it?.date?.day ?: 0
                    tvDate.text = dateToString(eDay, eMonth, eYear)
                    eHour = it?.date?.hour ?: 0
                    eMinute = it?.date?.minute ?: 0
                    tvTime.text = timeToString(eHour, eMinute)
                    tvEventPlace.text = it?.place
                    if (it?.avatar.isNullOrEmpty()) {
                        Picasso.get().load(STANDARD_EVENT_IMAGE).into(event_image)
                        eventImageLink =
                            STANDARD_EVENT_IMAGE
                    } else {
                        Picasso.get().load(it?.avatar).into(event_image)
                        eventImageLink = it?.avatar!!
                    }
                    ePlace = it?.place ?: ""
                    eLat = it?.lat ?: 0.0
                    eLng = it?.lng ?: 0.0
                    event_set_btn.text = getString(R.string.button_save_edit_profile)
                    event_set_btn.setOnClickListener { updateEvent() }
                    supportActionBar?.title = getString(R.string.title_edit_event)
                }
            )
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
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
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
            val tpd = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { view, hour, minute ->
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
        apiClient.putEvent(
            name = event_header_edit.text.toString(),
            description = event_describe_header_edit.text.toString(),
            year = eYear,
            month = eMonth,
            day = eDay,
            hour = eHour,
            minute = eMinute,
            place = ePlace ?: "",
            lat = eLat!!,
            lng = eLng!!,
            authorid = userId!!,
            avatar = eventImageLink
        )?.enqueue(
            EventSender(applicationContext) {
                val intent = Intent(this, EventActivity::class.java)
                intent.putExtra("eventid", it)
                startActivity(intent)
                finish()
            }
        )
    }

    private fun updateEvent() {
        apiClient.updateEvent(
            eventid = eventId!!,
            name = event_header_edit.text.toString(),
            description = event_describe_header_edit.text.toString(),
            year = eYear,
            month = eMonth,
            day = eDay,
            hour = eHour,
            minute = eMinute,
            place = ePlace ?: "",
            lat = eLat!!,
            lng = eLng!!,
            avatar = eventImageLink
        )?.enqueue(
            EventUpdater(applicationContext) {
                val intent = Intent(this, EventActivity::class.java)
                intent.putExtra("eventid", eventId!!)
                startActivity(intent)
                finish()
            }
        )
    }
}
