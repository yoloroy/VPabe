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
    var eventHandler: ((Event) -> Unit)? = null
) : Callback<Event?> {

    fun start(
        eventId: Int = 1
    ) {
        apiClient.getSingleEvent(eventId)?.enqueue(this)
    }

    override fun onFailure(call: Call<Event?>, t: Throwable) {
        Toast.makeText(context, "Ошибка при сохранении данных", Toast.LENGTH_LONG).show()
        eventHandler?.let {
            null
        }
    }

    override fun onResponse(call: Call<Event?>, response: Response<Event?>) {
        eventHandler?.let {
            it(response.body()!!)
        }
    }
}