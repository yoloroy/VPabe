package yoloyoj.pub.web

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import yoloyoj.pub.models.ChatView
import yoloyoj.pub.models.Event
import yoloyoj.pub.models.Message
import yoloyoj.pub.models.User

@Suppress("unused")
public interface Link {
    @GET("putmessage")
    fun putMessage(
        @Query("text") text: String,
        @Query("sender") sender: Int,
        @Query("chatid") chatid: Int,
        @Query("attachment_link") attachmentLink: String
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

    @GET("putuser")
    fun regUser(
        @Query("username") name: String,
        @Query("telephone") telephone: String,
        @Query("avatar") avatar: String
    ): Call<Int>

    @GET("getaction")
    fun getAction(): Call<String?>?

    @GET("getevents")
    fun getEvents(
        @Query("userid") userid: Int = 0
    ): Call<List<Event>?>?

    @GET("getchats")
    fun getChats(
        @Query("userid") userid: Int = 0,
        @Query("chatscount") chatsCount: Int = 0,
        @Query("lms") lastMessageSum: Int = 0
    ): Call<List<ChatView>?>?

    @GET("upduser")
    fun updateUser(
        @Query("userid") userid: Int,
        @Query("username") username: String,
        @Query("status") status: String,
        @Query("avatar") avatar: String
    ): Call<ResponseBody?>?
}