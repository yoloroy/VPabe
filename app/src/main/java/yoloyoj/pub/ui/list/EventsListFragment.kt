package yoloyoj.pub.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_events_list.*
import yoloyoj.pub.R

class EventsListFragment : Fragment() {

    private lateinit var eventsListViewModel: EventsListViewModel
    private lateinit var events: EventData

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val root = inflater.inflate(R.layout.activity_events_list, container, false)
            eventsListViewModel = ViewModelProviders.of(this).get(EventsListViewModel::class.java)
            events = eventsListViewModel.events
            return root
        }

    override fun onStart() {
        events_container.layoutManager = LinearLayoutManager(context)
        events.observeForever { loadAdapter() }
        super.onStart()
    }

    private fun loadAdapter() {
        try {
            events_container?.adapter = EventListAdapter(
                events.value!!
            )

            events_container.scrollToPosition(
                events_container.adapter!!.itemCount - 1
            )

        } catch (e: Exception) {}
    }

}
