package yoloyoj.pub.web.handlers

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.models.Message
import yoloyoj.pub.utils.tryDefault
import yoloyoj.pub.web.apiClient

class MessageGetter(
    var messageUpdater: ((List<Message>) -> Unit)? = null
) : Callback<List<Message>?> {
    fun start(
        chatid: Int,
        after: Int
    ) {
        apiClient.getMessages(
            chatid, after
        )?.enqueue(this)
    }

    override fun onFailure(call: Call<List<Message>?>, t: Throwable) {
        tryDefault(Unit) { Log.i("onFailure", t.localizedMessage) }
        messageUpdater?.let { it(emptyList()) }
    }

    override fun onResponse(call: Call<List<Message>?>, response: Response<List<Message>?>) {
        messageUpdater?.let { it(response.body()?.filter { !it.hasNulls() }!!) }
    }
}
