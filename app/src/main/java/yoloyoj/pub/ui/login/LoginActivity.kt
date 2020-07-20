package yoloyoj.pub.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import yoloyoj.pub.MainActivity
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USER
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USERID
import yoloyoj.pub.R
import yoloyoj.pub.ui.registration.RegistrationActivity
import yoloyoj.pub.web.handlers.UserGetter

class LoginActivity : AppCompatActivity() {

    private lateinit var userGetter: UserGetter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    @SuppressLint("ApplySharedPref")
    override fun onStart() {
        userGetter = UserGetter {
            if (it != null) {
                getSharedPreferences(PREFERENCES_USER, Context.MODE_PRIVATE)
                    .edit().apply {
                        putInt(PREFERENCES_USERID, it.userid!!)
                        commit()
                    }

                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } else
                Snackbar.make(loginButton, "Проверьте введённые данные", Snackbar.LENGTH_LONG).show()
        }

        super.onStart()
    }

    public fun onClickRegister(view: View) {
        startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
    }

    public fun onClickLogin(view: View) {
        userGetter.start(telephone = editTextPhone.text.toString())
    }
}
