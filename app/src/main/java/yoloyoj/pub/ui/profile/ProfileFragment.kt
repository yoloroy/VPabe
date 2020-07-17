package yoloyoj.pub.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.soywiz.klock.DateTime
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import yoloyoj.pub.MainActivity
import yoloyoj.pub.R
import yoloyoj.pub.models.Event
import yoloyoj.pub.web.handlers.EventGetter
import yoloyoj.pub.web.handlers.UserGetter


class ProfileFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_profile, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerUpcomingEvents.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerVisitedEvents.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        var userId = (context as MainActivity).getUserId()
        if (arguments?.getInt("userId") != null){
            userId = arguments!!.getInt("userId")
        }

        UserGetter { user ->
            Picasso.get().load(user.avatar).into(userImage)
            userName.text = user.username
            userStatus.text = user.status
        }.start(userId!!)

        EventGetter { events ->
            val upcomingEvents = emptyList<Event>().toMutableList()
            val visitedEvents = emptyList<Event>().toMutableList()
            val curDate = DateTime.now().unixMillisLong
            for (e in events){
                val eventDate = DateTime.createAdjusted(
                    2020,
                    e.date!!.month!!,
                    e.date!!.day!!,
                    e.date!!.hour!!,
                    e.date!!.minute!!
                ).unixMillisLong
                if (eventDate >= curDate){
                    upcomingEvents.add(e)
                } else {
                    visitedEvents.add(e)
                }
            }
            recyclerUpcomingEvents.adapter = ProfileEventsAdapter(
                upcomingEvents
            )
            recyclerVisitedEvents.adapter = ProfileEventsAdapter(
                visitedEvents
            )
        }.start(userId)
        super.onViewCreated(view, savedInstanceState)
    }
}
