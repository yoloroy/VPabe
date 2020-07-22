package yoloyoj.pub.web.handlers

import android.content.Context
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInChatChecker (
    val context: Context,
    val callback: (Boolean?) -> Unit
): Callback<Boolean?> {

    override fun onFailure(call: Call<Boolean?>, t: Throwable) {
        Toast.makeText(context, "Ошибка при обновлении статуса чата", Toast.LENGTH_LONG).show()
        callback(null)
    }

    override fun onResponse(call: Call<Boolean?>, response: Response<Boolean?>) {
        callback(response.body())
    }
}