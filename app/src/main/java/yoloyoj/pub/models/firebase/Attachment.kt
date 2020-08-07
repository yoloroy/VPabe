package yoloyoj.pub.models.firebase

import yoloyoj.pub.models.Attachment

typealias FAttachment = yoloyoj.pub.models.firebase.Attachment

public class Attachment (
    val type: String? = null,
    val link: String? = null
) {
    companion object {
        fun fromApp(attachment: Attachment): FAttachment = FAttachment(
            attachment.attachment_type,
            attachment.attachment_link
        )
    }

    constructor() : this(null)

    fun toApp(): Attachment = Attachment(type, link)
}
