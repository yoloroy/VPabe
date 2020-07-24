package yoloyoj.pub.web.handlers

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.models.Event
import yoloyoj.pub.utils.tryDefault
import yoloyoj.pub.web.apiClient

class EventGetter(
    var eventListener: ((List<Event>) -> Unit)? = null
) : Callback<List<Event>?> {
    fun start(
        userid: Int = 0,
        eventid: Int = 0
    ): Unit? =
        apiClient.getEvents(userid, eventid)?.enqueue(this)

    override fun onFailure(call: Call<List<Event>?>, t: Throwable) {
        tryDefault(Unit) { Log.e("onFailure", t.localizedMessage) }
    }

    override fun onResponse(call: Call<List<Event>?>, response: Response<List<Event>?>) {
        eventListener?.let { it(response.body()!!) }
    }
}
