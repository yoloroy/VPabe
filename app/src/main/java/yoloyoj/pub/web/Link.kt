package yoloyoj.pub.web

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val v = "v1"

@Suppress("unused")
 interface Link {

    @GET("http://dev.virtualearth.net/REST/v1/Locations/{location}")
    fun getAddress(
        @Path("location") location: String,
        @Query("key") key: String
    ): Call<Map<String, Any>?>?

    @GET("$v/checkme")
    fun checkMe(
        @Query("phone") phone: String
    ): Call<String?>?
}
