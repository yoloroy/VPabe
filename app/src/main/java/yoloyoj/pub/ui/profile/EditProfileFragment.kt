package yoloyoj.pub.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import yoloyoj.pub.R
import yoloyoj.pub.ui.login.LoginActivity
import yoloyoj.pub.web.apiClient
import yoloyoj.pub.web.handlers.UserGetter
import yoloyoj.pub.web.handlers.UserUpdater

class EditProfileFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_edit_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val userId = activity?.getSharedPreferences(PREFERENCES_USER, Context.MODE_PRIVATE)?.getInt("USER_ID", 0)
        if (userId == null || userId == 0){
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }
        UserGetter { user ->
            Picasso.get().load(user.avatar).into(editUserImage)
            editUserName.setText(user.username)
            editUserStatus.setText(user.status)

            saveProfileButton.setOnClickListener {
                apiClient.updateUser(
                    userId!!,
                    editUserName.text.toString(),
                    editUserStatus.text.toString(),
                    user.avatar!! // TODO: Upload new photo
                )?.enqueue(
                    UserUpdater(context!!)
                )
                activity!!.supportFragmentManager.popBackStack()
            }
        }.start(userId!!)
    }
}