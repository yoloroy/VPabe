package yoloyoj.pub

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class LearnFirestore {
    class User (
        val name: String? = null,
        val status: String? = null,
        val phone: String? = null,
        val avatar: String? = null
    ) {
        constructor() : this(null)
    }

    class Attachment (
        val type: String? = null,
        val link: String? = null
    ) {
        constructor() : this(null)
    }

    class Message (
        val sender: DocumentReference? = null,
        val text: String? = null,
        val attachments: List<Attachment>? = null
    ) {
        constructor() : this(null)
    }

    class Event (
        val author: DocumentReference? = null,
        val avatar: String? = null,
        val name: String? = null,
        val description: String? = null,
        val likes: Int? = null,
        val messages: List<Message>? = null,
        val place: String? = null,
        val latlng: GeoPoint? = null,
        val date: Timestamp? = null,
        val subscribers: List<DocumentReference>? = null
    ) {
        constructor() : this(null)
    }

    @Test
    fun learn() {
        var a = ""

        val db = FirebaseFirestore.getInstance()
        db.collection("events")
            .get()
            .addOnSuccessListener {
               a = it.toObjects(Event::class.java)
                   .toString()
            }
            .addOnCanceledListener {
                print("eror")
            }

        while (a == "") {}
        print(a)
    }
}
