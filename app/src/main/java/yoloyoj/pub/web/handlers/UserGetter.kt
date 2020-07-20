package yoloyoj.pub.web.handlers

import android.content.Context
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.models.User
import yoloyoj.pub.web.apiClient

class UserGetter(
    val context: Context,
    var userUpdater: (User?) -> Unit
) : Callback<User?> {

    fun start(
        userid: Int = 0,
        telephone: String = ""
    ) {
        if ((userid != 0) or (telephone != ""))
            apiClient.getUser(userid, telephone).enqueue(this)
    }

    override fun onFailure(call: Call<User?>, t: Throwable) {
        Toast.makeText(context, "Ошибка при получении данных", Toast.LENGTH_LONG).show()
        userUpdater(null)
    }

    override fun onResponse(call: Call<User?>, response: Response<User?>) {
        userUpdater(response.body()!!)
    }
}