package yoloyoj.pub.ui.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_events_list.*
import yoloyoj.pub.R
import yoloyoj.pub.models.Event
import yoloyoj.pub.web.apiClient
import yoloyoj.pub.web.handlers.EventGetter

class EventsListFragment : Fragment() {

    private lateinit var eventsListViewModel: EventsListViewModel
    private lateinit var events: EventData

    var search = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.events_list_menu, menu)

        val searchItem = menu.findItem(R.id.events_list_search)

        val searchView = MenuItemCompat.getActionView(searchItem) as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null)
                    apiClient.getSearchedEvents(query)!!.enqueue(EventGetter {
                        search = true
                        loadAdapter(it)
                    })

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == null) return false

                if (newText.isEmpty()) {
                    search = false
                    loadAdapter(events.value!!)

                    return true
                }

                return false
            }
        })

        searchView.setOnCloseListener {
            search = false

            true
        }

        super.onCreateOptionsMenu(menu, inflater)
    }
  
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fab.setOnClickListener {
            startActivity(Intent(activity!!.applicationContext, EventEditActivity::class.java))
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        events_container.layoutManager = LinearLayoutManager(context)

        events.observeForever { if (!search) loadAdapter(events.value!!) }

        super.onStart()
    }

    private fun loadAdapter(events: List<Event>) {
        try {
            events_container?.adapter = EventListAdapter(
                events
            )
        } catch (e: Exception) {}
    }

}
