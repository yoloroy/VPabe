package yoloyoj.pub.web.handlers

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.storage.Handler

class MessageSender(
    private val messageHandler: Handler<Boolean>
) : Callback<ResponseBody?> {
    // TODO: refactor
    // view needs to crying about failures
    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
        messageHandler(false)
    }

    override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
        messageHandler(true)
    }
}
