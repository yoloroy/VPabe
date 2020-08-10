package yoloyoj.pub.ui.utils.attachment.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_attachments_view.*
import yoloyoj.pub.R
import yoloyoj.pub.models.Attachment
import yoloyoj.pub.ui.chat.view.ATTACHMENTS_LINKS
import yoloyoj.pub.ui.chat.view.ATTACHMENTS_TYPES
import yoloyoj.pub.utils.product

class AttachmentsViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attachments_view)

        val types = intent.getStringArrayExtra(ATTACHMENTS_TYPES).asList()
        val links = intent.getStringArrayExtra(ATTACHMENTS_LINKS).asList()

        attachmentView.adapter = AttachmentViewAdapter(
            types.product(links).map {
                Attachment(
                    it.first,
                    it.second
                )
            }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
