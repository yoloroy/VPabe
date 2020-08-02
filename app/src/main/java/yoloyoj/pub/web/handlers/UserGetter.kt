package yoloyoj.pub.web.handlers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.models.User

class UserGetter(
    var userUpdater: (User?) -> Unit
) : Callback<User?> {

    override fun onFailure(call: Call<User?>, t: Throwable) {
        userUpdater(null)
    }

    override fun onResponse(call: Call<User?>, response: Response<User?>) {
        userUpdater(response.body())
    }
}