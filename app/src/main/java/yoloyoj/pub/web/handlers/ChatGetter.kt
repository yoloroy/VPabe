package yoloyoj.pub.web.handlers

import android.content.Context
import android.util.Log
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatGetter(
    val context: Context,
    var callback: (Int?) -> Unit
) : Callback<Int?> {

    override fun onFailure(call: Call<Int?>, t: Throwable) {
        Toast.makeText(context, "Ошибка при получении данных чата", Toast.LENGTH_LONG).show()
        callback(null)
    }

    override fun onResponse(call: Call<Int?>, response: Response<Int?>) {
        Log.d("ChatId", response.body().toString())
        callback(response.body())
    }
}
