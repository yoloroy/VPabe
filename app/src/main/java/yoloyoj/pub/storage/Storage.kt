package yoloyoj.pub.storage

import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import yoloyoj.pub.models.*
import yoloyoj.pub.models.firebase.Event.Companion.MESSAGES
import yoloyoj.pub.models.firebase.Event.Companion.SUBSCRIBERS
import java.util.Date
import yoloyoj.pub.models.firebase.Attachment as FAttachment
import yoloyoj.pub.models.firebase.Event as FEvent
import yoloyoj.pub.models.firebase.Message as FMessage
import yoloyoj.pub.models.firebase.User as FUser

typealias Handler<T> = (T) -> Unit

class Storage { // TODO: divide?
    companion object {
        const val USERS = "users"
        const val EVENTS = "events"

        private val db = FirebaseFirestore.getInstance()

        val users: CollectionReference
            get() = db.collection(USERS)

        val events: CollectionReference
            get() = db.collection(EVENTS)

        // region user
        fun getUser(
            phone: String = "",
            userid: String = "0", // temporary
            handler: Handler<User?>
        ) { // TODO: add failure handler
            if ((userid != "0") or (phone != "")) {
                var temp = phone
                if (temp.startsWith('+')) {
                    temp = "${temp[1].toString().toInt()+1}${temp.slice(2 until temp.length)}"
                }

                // TODO: divide?
                if (userid != "0") {
                    users.document(userid)
                        .get()
                        .addOnSuccessListener {
                            handler(
                                it.toObject(FUser::class.java)!!
                                    .apply {
                                        id = it.reference.id
                                    }
                                    .toApp()
                            )
                        }
                } else {
                    users
                        .whereEqualTo(FUser.PHONE, temp)
                        .get()
                        .addOnSuccessListener {
                            handler(
                                it.last().toObject(FUser::class.java)
                                    .apply {
                                        id = it.last().reference.id
                                    }
                                    .toApp()
                            )
                        }
                }
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


            val userMap: HashMap<String, Any> = FUser.run {
                hashMapOf(
                    NAME to name,
                    PHONE to temp,
                    AVATAR to avatar,
                    STATUS to ""
                )
            }

            users
                .add(userMap)
                .addOnCompleteListener {
                    handler(it.isSuccessful to it.result?.id)
                }
        }

        fun updateUser(
            userid: String,
            name: String,
            status: String,
            avatarLink: String,
            handler: Handler<Boolean>
        ) {
            val userMap: HashMap<String, Any> = FUser.run {
                hashMapOf(
                    NAME to name,
                    AVATAR to avatarLink,
                    STATUS to status
                )
            }

            users.document(userid)
                .update(userMap)
                .addOnCompleteListener { handler(it.isSuccessful) }
        }
        // endregion

        // region events
        fun observeAllEvents(handler: Handler<List<Event>>) {
            events
                .addSnapshotListener { value, _ ->
                    handler(value!!.map {
                        it.toObject(FEvent::class.java)
                            .apply {
                                id = it.id
                            }
                            .toApp()
                    })
                }
        }

        fun getEventsBySearch(query: String, handler: Handler<List<Event>>) {
            // TODO: do search by Algolia
            events
                .get()
                .addOnSuccessListener { snapshot ->
                    handler(
                        snapshot
                            .map {
                                it.toObject(FEvent::class.java)
                                    .apply {
                                        id = it.id
                                    }
                                    .toApp()
                            }
                            .filter {
                                (query in it.name!!) or (query in (it.description?: ""))
                            }
                    )
                }
        }

        fun getEventsForUser(userid: String, handler: Handler<List<Event>>) {
            events
                .whereArrayContains(SUBSCRIBERS, users.document(userid))
                .get()
                .addOnSuccessListener { snapshot ->
                    handler(snapshot.map {
                        it.toObject(FEvent::class.java)
                            .apply {
                                id = it.id
                            }
                            .toApp()
                    })
                }
        }

        fun getEvent(eventid: String, handler: Handler<Event>) {
            events.document(eventid)
                .get()
                .addOnSuccessListener {
                    handler(
                        it.toObject(FEvent::class.java)!!
                            .apply {
                                id = it.id
                            }
                            .toApp()
                    )
                }
        }

        fun putEvent(event: Event, handler: Handler<String?>) {
            with(event) {
                events
                    .add(
                        FEvent(
                            author = users.document(authorid!!),
                            avatar = avatar,
                            name = name,
                            description = description,
                            likes = 0,
                            messages = emptyList(),
                            place = place,
                            latlng = GeoPoint(lat!!, lng!!),
                            date = Timestamp(date?.javaDate?: Date()),
                            subscribers = emptyList()
                        )
                    )
                    .addOnSuccessListener { handler(it.id) }
            }
        }

        fun updateEvent(eventid: String, event: Event, handler: Handler<Boolean>) {
            with(FEvent) {
            with(event) {
                events.document(eventid)
                    .update(
                        hashMapOf<String, Any?>(
                            AVATAR to avatar,
                            NAME to name,
                            DESCRIPTION to description,
                            PLACE to place,
                            LATLNG to GeoPoint(lat!!, lng!!),
                            DATE to Timestamp(date?.javaDate?: Date())
                        )
                    )
                    .addOnCompleteListener { handler(it.isSuccessful) }
            }}
        }

        fun observeChatList(
            userid: String,
            handler: Handler<List<ChatView>>
        ) {
            events
                .whereArrayContains(SUBSCRIBERS, users.document(userid))
                .addSnapshotListener { snapshot, _ ->
                    handler(
                        snapshot!!
                            .map {
                                it.toObject(FEvent::class.java) to it.id
                            }
                            .mapIndexed { i, (event, id) ->
                                ChatView(
                                    id,
                                    null,
                                    event.avatar,
                                    event.messages?.last()?.toApp(i, id),
                                    event.name
                                )
                            }
                    )
                }
        }

        fun getChatId(eventid: String, handler: Handler<String>) { // TODO: refactor to returning objects
            handler(eventid)
        }

        fun checkIsUserInChat(userid: String, chatid: String, handler: Handler<Boolean>) =
            checkIsUserSubscribed(userid, chatid, handler) // TODO

        fun checkIsUserSubscribed(userid: String, eventid: String, handler: Handler<Boolean>) {
            events.document(eventid)
                .get()
                .addOnSuccessListener {
                    handler(
                        users.document(userid) in it.toObject(FEvent::class.java)!!.subscribers?: emptyList()
                    )
                }
        }

        fun addUserToChat(userid: String, chatid: String, handler: Handler<Unit>) {
            subscribe(userid, chatid, handler)
        }

        fun subscribe(userid: String, eventid: String, handler: Handler<Unit>) {
            val eventRef = events.document(eventid)

            eventRef.get()
                .addOnSuccessListener {
                    val event = it.toObject(FEvent::class.java)!!
                    eventRef
                        .update(SUBSCRIBERS, event.subscribers?.plus(users.document(userid)))
                        .addOnSuccessListener {
                            handler(Unit)
                        }
                }
        }
        // endregion

        // region chat
        fun getMessages(chatid: String, handler: Handler<List<Message>>) {
            getChatSubscribers(chatid) { subs ->
                events.document(chatid)
                    .get()
                    .addOnSuccessListener { event ->
                        handler(
                            event.toObject(FEvent::class.java)!!
                                .messages!!.mapIndexed { i, it ->
                                it
                                    .apply {
                                        _sender = subs[it.sender!!.id]
                                    }
                                    .toApp(i, chatid)
                            }
                        )
                    }
            }
        }

        fun sendMessage(
            text: String,
            userid: String,
            chatid: String,
            attachments: List<Attachment>,
            handler: Handler<Boolean>
        ) {
            val eventRef = events.document(chatid)

            eventRef.get()
                .addOnSuccessListener { snapshot ->
                    val event = snapshot.toObject(FEvent::class.java)!!
                    eventRef.update(
                        MESSAGES,
                        event.messages?.plus(
                            FMessage(
                                users.document(userid),
                                text,
                                attachments.map { FAttachment.fromApp(it) }
                            ).apply {
                                users.document(userid).get()
                                    .addOnSuccessListener {
                                        _sender = it.toObject(FUser::class.java)
                                        handler(true)
                                    }
                            }
                        )
                    )
                }
        }

        fun observeNewMessages(
            chatid: String, after: Int, handler: Handler<List<Message>>
        ) {
            getChatSubscribers(chatid) { subs ->
                events.document(chatid).addSnapshotListener { snapshot, error ->
                    val event = snapshot!!.toObject(FEvent::class.java)!!
                    handler(event.messages!!
                        .filterIndexed { index, _ -> index > after }
                        .mapIndexed { i, it ->
                            it
                                .apply {
                                    _sender = subs[it.sender!!.id]
                                }
                                .toApp(i, chatid)
                        }
                    )
                }
            }
        }
        // endregion

        private fun getChatSubscribers(chatid: String, handler: Handler<Map<String, FUser>>) {
            events.document(chatid)
                .get()
                .addOnSuccessListener {
                    val subs = hashMapOf<String, FUser>()
                    val subRefs = it.toObject(FEvent::class.java)!!.subscribers!!

                    subRefs.mapIndexed { index, userRef ->
                        userRef.get()
                            .addOnSuccessListener { userSnap ->
                                subs[userRef.id] = userSnap.toObject(FUser::class.java)!!

                                if (index == subRefs.lastIndex)
                                    handler(subs)
                            }
                    }
                }
        }
    }
}
