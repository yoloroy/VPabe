package yoloyoj.pub.ui.profile.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USER
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USERID
import yoloyoj.pub.R
import yoloyoj.pub.ui.enter.login.LoginActivity
import yoloyoj.pub.web.apiClient
import yoloyoj.pub.web.handlers.UserGetter
import yoloyoj.pub.web.handlers.UserUpdater
import yoloyoj.pub.web.utils.CODE_GET_PICTURE
import yoloyoj.pub.web.utils.chooseImage
import yoloyoj.pub.web.utils.putImage

class EditProfileFragment: Fragment() {

    private var avatarLink = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_edit_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val userId = activity
            ?.getSharedPreferences(PREFERENCES_USER, Context.MODE_PRIVATE)
            ?.getInt(PREFERENCES_USERID, 1)

        if (userId == null || userId == 0){
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }
        UserGetter { user ->
            if (user!!.avatar!!.isNotBlank())
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
                    UserUpdater(context!!) {
                        val imm: InputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(getView()!!.windowToken, 0)
                        activity!!.supportFragmentManager.popBackStack()
                    }
                )
            }
        }.start(userId!!)

        setNewImageButton.setOnClickListener { chooseImage() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        when (requestCode) {
            CODE_GET_PICTURE -> putImage(data.data!!) { onImagePutted(it) }
        }
    }

    private fun onImagePutted(link: String) {
        Log.i("hmm", "mmh")
        Picasso.get().load(link).into(editUserImage)
        avatarLink = link
    }
}
