package com.walhalla.ttvloader.models

class LocalVideo {
    @JvmField var path: String? = null
    @JvmField var thumb: String? = null
    @JvmField var selected: Boolean = false
    @JvmField var duration: Int = 0
    var id: Int = 0
    override fun toString(): String {
        return "Video{" + "path='" + path + '\'' + ", thumb='" + thumb + '\'' + ", selected=" + selected + ", duration=" + duration + ", id=" + id + '}'
    }
}
