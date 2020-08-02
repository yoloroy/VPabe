package yoloyoj.pub.web.handlers

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.storage.Handler

class EventUpdater(
    val onEventUpdated: Handler<Boolean>
) : Callback<ResponseBody?> {

    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
        onEventUpdated(false)
    }

    override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
        onEventUpdated(true)
    }
}