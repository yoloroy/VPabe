package yoloyoj.pub.ui.registration

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_registration.*
import yoloyoj.pub.MainActivity
import yoloyoj.pub.R
import yoloyoj.pub.ui.chat.CODE_GET_PICTURE
import yoloyoj.pub.web.handlers.REGISTERED_FAIL
import yoloyoj.pub.web.handlers.REGISTERED_FALSE
import yoloyoj.pub.web.handlers.REGISTERED_TRUE
import yoloyoj.pub.web.handlers.UserSender
import java.io.File

class RegistrationActivity : AppCompatActivity() {

    lateinit var userSender: UserSender

    private val name: String
    get() = nameEdit.text.toString()

    private val phone: String
    get() = phoneEdit.text.toString()

    private var avatar: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        userSender = UserSender {
            when(it) {
                REGISTERED_TRUE ->
                    startActivity(Intent(this,  MainActivity::class.java))

                REGISTERED_FALSE ->
                    registerFailBanner.visibility = View.VISIBLE

                REGISTERED_FAIL ->
                    Snackbar.make(registerButton, "Произошла ошибка, пожалуйста, повторите попытку позже", Snackbar.LENGTH_LONG)

            }
        }
    }

    public fun onClickRegister(view: View) {
        if (name.isBlank() or phone.isBlank())
            registerFailBanner.visibility = View.VISIBLE

        userSender.start(
            name,
            phone,
            avatar
        )
    }

    public fun onClickBannerClose(view: View) {
        registerFailBanner.visibility = View.GONE
    }

    public fun onAvatarChoose(view: View) {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }

        startActivityForResult(
            Intent.createChooser(intent, "Select picture"),
            CODE_GET_PICTURE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CODE_GET_PICTURE -> putImage(data!!.data!!)
        }
    }

    private fun putImage(uri: Uri) {
        val file = File(uri.path)

        val storage = FirebaseStorage.getInstance()
        val storageReference = storage
            .getReferenceFromUrl("gs://vpabe-75c05.appspot.com") // TODO: remove hardcode
            .child("${file.hashCode()}.${uri.path!!.split(".").last()}")

        storageReference.putFile(uri)
        storageReference.downloadUrl.addOnSuccessListener {
            onImagePutted(it.path!!)
        }
    }

    private fun onImagePutted(link: String) {
        Picasso.get()
            .load(link)
            .placeholder(R.drawable.ic_person)
            .into(avatarEdit)

        avatar = "https://firebasestorage.googleapis.com$link?alt=media"
    }
}
