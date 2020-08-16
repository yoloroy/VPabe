package yoloyoj.pub.models

public class User (
    val name: String? = null,
    val status: String? = null,
    val phone: String? = null,
    val avatar: String? = null,
    val experience: Int? = 0
) {
    companion object {
        const val AVATAR = "avatar"
        const val NAME = "name"
        const val PHONE = "phone"
        const val STATUS = "status"
    }

    lateinit var id: String

    constructor() : this(null)
}
