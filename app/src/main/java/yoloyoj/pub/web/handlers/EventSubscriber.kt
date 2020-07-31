package yoloyoj.pub.web.handlers

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventSubscriber (
    val callback: () -> Unit
): Callback<ResponseBody?> {
    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {}

    override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
        callback()
    }
}