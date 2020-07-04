package yoloyoj.pub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_loading)

        Handler().postDelayed({
            startActivity(Intent(this@LoadingActivity, MainActivity::class.java))
            finish()
        }, 1000)
    }
}
