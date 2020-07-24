package yoloyoj.pub.web.handlers

import android.content.Context
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.models.Event
import yoloyoj.pub.web.apiClient

class SingleEventGetter(
    val context: Context,
    var eventHandler: ((Event?) -> Unit)
) : Callback<Event?> {

    fun start(
        eventId: Int = 0
    ) {
        apiClient.getSingleEvent(eventId)?.enqueue(this)
    }

    override fun onFailure(call: Call<Event?>, t: Throwable) {
        Toast.makeText(context, "Ошибка при получении данных", Toast.LENGTH_LONG).show()
        eventHandler(null)
    }

    override fun onResponse(call: Call<Event?>, response: Response<Event?>) {
        eventHandler(response.body())
    }
}
