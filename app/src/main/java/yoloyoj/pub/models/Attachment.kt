package yoloyoj.pub.models

const val TYPE_IMAGE = "image"
const val TYPE_DOCUMENT = "document"

class Attachment(
    var attachment_type: String? = null,
    var attachment_link: String? = null
)
