package yoloyoj.pub.models

class Like(
    var likeid: Int? = null,
    var count: Int? = null
) {
    override fun toString(): String =
        "{ \n" +
            "likeid=${this.likeid},\n" +
            "count=${this.count}\n" +
            " }"
}
