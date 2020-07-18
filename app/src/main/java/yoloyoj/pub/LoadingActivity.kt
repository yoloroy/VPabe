package yoloyoj.pub

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import yoloyoj.pub.ui.login.LoginActivity

class LoadingActivity : AppCompatActivity() {

    companion object {
        const val PREFERENCES_LOGIN = "login"
        const val PREFERENCES_LOGIN_SIGNED = "signed"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_loading)

        Handler().postDelayed({
            /* Code below used before Login activity implemented

            if (getSharedPreferences(PREFERENCES_LOGIN, Context.MODE_PRIVATE)
                    .getBoolean(PREFERENCES_LOGIN_SIGNED, false)) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, RegistrationActivity::class.java))
            }
            finish()
            * */

            startActivity(Intent(this@LoadingActivity, LoginActivity::class.java))
            finish()
        }, 1000)
    }
}
