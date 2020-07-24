package yoloyoj.pub.ui.enter.registration

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.MainActivity
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USER
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USERID
import yoloyoj.pub.R
import yoloyoj.pub.ui.chat.view.CODE_GET_PICTURE
import yoloyoj.pub.web.apiClient
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

        userSender = UserSender { result, userid ->
            when(result) {
                REGISTERED_TRUE ->
                    apiClient.checkMe(phoneEdit.text.toString())!!.enqueue(object :
                        Callback<String?> {
                        override fun onFailure(call: Call<String?>, t: Throwable) = showVerificationFailMessage()

                        override fun onResponse(call: Call<String?>, response: Response<String?>) = checkCode(response.body()!!, userid!!)
                    })

                REGISTERED_FALSE ->
                    registerFailBanner.visibility = View.VISIBLE

                REGISTERED_FAIL ->
                    Snackbar.make(registerButton, "Произошла ошибка, пожалуйста, повторите попытку позже", Snackbar.LENGTH_LONG)

            }
        }
    }

    @SuppressLint("ApplySharedPref")
    private fun register(userid: Int) {
        getSharedPreferences(PREFERENCES_USER, Context.MODE_PRIVATE).edit().apply {
            putInt(PREFERENCES_USERID, userid)
            commit()
        }

        startActivity(Intent(this,  MainActivity::class.java))
        finish()
    }

    public fun onClickRegister(view: View) {
        if (name.isBlank() or phone.isBlank())
            registerFailBanner.visibility = View.VISIBLE

        userSender.start(name, phone, avatar)
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
        avatar = "https://firebasestorage.googleapis.com$link?alt=media"

        Picasso.get()
            .load(avatar)
            .placeholder(R.drawable.ic_person)
            .into(avatarEdit)
    }

    fun checkCode(code: String, userid: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_enter_code, null)
        val input = dialogView.findViewById<EditText>(R.id.input)
        val builder = AlertDialog.Builder(this).apply {
            setView(dialogView)
            setCancelable(false)
            setPositiveButton(getString(android.R.string.ok)) { dialog, id ->
                if (input.text.toString() == code)
                    onRegisterSuccess(userid)
                else
                    showVerificationFailMessage()
            }
        }
        builder.create().show()
    }

    @SuppressLint("ApplySharedPref")
    fun onRegisterSuccess(userid: Int) {
        register(userid)
    }

    private fun showVerificationFailMessage() {
        Snackbar.make(loginButton, "Ошибка верефикации", Snackbar.LENGTH_LONG).show()
    }
}
