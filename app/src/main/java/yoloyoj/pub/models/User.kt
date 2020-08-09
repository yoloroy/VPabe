package yoloyoj.pub.models

import yoloyoj.pub.models.firebase.User

class User {
    var userid: String? = null
    var username: String? = null
    var telephone: String? = null
    var avatar: String? = null
    var status: String? = null
    var origin: User? = null

    fun hasNulls(): Boolean {
        return listOf(
            userid, username, telephone, avatar, status
        ).any { it == null }
    }
}
