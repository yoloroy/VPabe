package yoloyoj.pub

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USER
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USERID
import yoloyoj.pub.ui.enter.login.LoginActivity

class LoadingActivity : AppCompatActivity() {

    companion object {
        const val TIME_MILLIS_SHOW_LOADING: Long = 1000
    }

    private var auth: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_loading)

        auth = FirebaseAuth.getInstance().currentUser
        val userid = getSharedPreferences(PREFERENCES_USER, Context.MODE_PRIVATE).getString(PREFERENCES_USERID, null)

        Handler().postDelayed({
            if ((auth == null) or userid.isNullOrEmpty()) {
                startActivity(Intent(this@LoadingActivity, LoginActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@LoadingActivity, MainActivity::class.java))
                finish()
            }
        }, TIME_MILLIS_SHOW_LOADING)
    }
}
