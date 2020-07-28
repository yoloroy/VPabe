package yoloyoj.pub.storage

import yoloyoj.pub.models.Event
import yoloyoj.pub.models.User
import yoloyoj.pub.web.apiClient
import yoloyoj.pub.web.handlers.EventGetter
import yoloyoj.pub.web.handlers.UserGetter

typealias Handler<T> = (T) -> Unit

class Storage {
    companion object {
        const val USERS = "users"
        const val EVENTS = "events"

        fun getUser(
            phone: String = "",
            userid: Int = 0, // temporary
            handler: Handler<User>
        ) {
            var userGetter: UserGetter? = null
            userGetter = UserGetter {
                if (it == null) {
                    userGetter!!.start(telephone = phone, userid = userid)
                    return@UserGetter
                }

                handler(it)
            }

            userGetter.start(telephone = phone, userid = userid)
        }

        fun observeAllEvents(handler: Handler<List<Event>>) {
            var eventGetter: EventGetter? = null

            eventGetter = EventGetter { events ->
                handler(events)
                eventGetter!!.start(eventid = events.map { it.eventid!! }.maxBy { it }!!)
            }

            eventGetter.start()
        }

        fun getEventsBySearch(query: String, handler: Handler<List<Event>>) {
            apiClient.getSearchedEvents(query)!!.enqueue(EventGetter {
                handler(it)
            })
        }

        fun getEventsForUser(userid: Int, handler: Handler<List<Event>>) {
            EventGetter {
                handler(it)
            }.start(userid = userid)
        }
    }
}
