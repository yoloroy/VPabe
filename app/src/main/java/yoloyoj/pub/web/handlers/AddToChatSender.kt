package yoloyoj.pub.web.handlers

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddToChatSender (
    val callback: (Boolean) -> Unit
): Callback<ResponseBody?> {
    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
        callback(false)
    }

    override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
        callback(true)
    }
}