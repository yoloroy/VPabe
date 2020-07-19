package yoloyoj.pub.web.handlers

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.models.User
import yoloyoj.pub.web.apiClient

class UserUpdater(
    val context: Context
) : Callback<ResponseBody?> {

    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
        Toast.makeText(context, "Ошибка при сохранении данных", Toast.LENGTH_LONG).show()
    }

    override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
        // all is OK
    }
}