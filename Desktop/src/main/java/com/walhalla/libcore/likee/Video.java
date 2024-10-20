package com.walhalla.libcore.likee;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Video {

    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("commentCount")
    @Expose
    private Integer commentCount;
    @SerializedName("coverUrl")
    @Expose
    private String coverUrl;
    @SerializedName("likeCount")
    @Expose
    private Integer likeCount;
    @SerializedName("likeeId")
    @Expose
    private String likeeId;
    @SerializedName("msgText")
    @Expose
    private String msgText;
    @SerializedName("musicName")
    @Expose
    private String musicName;
    @SerializedName("musicId")
    @Expose
    private String musicId;
    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("yyuid")
    @Expose
    private String yyuid;
    @SerializedName("postId")
    @Expose
    private String postId;
    @SerializedName("shareUrl")
    @Expose
    private String shareUrl;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("shareCount")
    @Expose
    private Integer shareCount;
    @SerializedName("playCount")
    @Expose
    private Integer playCount;
    @SerializedName("videoHeight")
    @Expose
    private Integer videoHeight;
    @SerializedName("videoUrl")
    @Expose
    private Object videoUrl;
    @SerializedName("videoWidth")
    @Expose
    private Integer videoWidth;
    @SerializedName("hashtagInfos")
    @Expose
    private String hashtagInfos;
    @SerializedName("atUserInfos")
    @Expose
    private String atUserInfos;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("postTime")
    @Expose
    private Integer postTime;
    @SerializedName("sound")
    @Expose
    private Sound sound;
    @SerializedName("cloudMusic")
    @Expose
    private Object cloudMusic;
    @SerializedName("pgc")
    @Expose
    private String pgc;
    @SerializedName("posterUid")
    @Expose
    private String posterUid;
    @SerializedName("checkStatus")
    @Expose
    private Integer checkStatus;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("allImage")
    @Expose
    private String allImage;
    @SerializedName("music")
    @Expose
    private Object music;
    @SerializedName("postType")
    @Expose
    private Integer postType;
    @SerializedName("postLongDesc")
    @Expose
    private String postLongDesc;
    @SerializedName("postLongHashtagInfos")
    @Expose
    private String postLongHashtagInfos;
    @SerializedName("postLongAtUserInfos")
    @Expose
    private String postLongAtUserInfos;
    @SerializedName("superFollow")
    @Expose
    private Integer superFollow;
    @SerializedName("posterPrivateAccount")
    @Expose
    private Boolean posterPrivateAccount;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public String getLikeeId() {
        return likeeId;
    }

    public void setLikeeId(String likeeId) {
        this.likeeId = likeeId;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getYyuid() {
        return yyuid;
    }

    public void setYyuid(String yyuid) {
        this.yyuid = yyuid;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public Integer getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Integer playCount) {
        this.playCount = playCount;
    }

    public Integer getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(Integer videoHeight) {
        this.videoHeight = videoHeight;
    }

    public Object getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(Object videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(Integer videoWidth) {
        this.videoWidth = videoWidth;
    }

    public String getHashtagInfos() {
        return hashtagInfos;
    }

    public void setHashtagInfos(String hashtagInfos) {
        this.hashtagInfos = hashtagInfos;
    }

    public String getAtUserInfos() {
        return atUserInfos;
    }

    public void setAtUserInfos(String atUserInfos) {
        this.atUserInfos = atUserInfos;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getPostTime() {
        return postTime;
    }

    public void setPostTime(Integer postTime) {
        this.postTime = postTime;
    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public Object getCloudMusic() {
        return cloudMusic;
    }

    public void setCloudMusic(Object cloudMusic) {
        this.cloudMusic = cloudMusic;
    }

    public String getPgc() {
        return pgc;
    }

    public void setPgc(String pgc) {
        this.pgc = pgc;
    }

    public String getPosterUid() {
        return posterUid;
    }

    public void setPosterUid(String posterUid) {
        this.posterUid = posterUid;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAllImage() {
        return allImage;
    }

    public void setAllImage(String allImage) {
        this.allImage = allImage;
    }

    public Object getMusic() {
        return music;
    }

    public void setMusic(Object music) {
        this.music = music;
    }

    public Integer getPostType() {
        return postType;
    }

    public void setPostType(Integer postType) {
        this.postType = postType;
    }

    public String getPostLongDesc() {
        return postLongDesc;
    }

    public void setPostLongDesc(String postLongDesc) {
        this.postLongDesc = postLongDesc;
    }

    public String getPostLongHashtagInfos() {
        return postLongHashtagInfos;
    }

    public void setPostLongHashtagInfos(String postLongHashtagInfos) {
        this.postLongHashtagInfos = postLongHashtagInfos;
    }

    public String getPostLongAtUserInfos() {
        return postLongAtUserInfos;
    }

    public void setPostLongAtUserInfos(String postLongAtUserInfos) {
        this.postLongAtUserInfos = postLongAtUserInfos;
    }

    public Integer getSuperFollow() {
        return superFollow;
    }

    public void setSuperFollow(Integer superFollow) {
        this.superFollow = superFollow;
    }

    public Boolean getPosterPrivateAccount() {
        return posterPrivateAccount;
    }

    public void setPosterPrivateAccount(Boolean posterPrivateAccount) {
        this.posterPrivateAccount = posterPrivateAccount;
    }

}