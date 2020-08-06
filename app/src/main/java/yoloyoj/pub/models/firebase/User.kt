package yoloyoj.pub.models.firebase

import yoloyoj.pub.models.User

public class User (
    val name: String? = null,
    val status: String? = null,
    val phone: String? = null,
    val avatar: String? = null
) {
    constructor() : this(null)
    fun toApp(id: String): User {
        return User(

        )
    }
}
