package yoloyoj.pub.models

const val TYPE_IMAGE = "image"
const val TYPE_DOCUMENT = "document"

class Attachment(
    var attachment_type: String? = null,
    var attachment_link: String? = null
) {
    override fun toString(): String =
        "{\n\tattachment_type=$attachment_type,\n\tattachment_link=$attachment_link\n},"
}
