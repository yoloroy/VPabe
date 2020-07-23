package yoloyoj.pub.ui.profile.asactivity

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
import yoloyoj.pub.ui.event.STANDARD_EVENT_IMAGE
import yoloyoj.pub.ui.login.LoginActivity
import yoloyoj.pub.ui.profile.STANDARD_PROFILE_IMAGE
import yoloyoj.pub.web.handlers.EventGetter
import yoloyoj.pub.web.handlers.UserGetter

class ProfileActivity: AppCompatActivity() {

    private var selfUserId: Int? = 0 // me
    private var profileUserId: Int = 0 // other user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.title = getString(R.string.title_profile)
        recyclerUpcomingEventsActivity.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        recyclerVisitedEventsActivity.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)

        selfUserId = getSharedPreferences(PREFERENCES_USER, Context.MODE_PRIVATE)
            ?.getInt(PREFERENCES_USERID, 1)
        if (selfUserId == null || selfUserId == 0){
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
            return
        }

        profileUserId = intent.getIntExtra("userid", 0)
        if (profileUserId == 0) {
            finish()
            return
        }

        lateinit var userGetter: UserGetter

        userGetter = UserGetter (applicationContext) {user ->
            if (user == null){
                userGetter.start(profileUserId)
                return@UserGetter
            }

            if (user.avatar.isNullOrEmpty()) {
                Picasso.get().load(STANDARD_PROFILE_IMAGE).into(userImageActivity)
            } else {
                Picasso.get().load(user.avatar).into(userImageActivity)
            }
            userNameActivity.text = user.username
            userStatusActivity.text = user.status
        }

        userGetter.start(profileUserId)

        EventGetter { events ->
            val upcomingEvents = emptyList<ProfileEventItemActivity>().toMutableList()
            val visitedEvents = emptyList<ProfileEventItemActivity>().toMutableList()
            val curDate = DateTime.now().unixMillisLong
            for (e in events){
                val eventDate = DateTime.createAdjusted(
                    e.date!!.year!!,
                    e.date!!.month!!,
                    e.date!!.day!!,
                    e.date!!.hour!!,
                    e.date!!.minute!!
                ).unixMillisLong
                var imageLink = STANDARD_EVENT_IMAGE
                if (!e.avatar.isNullOrEmpty()) {
                    imageLink = e.avatar!!
                }
                if (eventDate >= curDate){
                    upcomingEvents.add(ProfileEventItemActivity(eventName = e.name!!, eventId = e.eventid!!, eventImageLink = imageLink))
                } else {
                    visitedEvents.add(ProfileEventItemActivity(eventName = e.name!!, eventId = e.eventid!!, eventImageLink = imageLink))
                }
            }
            recyclerUpcomingEventsActivity.adapter = ProfileEventsAdapterActivity(
                upcomingEvents.reversed()
            )
            recyclerVisitedEventsActivity.adapter = ProfileEventsAdapterActivity(
                visitedEvents.reversed()
            )
        }.start(userid = profileUserId)
    }
}