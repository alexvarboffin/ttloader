package com.walhalla.ttvloader.activity.mime;

import java.util.Arrays;
import java.util.List;

public class MimeTabData {
    public static final List<String> MIME_TYPES = Arrays.asList(
            "video/mp4",
            "video/3gpp",
            "video/avi",
            "video/mpeg",
            "video/quicktime",
            "video/webm",
            "video/x-matroska",
            "video/x-ms-wmv",
            "video/x-flv",
            "video/ogg",
            "video/x-msvideo",
            "video/x-m4v",
            "*/*",
            "image/*",
            "video/*"
    );

    public static final List<String> TAB_TITLES = MIME_TYPES;
}