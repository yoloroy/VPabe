package yoloyoj.pub.models

const val TYPE_IMAGE = "image"

public class Attachment (
    val type: String? = null,
    val link: String? = null
) {

    constructor() : this(null)
}
