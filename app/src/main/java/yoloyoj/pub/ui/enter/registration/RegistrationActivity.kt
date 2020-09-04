package yoloyoj.pub.ui.enter.registration

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firestore.v1.UpdateDocumentRequest
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
import yoloyoj.pub.storage.Storage
import yoloyoj.pub.ui.enter.login.LoginActivity
import yoloyoj.pub.web.apiClient
import yoloyoj.pub.web.utils.CODE_GET_PICTURE
import yoloyoj.pub.web.utils.chooseImage
import yoloyoj.pub.web.utils.putImage
import java.util.concurrent.TimeUnit

const val REGISTERED_TRUE = true
const val REGISTERED_FALSE = false
val REGISTERED_FAIL = null

class RegistrationActivity : AppCompatActivity() {

    private val name: String
    get() = nameEdit.text.toString()

    private val phone: String
    get() = phoneEdit.text.toString()

    private val email: String
    get() = emailEdit.text.toString()

    private val password: String
    get() = passwordEdit.text.toString()

    private var avatar: String = ""

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = FirebaseAuth.getInstance()
        PhoneCheckCallback.registrationActivity = this
    }

    @SuppressLint("ApplySharedPref")
    private fun register(userid: String) {
        getSharedPreferences(PREFERENCES_USER, Context.MODE_PRIVATE).edit().apply {
            putString(PREFERENCES_USERID, userid)
            commit()
        }

        startActivity(Intent(this,  MainActivity::class.java))
        finish()
    }

    public fun onClickRegister(view: View) {
        if (
            name.isBlank() or
            phone.isBlank() or
            !PhoneNumberUtils.isGlobalPhoneNumber(phone)
        ) {
            showWrongInputMessage()
            return
        }

        auth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    PhoneCheckCallback.otherCredentialUserUpdates = {
                        val credential = it.result!!.credential
                        val user = auth.currentUser

                        val profileUpdates = UserProfileChangeRequest.Builder().apply {
                            displayName = name
                            photoUri = Uri.parse(avatar)

                        }
                        user!!
                    }

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phone,
                        60,
                        TimeUnit.SECONDS,
                        this,
                        PhoneCheckCallback
                    )
                } else {
                    showWrongInputMessage()
                }
            }
    }

    public fun onClickBannerClose(view: View) {
        registerFailBanner.visibility = View.GONE
    }

    public fun onAvatarChoose(view: View) = chooseImage()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CODE_GET_PICTURE -> putImage(data!!.data!!) { onImagePutted(it) }
        }
    }

    private fun onImagePutted(link: String) {
        avatar = link

        Picasso.get()
            .load(link)
            .placeholder(R.drawable.ic_person)
            .into(avatarEdit)
    }

    fun checkCode(code: String, userid: String) {
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
    fun onRegisterSuccess(userid: String) {
        register(userid)
    }

    private fun showVerificationFailMessage() {
        Snackbar.make(findViewById(android.R.id.content), "Ошибка верефикации", Snackbar.LENGTH_LONG).show()
    }

    private fun showWrongInputMessage() {
        registerFailBanner.visibility = View.VISIBLE
    }

    object PhoneCheckCallback : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        lateinit var registrationActivity: RegistrationActivity
        lateinit var otherCredentialUserUpdates: () -> FirebaseUser

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            registrationActivity.auth.signInWithCredential(credential)
                .addOnSuccessListener {
                    otherCredentialUserUpdates().also { user ->
                        user.updatePhoneNumber(credential)
                    }
                }
            Storage.regUser(
                name = registrationActivity.name,
                phone = registrationActivity.phone,
                avatar = registrationActivity.avatar
            ) { (success, id) ->
                if (success)
                    registrationActivity.onRegisterSuccess(id!!)
                else
                    onVerificationFailed(FirebaseException("Storage registration fail"))
            }
        }

        override fun onVerificationFailed(exception: FirebaseException) {
            registrationActivity.showVerificationFailMessage()
        }

    }
}
