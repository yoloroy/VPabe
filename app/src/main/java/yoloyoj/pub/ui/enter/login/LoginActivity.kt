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
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.MainActivity
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USER
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USERID
import yoloyoj.pub.R
import yoloyoj.pub.models.User
import yoloyoj.pub.storage.Storage
import yoloyoj.pub.ui.enter.registration.RegistrationActivity
import yoloyoj.pub.web.apiClient

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private val phone: String
        get() = editTextPhone.text.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
    }

    public fun onClickRegister(view: View) {
        startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
    }

    public fun onClickLogin(view: View) {
        if (!PhoneNumberUtils.isGlobalPhoneNumber(phone)) {
            showWrongInputMessage()
            return
        }

        Storage.getUser(phone = phone) {
            if (it == null) {
                showWrongInputMessage()
                return@getUser
            }
            checkMe(it)
        }
    }

    private fun checkMe(user: User) {
        apiClient.checkMe(phone)!!.enqueue(object : Callback<String?> {
            override fun onFailure(call: Call<String?>, t: Throwable) = showVerificationFailMessage()

            override fun onResponse(call: Call<String?>, response: Response<String?>) = checkCode(response.body()!!, user)
        })
    }

    fun checkCode(code: String, user: User) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_enter_code, null)
        val input = dialogView.findViewById<EditText>(R.id.input)
        val builder = AlertDialog.Builder(this).apply {
            setView(dialogView)
            setCancelable(false)
            setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                if (input.text.toString().isBlank() or (input.text.toString() == code))
                    onLoginSuccess(user)
                else
                    showVerificationFailMessage()
            }
        }
        builder.create().show()
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
}
