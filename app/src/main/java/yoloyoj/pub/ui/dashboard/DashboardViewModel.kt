package yoloyoj.pub.ui.dashboard

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.models.Message
import yoloyoj.pub.web.apiClient

class DashboardViewModel : ViewModel() {
    var messages = MutableLiveData<MutableList<String>>().apply {
        value = mutableListOf()
    }

    init {
        runGetter()
        loadMessages()
    }

    fun loadMessages() {
        apiClient.getMessages()?.enqueue(MessageGetter())
    }

    fun runGetter() {
        apiClient.getAction()?.enqueue(ActionGetter())
    }

    inner class ActionGetter : Callback<String?> {
        override fun onFailure(call: Call<String?>, t: Throwable) {
            t.printStackTrace()
            runGetter()
        }

        override fun onResponse(call: Call<String?>, response: Response<String?>) {
            when(response.body()) {
                "message" -> loadMessages()
            }

            runGetter()
        }
    }

    inner class MessageGetter : Callback<List<Message>?> {
        override fun onFailure(call: Call<List<Message>?>, t: Throwable) {
            Log.i("response", t.message)
        }

        override fun onResponse(call: Call<List<Message>?>, response: Response<List<Message>?>) {
            messages.value = mutableListOf()

            response.body()?.forEach { message ->
                if (message.text == null) return@forEach
                messages.value!!.add(message.text!!)
            }
        }
    }
}
