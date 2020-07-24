package yoloyoj.pub.web

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import yoloyoj.pub.models.ChatView
import yoloyoj.pub.models.Event
import yoloyoj.pub.models.Message
import yoloyoj.pub.models.User

@Suppress("unused")
interface Link {
    @GET("putmessage")
    fun putMessage(
        @Query("text") text: String,
        @Query("sender") sender: Int,
        @Query("chatid") chatid: Int,
        @Query("attachment_link") attachmentLink: String
    ): Call<ResponseBody?>?

    @GET("putevent")
    fun putEvent(
        @Query("name") name: String,
        @Query("description") description: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int,
        @Query("hour") hour: Int,
        @Query("minute") minute: Int,
        @Query("place") place: String,
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("authorid") authorid: Int,
        @Query("avatar") avatar: String
    ): Call<Int?>?

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
        @Query("userid") userid: Int = 0,
        @Query("eventid") eventid: Int = 0
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

    @GET("searchevents")
    fun getSearchedEvents(
        @Query("searchtext") searchtext: String
    ): Call<List<Event>?>?

    @GET("http://dev.virtualearth.net/REST/v1/Locations/{location}")
    fun getAddress(
        @Path("location") location: String,
        @Query("key") key: String
    ): Call<Map<String, Any>?>?

    @GET("getevent")
    fun getSingleEvent(
        @Query("eventid") eventid: Int = 1
    ): Call<Event?>?

    @GET("updevent")
    fun updateEvent(
        @Query("eventid") eventid: Int,
        @Query("name") name: String,
        @Query("description") description: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int,
        @Query("hour") hour: Int,
        @Query("minute") minute: Int,
        @Query("place") place: String,
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("avatar") avatar: String
    ): Call<ResponseBody?>?

    @GET("dosubscribe")
    fun subscribeOnEvent(
        @Query("eventid") eventid: Int,
        @Query("userid") userid: Int,
        @Query("subscribe") subscribe: String = "true"
    ): Call<ResponseBody?>?

    @GET("checksubscribe")
    fun checkSubscribe(
        @Query("eventid") eventid: Int,
        @Query("userid") userid: Int
    ): Call<Boolean?>?

    @GET("getchatbyevent")
    fun getChatByEvent(
        @Query("eventid") eventid: Int
    ): Call<Int?>?

    @GET("addtochat")
    fun addUserToChat(
        @Query("chatid") chatid: Int,
        @Query("userid") userid: Int
    ): Call<ResponseBody?>?

    @GET("checkinchat")
    fun isUserInChat(
        @Query("chatid") chatid: Int,
        @Query("userid") userid: Int
    ): Call<Boolean?>?

    @GET("checkme")
    fun checkMe(
        @Query("phone") phone: String
    ): Call<String?>?
}
