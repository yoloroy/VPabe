package yoloyoj.pub.ui.enter.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.MainActivity
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USER
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USERID
import yoloyoj.pub.R
import yoloyoj.pub.models.User
import yoloyoj.pub.models.User.Companion.ID_ANONYMOUS_USER
import yoloyoj.pub.storage.Storage
import yoloyoj.pub.ui.enter.registration.RegistrationActivity
import yoloyoj.pub.web.apiClient
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private val phone: String
        get() = editTextPhone.text.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        AuthCallback.loginActivity = this
    }

    public fun onClickRegister(view: View) {
        startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
    }

    public fun onClickLogin(view: View) {
        if (!PhoneNumberUtils.isGlobalPhoneNumber(phone)) {
            showWrongInputMessage()
            return
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phone,
            60,
            TimeUnit.SECONDS,
            this,
            AuthCallback
        )
    }

    public fun onClickAnonymousLogon(view: View) {
        auth.signInAnonymously()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = auth.currentUser
                    onLoginSuccess(User().apply { id = ID_ANONYMOUS_USER })
                } else {
                    showVerificationFailMessage()
                }
            }
    }

    @SuppressLint("ApplySharedPref")
    fun onLoginSuccess(user: User) {
        getSharedPreferences(PREFERENCES_USER, Context.MODE_PRIVATE)
            .edit().apply {
                putString(PREFERENCES_USERID, user.id)
                commit()
            }

        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }

    private fun showVerificationFailMessage() {
        Snackbar.make(loginButton, "Ошибка верефикации", Snackbar.LENGTH_LONG).show()
    }

    private fun showWrongInputMessage() {
        Snackbar.make(loginButton, "Проверьте введённые данные", Snackbar.LENGTH_LONG).show()
    }

    object AuthCallback : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        lateinit var loginActivity: LoginActivity

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Storage.getUser(phone = loginActivity.phone) {
                loginActivity.onLoginSuccess(it!!)
            }
        }

        override fun onVerificationFailed(exception: FirebaseException) {
            loginActivity.showVerificationFailMessage()
        }

    }
}
