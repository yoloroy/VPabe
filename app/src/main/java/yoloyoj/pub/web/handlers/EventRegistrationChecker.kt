package yoloyoj.pub.web.handlers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventRegistrationChecker (
    val callback: (Boolean?) -> Unit
): Callback<Boolean?> {
    override fun onFailure(call: Call<Boolean?>, t: Throwable) {
        callback(null)
    }

    override fun onResponse(call: Call<Boolean?>, response: Response<Boolean?>) {
        callback(response.body())
    }
}