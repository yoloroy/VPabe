package yoloyoj.pub.web.handlers

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.models.Message
import yoloyoj.pub.storage.Handler
import yoloyoj.pub.utils.tryDefault

class MessageGetter(
    var messageUpdater: Handler<List<Message>>? = null
) : Callback<List<Message>?> {

    override fun onFailure(call: Call<List<Message>?>, t: Throwable) {
        tryDefault(Unit) { Log.i("onFailure", t.localizedMessage) }
        messageUpdater?.let { it(emptyList()) }
    }

    override fun onResponse(call: Call<List<Message>?>, response: Response<List<Message>?>) {
        messageUpdater?.let { it(response.body()?.filter { !it.hasNulls() }!!) }
    }
}