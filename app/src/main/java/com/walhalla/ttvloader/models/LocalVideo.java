package com.walhalla.ttvloader.models;

public class LocalVideo {

    public String path;
    public String thumb;
    public boolean selected;
    public int duration;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;

    @Override
    public String toString() {
        return "Video{" +
                "path='" + path + '\'' +
                ", thumb='" + thumb + '\'' +
                ", selected=" + selected +
                ", duration=" + duration +
                ", id=" + id +
                '}';
    }
}