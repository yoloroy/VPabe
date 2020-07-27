package yoloyoj.pub.web.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.storage.FirebaseStorage
import java.io.File

// see result in activity.onActivityResult
const val CODE_GET_PICTURE = 1
fun Activity.chooseImage() {
    val intent = Intent().apply {
        type = "image/*"
        action = Intent.ACTION_GET_CONTENT
    }

    startActivityForResult(
        Intent.createChooser(intent, "Select picture"),
        CODE_GET_PICTURE
    )
}

fun Fragment.chooseImage() {
    val intent = Intent().apply {
        type = "image/*"
        action = Intent.ACTION_GET_CONTENT
    }

    startActivityForResult(
        Intent.createChooser(intent, "Select picture"),
        CODE_GET_PICTURE
    )
}

fun putImage(uri: Uri, onImagePutted: (link: String) -> Unit) {
    val file = File(uri.path)

    val storage = FirebaseStorage.getInstance()
    val storageReference = storage
        .getReferenceFromUrl("gs://vpabe-75c05.appspot.com") // TODO: remove hardcode
        .child("${file.hashCode()}.${uri.path!!.split(".").last()}")

    storageReference.putFile(uri)
    storageReference.downloadUrl.addOnSuccessListener {
        onImagePutted("https://firebasestorage.googleapis.com${it.path!!}?alt=media")
    }
    storageReference.downloadUrl.addOnFailureListener {
        Log.e("storageErr", it.toString())
    }
}
