package yoloyoj.pub.web.handlers

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatGetter(
    var callback: (String?) -> Unit
) : Callback<String?> {

    override fun onFailure(call: Call<String?>, t: Throwable) {
        callback(null)
    }

    override fun onResponse(call: Call<String?>, response: Response<String?>) {
        Log.d("ChatId", response.body().toString())
        callback(response.body())
    }
}