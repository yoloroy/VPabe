package yoloyoj.pub.web.handlers

import android.view.View
import com.google.android.material.snackbar.Snackbar
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageSender(private val view: View) : Callback<ResponseBody?> {
    // TODO: refactor
    // view needs to crying about failures
    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
        Snackbar.make(view, "Не удалось отправить сообщение...", Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }

    override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
        // yeeeee boyyyyyyyy
    }
}
