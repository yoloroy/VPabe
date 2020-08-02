package yoloyoj.pub.web.handlers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.models.Event

class SingleEventGetter(
    var eventHandler: ((Event?) -> Unit)
) : Callback<Event?> {

    override fun onFailure(call: Call<Event?>, t: Throwable) {
        eventHandler(null)
    }

    override fun onResponse(call: Call<Event?>, response: Response<Event?>) {
        eventHandler(response.body())
    }
}
