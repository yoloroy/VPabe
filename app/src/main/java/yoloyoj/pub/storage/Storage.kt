package yoloyoj.pub.storage

import yoloyoj.pub.models.User
import yoloyoj.pub.web.handlers.UserGetter

class Storage {
    companion object {
        const val USERS = "users"
        const val EVENTS = "events"

        fun getUser(
            phone: String = "",
            userid: Int = 0, // temporary
            userHandler: (User) -> Unit
        ) {
            var userGetter: UserGetter? = null
            userGetter = UserGetter {
                if (it == null) {
                    userGetter!!.start(telephone = phone, userid = userid)
                    return@UserGetter
                }

                userHandler(it)
            }

            userGetter.start(telephone = phone, userid = userid)
        }
    }
}