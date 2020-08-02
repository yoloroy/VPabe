package yoloyoj.pub.web.handlers

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.storage.Handler

class UserUpdater(
    val callback: Handler<Boolean>
) : Callback<ResponseBody?> {

    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
        callback(false)
    }

    override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
        callback(true)
    }
}