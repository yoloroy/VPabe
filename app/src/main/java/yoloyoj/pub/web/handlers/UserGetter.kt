package yoloyoj.pub.web.handlers

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.models.User
import yoloyoj.pub.web.apiClient

class UserGetter(
    var userUpdater: (User) -> Unit
) : Callback<User?> {

    fun start(
        userid: Int = 0,
        telephone: String = ""
    ) {
        if ((userid != 0) or (telephone != ""))
            apiClient.getUser(userid, telephone).enqueue(this)
    }

    override fun onFailure(call: Call<User?>, t: Throwable) {
        Log.i("response", t.message)
    }

    override fun onResponse(call: Call<User?>, response: Response<User?>) {
        userUpdater(response.body()!!)
    }
}