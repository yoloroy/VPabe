package yoloyoj.pub.web.handlers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventSender(
    var eventHandler: (Int?) -> Unit
    ) : Callback<Int?> {

    override fun onFailure(call: Call<Int?>, t: Throwable) {
        eventHandler(null)
    }

    override fun onResponse(call: Call<Int?>, response: Response<Int?>) {
        eventHandler(response.body())
    }
}