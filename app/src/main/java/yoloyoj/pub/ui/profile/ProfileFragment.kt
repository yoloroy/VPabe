package yoloyoj.pub.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import yoloyoj.pub.R
import yoloyoj.pub.web.handlers.UserGetter

class ProfileFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val userId = arguments?.getInt("userId")
        recyclerUpcomingEvents.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerVisitedEvents.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        UserGetter { user ->
            Picasso.get().load(user.avatar).into(userImage)
            userName.text = user.username
            userStatus.text = user.status
        }.start(userId!!)
        recyclerUpcomingEvents.adapter = ProfileEventsAdapter(
            listOf()
        )
        recyclerVisitedEvents.adapter = ProfileEventsAdapter(
            listOf()
        )
        super.onViewCreated(view, savedInstanceState)
    }
}
