package yoloyoj.pub.ui.enter.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import yoloyoj.pub.ui.enter.registration.RegistrationActivity
import yoloyoj.pub.web.apiClient
import yoloyoj.pub.web.handlers.UserGetter

class LoginActivity : AppCompatActivity() {

    private lateinit var userGetter: UserGetter

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        userGetter = UserGetter (applicationContext) {
            if (it != null) {
                apiClient.checkMe(editTextPhone.text.toString())!!.enqueue(object : Callback<String?> {
                    override fun onFailure(call: Call<String?>, t: Throwable) = showVerificationFailMessage()

                    override fun onResponse(call: Call<String?>, response: Response<String?>) = checkCode(response.body()!!, it)
                })
            } else
                showWrongInputMessage()
        }

        super.onStart()
    }

    public fun onClickRegister(view: View) {
        startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
    }

    public fun onClickLogin(view: View) {
        userGetter.start(telephone = editTextPhone.text.toString())
    }

    fun checkCode(code: String, user: User) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_enter_code, null)
        val input = dialogView.findViewById<EditText>(R.id.input)
        val builder = AlertDialog.Builder(this).apply {
            setView(dialogView)
            setCancelable(false)
            setPositiveButton(getString(android.R.string.ok)) { dialog, id ->
                if (input.text.toString() == code)
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
                putInt(PREFERENCES_USERID, user.userid!!)
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
