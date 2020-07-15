package yoloyoj.pub.web.handlers

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.models.Event
import yoloyoj.pub.web.apiClient

class EventGetter(
    var eventListener: ((List<Event>) -> Unit)? = null
) : Callback<List<Event>?> {
    fun start() = apiClient.getEvents()?.enqueue(this)

    override fun onFailure(call: Call<List<Event>?>, t: Throwable) {
        Log.e("onFailure", t.localizedMessage)
        start()
    }

    override fun onResponse(call: Call<List<Event>?>, response: Response<List<Event>?>) {
        eventListener?.let { it(response.body()!!) }

        start()
    }
}
