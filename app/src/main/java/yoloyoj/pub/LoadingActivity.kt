package yoloyoj.pub

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USER
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USERID
import yoloyoj.pub.ui.login.LoginActivity
import yoloyoj.pub.ui.registration.RegistrationActivity

class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_loading)

        Handler().postDelayed({
            if (
                getSharedPreferences(PREFERENCES_USER, Context.MODE_PRIVATE)
                    .getInt(PREFERENCES_USERID, 0) == 0
            ) {
                startActivity(Intent(this@LoadingActivity, LoginActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@LoadingActivity, MainActivity::class.java))
                finish()
            }
        }, 1000)
    }
}
