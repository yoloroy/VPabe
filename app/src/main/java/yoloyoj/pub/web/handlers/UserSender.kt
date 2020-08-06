package yoloyoj.pub.web.handlers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val REGISTERED_TRUE = true
const val REGISTERED_FALSE = false
val REGISTERED_FAIL = null

class UserSender(
    var userUpdater: (Boolean?, String?) -> Unit
) : Callback<String?> {

    override fun onFailure(call: Call<String?>, t: Throwable) {
        userUpdater(REGISTERED_FAIL, null)
    }

    override fun onResponse(call: Call<String?>, response: Response<String?>) {
        userUpdater(response.body()!! != "0", response.body()!!)
    }
}