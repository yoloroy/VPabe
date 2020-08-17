package yoloyoj.pub.ui.enter.registration

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.MainActivity
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USER
import yoloyoj.pub.MainActivity.Companion.PREFERENCES_USERID
import yoloyoj.pub.R
import yoloyoj.pub.storage.Storage
import yoloyoj.pub.web.apiClient
import yoloyoj.pub.web.utils.CODE_GET_PICTURE
import yoloyoj.pub.web.utils.chooseImage
import yoloyoj.pub.web.utils.putImage

const val REGISTERED_TRUE = true
const val REGISTERED_FALSE = false
val REGISTERED_FAIL = null

class RegistrationActivity : AppCompatActivity() {

    private val name: String
    get() = nameEdit.text.toString()

    private val phone: String
    get() = phoneEdit.text.toString()

    private var avatar: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
    }

    @SuppressLint("ApplySharedPref")
    private fun register(userid: String) {
        getSharedPreferences(PREFERENCES_USER, Context.MODE_PRIVATE).edit().apply {
            putString(PREFERENCES_USERID, userid)
            commit()
        }

        startActivity(Intent(this,  MainActivity::class.java))
        finish()
    }

    public fun onClickRegister(view: View) {
        if (name.isBlank() or
            phone.isBlank() or
            !PhoneNumberUtils.isGlobalPhoneNumber(phone)) {
            showWrongInputMessage()
            return
        }

        Storage.regUser(name, phone, avatar) { (result, userid) ->
            when(result) {
                REGISTERED_TRUE -> checkMe(userid!!)

                REGISTERED_FALSE -> showWrongInputMessage()

                REGISTERED_FAIL -> showUnknownErrorMessage()
            }
        }
    }

    private fun checkMe(userid: String) {
        apiClient.checkMe(phone)!!.enqueue(object :
            Callback<String?> {
            override fun onFailure(call: Call<String?>, t: Throwable) = showVerificationFailMessage()

            override fun onResponse(call: Call<String?>, response: Response<String?>) = checkCode(response.body()!!, userid)
        })
    }

    private fun showUnknownErrorMessage() {
        Snackbar.make(registerButton, "Произошла ошибка, пожалуйста, повторите попытку позже", Snackbar.LENGTH_LONG)
    }

    public fun onClickBannerClose(view: View) {
        registerFailBanner.visibility = View.GONE
    }

    public fun onAvatarChoose(view: View) = chooseImage()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CODE_GET_PICTURE -> putImage(data!!.data!!) { onImagePutted(it) }
        }
    }

    private fun onImagePutted(link: String) {
        avatar = link

        Picasso.get()
            .load(link)
            .placeholder(R.drawable.ic_person)
            .into(avatarEdit)
    }

    fun checkCode(code: String, userid: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_enter_code, null)
        val input = dialogView.findViewById<EditText>(R.id.input)
        val builder = AlertDialog.Builder(this).apply {
            setView(dialogView)
            setCancelable(false)
            setPositiveButton(getString(android.R.string.ok)) { dialog, id ->
                if (input.text.toString() == code)
                    onRegisterSuccess(userid)
                else
                    showVerificationFailMessage()
            }
        }
        builder.create().show()
    }

    @SuppressLint("ApplySharedPref")
    fun onRegisterSuccess(userid: String) {
        register(userid)
    }

    private fun showVerificationFailMessage() {
        Snackbar.make(loginButton, "Ошибка верефикации", Snackbar.LENGTH_LONG).show()
    }

    private fun showWrongInputMessage() {
        registerFailBanner.visibility = View.VISIBLE
    }
}
