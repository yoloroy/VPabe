package yoloyoj.pub.web

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import yoloyoj.pub.models.*

@Suppress("unused")
 interface Link {
    @GET("putmessage")
    fun putMessage(
        @Query("text") text: String,
        @Query("sender") sender: Int,
        @Query("chatid") chatid: Int
    ): Call<ResponseBody?>?

    @GET("putevent")
    fun putEvent(
        @Query("name") name: String,
        @Query("description") description: String,
        @Query("month") month: String,
        @Query("day") day: String,
        @Query("place") place: String

    ): Call<ResponseBody?>?


    @GET("getmessages")
    fun getMessages(
        @Query("chatid") chatid: Int,
        @Query("after") after: Int
    ): Call<List<Message>?>?

    @GET("getuser")
    fun getUser(
        @Query("userid") userid: Int = 0,
        @Query("telephone") telephone: String = ""
    ): Call<User?>

    @GET("getaction")
    fun getAction(): Call<String?>?

    @GET("getevents")
    fun getEvents(
        @Query("userid") userid: Int = 0,
        @Query("eventid") eventid: Int = 0
    ): Call<List<Event>?>?

    @GET("getchats")
    fun getChats(
        @Query("userid") userid: Int = 0
    ): Call<List<ChatView>?>?
}