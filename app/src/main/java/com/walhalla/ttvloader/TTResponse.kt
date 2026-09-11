package com.walhalla.ttvloader


class TTResponse {
    @JvmField
    var contentURL: String? = null
    @JvmField
    var thumb: String? = null
    @JvmField
    var username: String? = null
    var videoKey: String? = null
    @JvmField
    var cleanVideo: String? = null
    @JvmField
    var title: String? = null

    @JvmField
    var ext: String? = null


    @JvmField
    var description: String? = null //insta
    @JvmField
    var timestamp: Long = 0 //insta

    override fun toString(): String {
        return "TTResponse{" +
                "contentURL='" + contentURL + '\'' +
                ", thumb='" + thumb + '\'' +
                ", username='" + username + '\'' +
                ", videoKey='" + videoKey + '\'' +
                ", cleanVideo='" + cleanVideo + '\'' +
                ", title='" + title + '\'' +
                ", ext='" + ext + '\'' +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp +
                '}'
    }
}
