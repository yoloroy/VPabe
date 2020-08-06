package yoloyoj.pub.storage

import yoloyoj.pub.models.*
import yoloyoj.pub.utils.tryDefault
import yoloyoj.pub.web.apiClient
import yoloyoj.pub.web.handlers.*

typealias Handler<T> = (T) -> Unit

class Storage { // TODO: divide?
    companion object {
        const val USERS = "users"
        const val EVENTS = "events"

        // region user
        fun getUser(
            phone: String = "",
            userid: String = "0", // temporary
            handler: Handler<User>
        ) {
            if ((userid != "0") or (phone != "")) {
                var temp = phone
                if (temp.startsWith('+')) {
                    temp = "${temp[1].toString().toInt()+1}${temp.slice(2 until temp.length)}"
                }

                apiClient.getUser(userid, temp).enqueue(UserGetter {
                    if (it == null) {
                        getUser(temp, userid, handler)
                        return@UserGetter
                    }

                    handler(it)
                })
            }
        }

        fun regUser(
            name: String,
            phone: String,
            avatar: String,
            handler: Handler<Pair<Boolean?, String?>>
        ) {
            var temp = phone
            if (temp.startsWith('+')) {
                temp = "${temp[1].toString().toInt()+1}${temp.slice(2 until temp.length)}"
            }

            apiClient.regUser(name, temp, avatar).enqueue(UserSender {
                regResult, userid -> handler(regResult to userid)
            })
        }

        fun updateUser(
            userid: String,
            name: String,
            status: String,
            avatarLink: String,
            handler: Handler<Boolean>
        ) {
            apiClient.updateUser(
                userid,
                name,
                status,
                avatarLink
            )?.enqueue(
                UserUpdater(handler)
            )
        }
        // endregion

        // region events
        fun observeAllEvents(handler: Handler<List<Event>>, eventid: String = "0") {
            apiClient.getEvents(
                ALL_EVENTS, eventid
            )?.enqueue(EventGetter { events ->
                handler(events)

                // recursion repeating
                observeAllEvents(
                    handler,
                    events.map { it.eventid!! }.maxBy { it }!!
                )
            })
        }

        fun getEventsBySearch(query: String, handler: Handler<List<Event>>) {
            apiClient.getSearchedEvents(query)!!.enqueue(EventGetter {
                handler(it)
            })
        }

        fun getEventsForUser(userid: String, handler: Handler<List<Event>>) {
            apiClient.getEvents(
                userid, "0"
            )?.enqueue(EventGetter { handler(it) })
        }

        fun getEvent(eventid: String, handler: Handler<Event>) {
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

        fun putEvent(event: Event, handler: Handler<String?>) {
            with(event) {
                date!!.also { date ->
                    apiClient.putEvent(
                        name!!,
                        description!!,
                        date.year!!,
                        date.month!!,
                        date.day!!,
                        date.hour!!,
                        date.minute!!,
                        place?:"",
                        lat!!,
                        lng!!,
                        authorid!!,
                        avatar!!
                    )?.enqueue(EventSender(handler))
                }
            }
        }

        fun updateEvent(eventid: String, event: Event, handler: Handler<Boolean>) {
            with(event) {
                date!!.also { date ->
                    apiClient.updateEvent(
                        eventid,
                        name!!,
                        description!!,
                        date.year!!,
                        date.month!!,
                        date.day!!,
                        date.hour!!,
                        date.minute!!,
                        place?:"",
                        lat!!,
                        lng!!,
                        avatar!!
                    )?.enqueue(EventUpdater(handler))
                }
            }
        }

        fun observeChatList(
            userid: String,
            handler: Handler<List<ChatView>>,
            chatsCount: Int = 0,
            lastMessageSum: Int = 0
        ) {
            apiClient.getChats(
                userid, chatsCount, lastMessageSum
            )?.enqueue(ChatListGetter(userid) { chats ->
                handler(chats)

                observeChatList(
                    userid, handler,
                    chats.count(),
                    tryDefault(lastMessageSum) { chats.sumBy { it.lastMessage!!._rowid_!! } }
                )
            })
        }

        fun getChatId(eventid: String, handler: Handler<String>) { // TODO: refactor to returning objects
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

        fun checkIsUserInChat(userid: String, chatid: String, handler: Handler<Boolean>) {
            apiClient.isUserInChat(
                userid = userid,
                chatid = chatid
            )?.enqueue(
                UserInChatChecker{ it?.let { it1 -> handler(it1) } }
            )
        }

        fun checkIsUserSubscribed(userid: String, eventid: String, handler: Handler<Boolean>) {
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

        fun addUserToChat(userid: String, chatid: String, handler: Handler<Unit>) {
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

        fun subscribe(userid: String, eventid: String, handler: Handler<Unit>) {
            apiClient.subscribeOnEvent(
                eventid = eventid,
                userid = userid
            )?.enqueue(EventSubscriber { handler(Unit) })
        }
        // endregion

        // region chat
        fun sendMessage(
            text: String,
            userid: String,
            chatid: String,
            attachments: List<Attachment>,
            handler: Handler<Boolean>
        ) {
            apiClient.putMessage(
                text,
                userid,
                chatid,
                attachments.map{ it.attachment_link }.joinToString(";")
            )?.enqueue(MessageSender(handler))
        }

        fun observeNewMessages(
            chatid: String, after: Int, handler: Handler<List<Message>>
        ) {
            apiClient.getMessages(
                chatid, after
            )?.enqueue(MessageGetter { newMessages ->
                handler(newMessages)

                observeNewMessages(
                    chatid,
                    when {
                        newMessages.isNotEmpty() -> newMessages.last()._rowid_!!
                        else -> after
                    },
                    handler
                )
            })
        }
        // endregion
    }
}
