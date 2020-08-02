package yoloyoj.pub.web.handlers

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatGetter(
    var callback: (Int?) -> Unit
) : Callback<Int?> {

    override fun onFailure(call: Call<Int?>, t: Throwable) {
        callback(null)
    }

    override fun onResponse(call: Call<Int?>, response: Response<Int?>) {
        Log.d("ChatId", response.body().toString())
        callback(response.body())
    }
}
