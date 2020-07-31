package yoloyoj.pub.storage

import yoloyoj.pub.models.ChatView
import yoloyoj.pub.models.Event
import yoloyoj.pub.models.User
import yoloyoj.pub.utils.tryDefault
import yoloyoj.pub.web.apiClient
import yoloyoj.pub.web.handlers.*

typealias Handler<T> = (T) -> Unit

class Storage { // TODO: divide?
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

        fun getEvent(eventid: Int, handler: Handler<Event>) {
            apiClient.getSingleEvent(
                eventid
            )?.enqueue(
                SingleEventGetter {
                    if (it == null) {
                        getEvent(eventid, handler)
                        return@SingleEventGetter
                    }

                    handler(it)
                }
            )
        }

        fun observeChatList(userid: Int, handler: Handler<List<ChatView>>) {
            var chatsCount: Int
            var lastMessageSum = 0

            var chatListGetter: ChatListGetter? = null
            chatListGetter = ChatListGetter(userid) { chats ->
                handler(chats)

                chatsCount = chats.count()
                lastMessageSum = tryDefault(lastMessageSum) { chats.sumBy { it.lastMessage!!._rowid_!! } }

                chatListGetter!!.start(chatsCount, lastMessageSum)
            }
            chatListGetter.start()
        }

        fun getChatId(eventid: Int, handler: Handler<Int>) { // TODO: refactor to returning objects
            var chatGetter: ChatGetter? = null

            chatGetter = ChatGetter {
                if (it == null)
                    apiClient
                        .getChatByEvent(eventid)
                        ?.enqueue(chatGetter!!)
                else
                    handler(it)
            }

            apiClient
                .getChatByEvent(eventid)
                ?.enqueue(chatGetter)
        }

        fun checkIsUserInChat(userid: Int, chatid: Int, handler: Handler<Boolean>) {
            apiClient.isUserInChat(
                userid = userid,
                chatid = chatid
            )?.enqueue(
                UserInChatChecker{ it?.let { it1 -> handler(it1) } }
            )
        }

        fun checkIsUserSubscribed(userid: Int, eventid: Int, handler: Handler<Boolean>) {
            apiClient.checkSubscribe(
                eventid = eventid,
                userid = userid
            )?.enqueue(
                EventRegistrationChecker {
                    if (it == null) {
                        checkIsUserSubscribed(userid, eventid, handler)
                        return@EventRegistrationChecker
                    }

                    handler(it)
                }
            )
        }

        fun addUserToChat(userid: Int, chatid: Int, handler: Handler<Unit>) {
            apiClient.addUserToChat(
                chatid = chatid,
                userid = userid
            )?.enqueue(
                AddToChatSender { isAdded ->
                    if (isAdded) handler(Unit)
                    // TODO: add error handler?
                }
            )
        }

        fun subscribe(userid: Int, eventid: Int, handler: Handler<Unit>) {
            apiClient.subscribeOnEvent(
                eventid = eventid,
                userid = userid
            )?.enqueue(EventSubscriber { handler(Unit) })
        }
    }
}
