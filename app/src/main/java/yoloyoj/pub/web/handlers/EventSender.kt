package yoloyoj.pub.web.handlers

import android.content.Context
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventSender(
    val context: Context,
    var eventHandler: (Int?) -> Unit
) : Callback<Int?> {
    override fun onFailure(call: Call<Int?>, t: Throwable) {
        Toast.makeText(context, "Ошибка при сохранении события", Toast.LENGTH_LONG).show()
        eventHandler(null)
    }

    override fun onResponse(call: Call<Int?>, response: Response<Int?>) {
        eventHandler(response.body())
    }
}
