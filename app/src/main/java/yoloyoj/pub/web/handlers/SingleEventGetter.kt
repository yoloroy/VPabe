package yoloyoj.pub.web.handlers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.models.Event
import yoloyoj.pub.web.apiClient

class SingleEventGetter(
    var eventHandler: ((Event?) -> Unit)
) : Callback<Event?> {

    fun start(
        eventId: Int = 0
    ) {
        apiClient.getSingleEvent(eventId)?.enqueue(this)
    }

    override fun onFailure(call: Call<Event?>, t: Throwable) {
        eventHandler(null)
    }

    override fun onResponse(call: Call<Event?>, response: Response<Event?>) {
        eventHandler(response.body())
    }
}