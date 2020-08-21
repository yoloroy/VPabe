package yoloyoj.pub.ui.profile.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USER
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USERID
import yoloyoj.pub.R
import yoloyoj.pub.models.Event
import yoloyoj.pub.storage.Storage
import yoloyoj.pub.ui.enter.login.LoginActivity
import yoloyoj.pub.ui.event.view.STANDARD_EVENT_IMAGE
import java.util.*

const val STANDARD_PROFILE_IMAGE = "https://alpinism-industrial.ru/wp-content/uploads/2019/09/kisspng-user-profile-computer-icons-clip-art-profile-5ac092f6f2d337.1560498715225699749946-300x300.jpg"

class ProfileFragment : Fragment() {

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

        val userId = activity
            ?.getSharedPreferences(PREFERENCES_USER, Context.MODE_PRIVATE)
            ?.getString(PREFERENCES_USERID, "1")
        if (userId == null || userId == "0")
            goLogin()

        userId!!
        Storage.getUser(userid = userId) { user ->
            if (user == null) {
                goLogin()
                return@getUser
            }

            if (user.avatar.isNullOrEmpty()) {
                Picasso.get().load(STANDARD_PROFILE_IMAGE).into(userImage)
            } else {
                Picasso.get().load(user.avatar).into(userImage)
            }
            userName.text = user.name
            userStatus.text = user.status
        }
        loadEvents(userId)

        super.onViewCreated(view, savedInstanceState)
    }

    private fun loadEvents(userId: String) {
        Storage.getEventsForUser(userId) { events ->
            val (upcomingEvents, visitedEvents) = events.asReversed()
                .partition { it.date!!.toDate().after(Date()) }.toList()
                .map { part ->
                    part.map {
                        ProfileEventItem(
                            eventName = it.name!!,
                            eventId = it.id,
                            eventImageLink = fixImageLink(it.avatar)
                        )
                    }
                }
                .run { get(0) to get(1) }

            userExp.text = "${visitedEvents.size * 10} exp."

            if (upcomingEvents.isNotEmpty())
                recyclerUpcomingEvents.adapter = ProfileEventsAdapter(upcomingEvents)
            else {
                cardUpcomingEvents.visibility = View.GONE
            }

            if (visitedEvents.isNotEmpty())
                recyclerVisitedEvents.adapter = ProfileEventsAdapter(visitedEvents)
            else {
                cardVisitedEvents.visibility = View.GONE
            }
        }
    }

    private fun fixImageLink(imageLink: String?): String {
        return if (imageLink.isNullOrEmpty())
            STANDARD_EVENT_IMAGE
        else
            imageLink
    }

    val Event.millisDate: Long
        get() = date!!.seconds/1000

    private fun goLogin() {
        startActivity(Intent(context, LoginActivity::class.java))
        activity?.finish()
    }
}
