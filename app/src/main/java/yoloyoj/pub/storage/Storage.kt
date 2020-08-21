package yoloyoj.pub.storage

import android.location.Location
import com.ckdroid.geofirequery.GeoQuery
import com.ckdroid.geofirequery.model.Distance
import com.ckdroid.geofirequery.setLocation
import com.ckdroid.geofirequery.utils.BoundingBoxUtils
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import yoloyoj.pub.models.Attachment
import yoloyoj.pub.models.Event
import yoloyoj.pub.models.Event.Companion.MESSAGES
import yoloyoj.pub.models.Event.Companion.SUBSCRIBERS
import yoloyoj.pub.models.Message
import yoloyoj.pub.models.User
import yoloyoj.pub.ui.event.MutableLocation
import java.util.*

typealias Handler<T> = (T) -> Unit

class Storage { // TODO: divide?
    companion object {
        private const val USERS = "users"
        private const val EVENTS = "events"

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
                                it.toObject(User::class.java)!!
                                    .apply {
                                        id = it.reference.id
                                    }
                            )
                        }
                } else {
                    users
                        .whereEqualTo(User.PHONE, temp)
                        .get()
                        .addOnSuccessListener {
                            handler(
                                it.last().toObject(User::class.java)
                                    .apply {
                                        id = it.last().reference.id
                                    }
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


            val userMap: HashMap<String, Any> = User.run {
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
            val userMap: HashMap<String, Any> = User.run {
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
                        it.toObject(Event::class.java)
                            .apply {
                                id = it.id
                            }
                    })
                }
        }

        fun observeEventsNearMutableLocation(
            location: MutableLocation,
            distance: Double,
            handler: Handler<List<Event>>
        ) {
            location.observeForever {
                if (it == null) return@observeForever

                observeEventsNearLocation(it, distance, handler)
            }
        }

        private fun observeEventsNearLocation(
            location: Location,
            distance: Double,
            handler: Handler<List<Event>>
        ) {
            GeoQuery()
                .collection(EVENTS)
                .whereNearToLocation(
                    location,
                    Distance(distance, BoundingBoxUtils.DistanceUnit.KILOMETERS)
                )
                .addSnapshotListener { _, value, _ ->
                    handler(value.map { it.toEvent()!! })
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
                                it.toObject(Event::class.java)
                                    .apply {
                                        id = it.id
                                    }
                            }
                            .filter {
                                (query.toLowerCase(Locale.ROOT) in it.name!!.toLowerCase(Locale.ROOT)) or
                                (query.toLowerCase(Locale.ROOT) in (it.description?: "").toLowerCase(Locale.ROOT))
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
                        it.toObject(Event::class.java)
                            .apply {
                                id = it.id
                            }
                    })
                }
        }

        fun getEvent(eventid: String, handler: Handler<Event>) {
            events.document(eventid)
                .get()
                .addOnSuccessListener {
                    handler(
                        it.toObject(Event::class.java)!!
                            .apply {
                                id = it.id
                            }
                    )
                }
        }

        fun putEvent(event: Event, handler: Handler<String?>) {
            with(event) {
                events
                    .add(
                        Event(
                            author = author,
                            avatar = avatar,
                            name = name,
                            description = description,
                            likes = 0,
                            messages = emptyList(),
                            place = place,
                            latlng = latlng,
                            date = date,
                            subscribers = emptyList()
                        )
                    )
                    .addOnSuccessListener {
                        latlng!!
                        it.setLocation(
                            latlng.latitude,
                            latlng.longitude
                        )

                        handler(it.id)
                    }
            }
        }

        fun updateEvent(eventid: String, event: Event, handler: Handler<Boolean>) {
            with(Event) {
            with(event) {
                latlng!!
                events.document(eventid)
                    .update(
                        hashMapOf<String, Any?>(
                            AVATAR to avatar,
                            NAME to name,
                            DESCRIPTION to description,
                            PLACE to place,
                            LATLNG to latlng,
                            DATE to date
                        )
                    )
                    .addOnCompleteListener {
                        handler(it.isSuccessful)

                    }
                events.document(eventid)
                    .setLocation(
                        latlng.latitude,
                        latlng.longitude
                    )
            }}
        }

        fun observeChatList(
            userid: String,
            handler: Handler<List<Event>>
        ) {
            events
                .whereArrayContains(SUBSCRIBERS, users.document(userid))
                .addSnapshotListener { snapshot, _ ->
                    handler(
                        snapshot!!
                            .map {
                                it.toObject(Event::class.java) to it.id
                            }
                            .mapIndexed { _, (event, id) ->
                                event.apply { this.id = id }
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
                        users.document(userid) in it.toObject(Event::class.java)!!.subscribers?: emptyList()
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
                    val event = it.toObject(Event::class.java)!!
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
                            event.toObject(Event::class.java)!!
                                .messages!!.map {
                                it.apply {
                                    _sender = subs[it.sender!!.id]
                                }
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
                    val event = snapshot.toObject(Event::class.java)!!
                    eventRef.update(
                        MESSAGES,
                        event.messages?.plus(
                            Message(
                                users.document(userid),
                                text,
                                attachments
                            ).apply {
                                users.document(userid).get()
                                    .addOnSuccessListener {
                                        _sender = it.toObject(User::class.java)
                                        handler(true)
                                    }
                            }
                        )
                    )
                }
        }

        fun observeMessages(
            chatid: String, after: Int, handler: Handler<List<Message>>
        ) {
            // subscribers geting here use for not calling for them every message
            getChatSubscribers(chatid) { subs ->
                events.document(chatid).addSnapshotListener { snapshot, _ ->
                    val event = snapshot!!.toObject(Event::class.java)!!
                    handler(event.messages!!
                        .map {
                            it.apply {
                                _sender = subs[it.sender!!.id]
                            }
                        }
                    )
                }
            }
        }
        // endregion

        private fun getChatSubscribers(chatid: String, handler: Handler<Map<String, User>>) {
            events.document(chatid)
                .get()
                .addOnSuccessListener {
                    val subs = hashMapOf<String, User>()
                    val subRefs = it.toObject(Event::class.java)!!.subscribers!!

                    subRefs.mapIndexed { index, userRef ->
                        userRef.get()
                            .addOnSuccessListener { userSnap ->
                                subs[userRef.id] = userSnap.toObject(User::class.java)!!

                                if (index == subRefs.lastIndex)
                                    handler(subs)
                            }
                    }
                }
        }

        private fun DocumentSnapshot.toEvent() =
            this.toObject(Event::class.java)
    }
}
