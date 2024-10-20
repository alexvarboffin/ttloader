package com.walhalla.ttvloader;


public class TTResponse {

    public String contentURL;
    public String thumb;
    public String username;
    public String videoKey;
    public String cleanVideo;
    public String title;

    public String ext;


    public String description;//insta
    public long timestamp;//insta

    @Override
    public String toString() {
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
                '}';
    }
}
