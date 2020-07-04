package yoloyoj.pub.web

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import yoloyoj.pub.models.Message

@Suppress("unused")
public interface Link {
    @GET("putmessage")
    fun putMessage(
        @Query("text") text: String
    ): Call<ResponseBody?>?

    @GET("getmessages")
    fun getMessages(): Call<List<Message>?>?

    @GET("getaction")
    fun getAction(): Call<String?>?
}