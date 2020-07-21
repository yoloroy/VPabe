package yoloyoj.pub.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.soywiz.klock.DateTime
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_event.*
import kotlinx.android.synthetic.main.fragment_profile.*
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USER
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USERID
import yoloyoj.pub.R
import yoloyoj.pub.ui.event.STADNARD_EVENT_IMAGE
import yoloyoj.pub.ui.login.LoginActivity
import yoloyoj.pub.web.handlers.EventGetter
import yoloyoj.pub.web.handlers.UserGetter

const val STANDARD_PROFILE_IMAGE = "https://alpinism-industrial.ru/wp-content/uploads/2019/09/kisspng-user-profile-computer-icons-clip-art-profile-5ac092f6f2d337.1560498715225699749946-300x300.jpg"

class ProfileFragment : Fragment() {

    private var menuItem: MenuItem? = null
    private var isOtherUser = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.profile_edit_menu, menu)

        menu.getItem(0).setOnMenuItemClickListener {
            findNavController().navigate(R.id.editProfileFragment)
            true
        }

        menuItem = menu.getItem(0)
        menuItem!!.isVisible = false

        menu.getItem(1).setOnMenuItemClickListener {
            activity!!.getSharedPreferences(PREFERENCES_USER, Context.MODE_PRIVATE)
                .edit().apply {
                    putInt(PREFERENCES_USERID, 0)
                    apply()
                }

            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
            true
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_profile, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerUpcomingEvents.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerVisitedEvents.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        var userId = activity
            ?.getSharedPreferences(PREFERENCES_USER, Context.MODE_PRIVATE)
            ?.getInt(PREFERENCES_USERID, 1)
        if (userId == null || userId == 0){
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }

        if (arguments?.getInt("userId") != null){
            userId = arguments!!.getInt("userId")
            isOtherUser = true
        }

        lateinit var userGetter: UserGetter

        userGetter = UserGetter (activity!!.applicationContext) {user ->
            if (user == null){
                userGetter.start(userId!!)
                return@UserGetter
            }

            if (user.avatar.isNullOrEmpty()) {
                Picasso.get().load(STANDARD_PROFILE_IMAGE).into(userImage)
            } else {
                Picasso.get().load(user.avatar).into(userImage)
            }
            userName.text = user.username
            userStatus.text = user.status
            if (!isOtherUser) {
                menuItem!!.isVisible = true
            }
        }

        userGetter.start(userId!!)
        
        EventGetter { events ->
            val upcomingEvents = emptyList<ProfileEventItem>().toMutableList()
            val visitedEvents = emptyList<ProfileEventItem>().toMutableList()
            val curDate = DateTime.now().unixMillisLong
            for (e in events){
                val eventDate = DateTime.createAdjusted(
                    e.date!!.year!!,
                    e.date!!.month!!,
                    e.date!!.day!!,
                    e.date!!.hour!!,
                    e.date!!.minute!!
                ).unixMillisLong
                var imageLink = STADNARD_EVENT_IMAGE
                if (!e.avatar.isNullOrEmpty()) {
                    imageLink = e.avatar!!
                }
                if (eventDate >= curDate){
                    upcomingEvents.add(ProfileEventItem(eventName = e.name!!, eventId = e.eventid!!, eventImageLink = imageLink))
                } else {
                    visitedEvents.add(ProfileEventItem(eventName = e.name!!, eventId = e.eventid!!, eventImageLink = imageLink))
                }
            }
            recyclerUpcomingEvents.adapter = ProfileEventsAdapter(
                upcomingEvents
            )
            recyclerVisitedEvents.adapter = ProfileEventsAdapter(
                visitedEvents
            )
        }.start(userid = userId)
        super.onViewCreated(view, savedInstanceState)
    }
}
