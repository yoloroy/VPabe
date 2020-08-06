package yoloyoj.pub.web.handlers

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.models.ChatView
import yoloyoj.pub.utils.tryDefault

class ChatListGetter(
    private val userid: String,
    var chatListener: ((List<ChatView>) -> Unit)? = null
) : Callback<List<ChatView>?> {

    override fun onFailure(call: Call<List<ChatView>?>, t: Throwable) {
        tryDefault(Unit) { Log.e("onFailure", t.localizedMessage) }
        chatListener?.let { it(emptyList()) }
    }

    override fun onResponse(call: Call<List<ChatView>?>, response: Response<List<ChatView>?>) {
        tryDefault(0) {
            chatListener?.let { it(response.body()!!) }
        }
    }
}