package yoloyoj.pub.web.handlers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventSender(
    var eventHandler: (String?) -> Unit
    ) : Callback<String?> {

    override fun onFailure(call: Call<String?>, t: Throwable) {
        eventHandler(null)
    }

    override fun onResponse(call: Call<String?>, response: Response<String?>) {
        eventHandler(response.body())
    }
}