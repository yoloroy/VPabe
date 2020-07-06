package yoloyoj.pub.models

class Message {
    var text: String? = null

    fun hasNulls(): Boolean {
        return text == null
    }
}
