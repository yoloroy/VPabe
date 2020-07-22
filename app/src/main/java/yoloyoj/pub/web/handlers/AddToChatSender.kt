package yoloyoj.pub.web.handlers

import android.content.Context
import android.widget.Toast
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddToChatSender (
    val context: Context,
    val callback: () -> Unit
): Callback<ResponseBody?> {
    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
        Toast.makeText(context, "Ошибка при входе в чат", Toast.LENGTH_LONG).show()
    }

    override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
        callback()
    }
}