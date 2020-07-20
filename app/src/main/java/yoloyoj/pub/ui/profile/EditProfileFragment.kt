package yoloyoj.pub.ui.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USER
import yoloyoj.pub.R
import yoloyoj.pub.ui.chat.CODE_GET_PICTURE
import yoloyoj.pub.ui.login.LoginActivity
import yoloyoj.pub.web.apiClient
import yoloyoj.pub.web.handlers.UserGetter
import yoloyoj.pub.web.handlers.UserUpdater
import java.io.File

class EditProfileFragment: Fragment() {

    private var avatarLink = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_edit_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val userId = activity?.getSharedPreferences(PREFERENCES_USER, Context.MODE_PRIVATE)?.getInt("USER_ID", 1)
        if (userId == null || userId == 0){
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }
        UserGetter { user ->
            Picasso.get().load(user.avatar).into(editUserImage)
            editUserName.setText(user.username)
            editUserStatus.setText(user.status)

            avatarLink = user.avatar!!

            saveProfileButton.setOnClickListener {
                apiClient.updateUser(
                    userId!!,
                    editUserName.text.toString(),
                    editUserStatus.text.toString(),
                    avatarLink
                )?.enqueue(
                    UserUpdater(context!!)
                )
                activity!!.supportFragmentManager.popBackStack()
            }
        }.start(userId!!)

        setNewImageButton.setOnClickListener {
            addAttachment()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CODE_GET_PICTURE -> putImage(data!!.data!!)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun addAttachment() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }

        startActivityForResult(
            Intent.createChooser(intent, "Select picture"),
            CODE_GET_PICTURE
        )
    }

    private fun putImage(uri: Uri) {
        val file = File(uri.path)

        val storage = FirebaseStorage.getInstance()
        val storageReference = storage
            .getReferenceFromUrl("gs://vpabe-75c05.appspot.com") // TODO: remove hardcode
            .child("${file.hashCode()}.${uri.path!!.split(".").last()}")

        storageReference.putFile(uri)
        storageReference.downloadUrl.addOnSuccessListener {
            onImagePutted(it.toString())
        }
    }

    private fun onImagePutted(link: String) {
        Picasso.get().load(link).into(editUserImage)
        avatarLink = link
    }
}