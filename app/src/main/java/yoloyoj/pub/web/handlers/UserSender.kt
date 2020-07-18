package yoloyoj.pub.web.handlers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.web.apiClient

const val REGISTERED_TRUE = true
const val REGISTERED_FALSE = false
val REGISTERED_FAIL = null

class UserSender(
    var userUpdater: (Boolean?) -> Unit
) : Callback<Boolean?> {

    fun start(
        name: String,
        telephone: String,
        avatar: String
    ) = apiClient.regUser(name, telephone, avatar).enqueue(this)

    override fun onFailure(call: Call<Boolean?>, t: Throwable) {
        userUpdater(REGISTERED_FAIL)
    }

    override fun onResponse(call: Call<Boolean?>, response: Response<Boolean?>) {
        userUpdater(response.body())
    }
}