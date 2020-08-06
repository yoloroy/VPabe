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

const val v = "v1"

@Suppress("unused")
 interface Link {
    @GET("$v/putmessage")
    fun putMessage(
        @Query("text") text: String,
        @Query("sender") sender: String,
        @Query("chatid") chatid: String,
        @Query("attachment_link") attachmentLink: String
    ): Call<ResponseBody?>?

    @GET("$v/putevent")
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
        @Query("authorid") authorid: String,
        @Query("avatar") avatar: String
    ): Call<String?>?

    @GET("$v/getmessages")
    fun getMessages(
        @Query("chatid") chatid: String,
        @Query("after") after: Int
    ): Call<List<Message>?>?

    @GET("$v/getuser")
    fun getUser(
        @Query("userid") userid: String = "0",
        @Query("telephone") telephone: String = ""
    ): Call<User?>

    @GET("$v/putuser")
    fun regUser(
        @Query("username") name: String,
        @Query("telephone") telephone: String,
        @Query("avatar") avatar: String
    ): Call<String>

    @GET("$v/getaction")
    fun getAction(): Call<String?>?

    @GET("$v/getevents")
    fun getEvents(
        @Query("userid") userid: String = "0",
        @Query("eventid") eventid: String = "0"
    ): Call<List<Event>?>?

    @GET("$v/getchats")
    fun getChats(
        @Query("userid") userid: String = "0",
        @Query("chatscount") chatsCount: Int = 0,
        @Query("lms") lastMessageSum: Int = 0
    ): Call<List<ChatView>?>?

    @GET("$v/upduser")
    fun updateUser(
        @Query("userid") userid: String,
        @Query("username") username: String,
        @Query("status") status: String,
        @Query("avatar") avatar: String
    ): Call<ResponseBody?>?

    @GET("$v/searchevents")
    fun getSearchedEvents(
        @Query("searchtext") searchtext: String
    ): Call<List<Event>?>?

    @GET("http://dev.virtualearth.net/REST/v1/Locations/{location}")
    fun getAddress(
        @Path("location") location: String,
        @Query("key") key: String
    ): Call<Map<String, Any>?>?
   
    @GET("$v/getevent")
    fun getSingleEvent(
        @Query("eventid") eventid: String = "1"
    ): Call<Event?>?

    @GET("$v/updevent")
    fun updateEvent(
        @Query("eventid") eventid: String,
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

    @GET("$v/dosubscribe")
    fun subscribeOnEvent(
        @Query("eventid") eventid: String,
        @Query("userid") userid: String,
        @Query("subscribe") subscribe: String = "true"
    ): Call<ResponseBody?>?

    @GET("$v/checksubscribe")
    fun checkSubscribe(
        @Query("eventid") eventid: String,
        @Query("userid") userid: String
    ): Call<Boolean?>?

    @GET("$v/getchatbyevent")
    fun getChatByEvent(
        @Query("eventid") eventid: String
    ): Call<String?>?

    @GET("$v/addtochat")
    fun addUserToChat(
        @Query("chatid") chatid: String,
        @Query("userid") userid: String
    ): Call<ResponseBody?>?

    @GET("$v/checkinchat")
    fun isUserInChat(
        @Query("chatid") chatid: String,
        @Query("userid") userid: String
    ): Call<Boolean?>?

    @GET("$v/checkme")
    fun checkMe(
        @Query("phone") phone: String
    ): Call<String?>?
}