package yoloyoj.pub.web.handlers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.web.apiClient

class ActionGetter : Callback<String?> {
    fun start(): Unit? =
        apiClient.getAction()?.enqueue(this)

    var actionListener: ((String) -> Unit)? = null

    override fun onFailure(call: Call<String?>, t: Throwable) {
        t.printStackTrace()

        start()
    }

    override fun onResponse(call: Call<String?>, response: Response<String?>) {
        actionListener?.let { it(response.body()!!) }

        start()
    }
}
