package yoloyoj.pub.ui.imageview

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.arialyy.aria.core.Aria
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_image_view.*
import yoloyoj.pub.R

const val EXTRA_IMAGE_LINK = "il"

class ImageViewActivity : AppCompatActivity() {
    lateinit var imageLink: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)

        imageLink = intent.getStringExtra(EXTRA_IMAGE_LINK)

        Picasso.get()
            .load(
                imageLink
            )
            .noPlaceholder()
            .into(imageView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.image_view_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> { onBackPressed(); return true }
            R.id.download -> {
                try { download() } catch (e: Exception) { showFail() }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun download() {
        val root = Environment.getExternalStorageDirectory()

        val permission =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) { // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                1
            )
        }

        Aria.download(this)
            .load(imageLink)
            .setDownloadPath(
                root.absolutePath +
                    "/download/" +
                    imageLink.split("/").last() // file name
            )
            .start()
    }

    private fun showFail() {
        Snackbar.make(imageView, R.string.unexpected_fail, Snackbar.LENGTH_LONG)
    }
}
