package yoloyoj.pub.web

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "http://192.168.0.194:5000/"

val apiClient: Link
    get() {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder()
                    .setLenient()
                    .create()
            ))
            .build().create(Link::class.java)
}

