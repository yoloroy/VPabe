package yoloyoj.pub.web.handlers

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.models.ChatView
import yoloyoj.pub.web.apiClient

class ChatListGetter(
    private val userid: Int,
    var chatListener: ((List<ChatView>) -> Unit)? = null
) : Callback<List<ChatView>?> {
    fun start() = apiClient.getChats(userid)?.enqueue(this)

    override fun onFailure(call: Call<List<ChatView>?>, t: Throwable) {
        Log.e("onFailure", t.localizedMessage)
    }

    override fun onResponse(call: Call<List<ChatView>?>, response: Response<List<ChatView>?>) {
        chatListener?.let { it(response.body()!!) }
    }
}