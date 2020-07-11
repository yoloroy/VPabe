package yoloyoj.pub.models

class User {
    var userid: Int? = null
    var username: String? = null
    var telephone: String? = null
    var avatar: String? = null
    var status: String? = null

    fun hasNulls(): Boolean {
        return listOf(
            userid, username, telephone, avatar, status
        ).any { it == null }
    }
}
