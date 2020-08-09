package yoloyoj.pub.models.firebase

import yoloyoj.pub.models.User

public class User (
    val name: String? = null,
    val status: String? = null,
    val phone: String? = null,
    val avatar: String? = null
) {
    companion object {
        const val AVATAR = "avatar"
        const val NAME = "name"
        const val PHONE = "phone"
        const val STATUS = "status"
    }

    lateinit var id: String

    constructor() : this(null)

    fun toApp(): User = // :poop: temporary
        User().apply {
            userid = id
            username = name
            telephone = phone
            avatar = this@User.avatar
            status = this@User.status
            origin = this@User
        }
}
