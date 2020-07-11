package yoloyoj.pub.models

class Message {
    var _rowid_: String? = null
    var text: String? = null
    var sender: Int? = null

    fun hasNulls(): Boolean {
        return (text == null) or (sender == null)
    }
}
