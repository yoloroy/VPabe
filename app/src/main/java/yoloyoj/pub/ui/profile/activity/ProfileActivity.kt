package yoloyoj.pub.ui.profile.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.soywiz.klock.DateTime
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USER
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USERID
import yoloyoj.pub.R
import yoloyoj.pub.storage.Storage
import yoloyoj.pub.ui.enter.login.LoginActivity
import yoloyoj.pub.ui.event.view.STANDARD_EVENT_IMAGE
import yoloyoj.pub.ui.profile.fragment.STANDARD_PROFILE_IMAGE

class ProfileActivity: AppCompatActivity() {

    private var selfUserId: String? = "0" // me
    private var profileUserId: String = "0" // other user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.title = getString(R.string.title_profile)
        recyclerUpcomingEventsActivity.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        recyclerVisitedEventsActivity.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)

        selfUserId = getSharedPreferences(PREFERENCES_USER, Context.MODE_PRIVATE)
            ?.getString(PREFERENCES_USERID, "1")
        if (selfUserId == null || selfUserId == "0"){
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
            return
        }

        profileUserId = intent.getStringExtra("userid")
        if (profileUserId == "0") {
            finish()
            return
        }

        Storage.getUser(userid = profileUserId) { user ->
            user!!
            if (user.avatar.isNullOrEmpty()) {
                Picasso.get().load(STANDARD_PROFILE_IMAGE).into(userImageActivity)
            } else {
                Picasso.get().load(user.avatar).into(userImageActivity)
            }
            userNameActivity.text = user.name
            userStatusActivity.text = user.status
        }

        Storage.getEventsForUser(profileUserId) { events ->
            val upcomingEvents = emptyList<ProfileEventItemActivity>().toMutableList()
            val visitedEvents = emptyList<ProfileEventItemActivity>().toMutableList()
            val curDate = DateTime.now().unixMillisLong
            for (e in events){
                val eventDate = e.date!!.seconds/1000
                var imageLink = STANDARD_EVENT_IMAGE
                if (!e.avatar.isNullOrEmpty()) {
                    imageLink = e.avatar
                }

                (if (eventDate >= curDate)
                    upcomingEvents
                else
                    visitedEvents)
                .add(
                    ProfileEventItemActivity(
                        eventName = e.name!!,
                        eventId = e.id,
                        eventImageLink = imageLink
                    )
                )
            }
            recyclerUpcomingEventsActivity.adapter = ProfileEventsAdapterActivity(
                upcomingEvents.reversed()
            )
            recyclerVisitedEventsActivity.adapter = ProfileEventsAdapterActivity(
                visitedEvents.reversed()
            )
        }
    }
}