package yoloyoj.pub.web.handlers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.web.apiClient

class ActionGetter : Callback<String?> {
    fun start() = apiClient.getAction()?.enqueue(this)

    var actionListener: ((String) -> Unit)? = null

    override fun onFailure(call: Call<String?>, t: Throwable) {
        t.printStackTrace()

        call.enqueue(this)
    }

    override fun onResponse(call: Call<String?>, response: Response<String?>) {
        actionListener?.let { it(response.body()!!) }

        call.enqueue(this)
    }
}