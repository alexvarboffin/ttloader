package com.retrytech.medialoot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DMVideo {
    @SerializedName("access_id")
    @Expose
    private String accessId;
    @SerializedName("advertising")
    @Expose
    private Advertising advertising;
    @SerializedName("channel")
    @Expose
    private String channel;
    @SerializedName("created_time")
    @Expose
    private long createdTime;
    @SerializedName("duration")
    @Expose
    private long duration;
    @SerializedName("endscreen")
    @Expose
    private Endscreen endscreen;
    @SerializedName("explicit")
    @Expose
    private boolean explicit;
    @SerializedName("filmstrip_url")
    @Expose
    private String filmstripUrl;
    @SerializedName("hardware_encoded")
    @Expose
    private boolean hardwareEncoded;
    @SerializedName("id")
    @Expose

    /* renamed from: id */
    private String f508id;
    @SerializedName("info")
    @Expose
    private Info info;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("like")
    @Expose
    private boolean like;
    @SerializedName("live_show_viewers")
    @Expose
    private boolean liveShowViewers;
    @SerializedName("media_type")
    @Expose
    private String mediaType;
    @SerializedName("owner")
    @Expose
    private Owner owner;
    @SerializedName("partner")
    @Expose
    private boolean partner;
    @SerializedName("poster_url")
    @Expose
    private String posterUrl;
    @SerializedName("posters")
    @Expose
    private Posters posters;
    @SerializedName("promoted")
    @Expose
    private boolean promoted;
    @SerializedName("protected_delivery")
    @Expose
    private boolean protectedDelivery;
    @SerializedName("qualities")
    @Expose
    private Qualities qualities;
    @SerializedName("reporting")
    @Expose
    private Reporting reporting;
    @SerializedName("repost")
    @Expose
    private boolean repost;
    @SerializedName("sharing")
    @Expose
    private Object sharing;
    @SerializedName("show_live_viewers")
    @Expose
    private boolean showLiveViewers;
    @SerializedName("stream_chromecast_url")
    @Expose
    private String streamChromecastUrl;
    @SerializedName("stream_type")
    @Expose
    private String streamType;
    @SerializedName("studio")
    @Expose
    private boolean studio;
    @SerializedName("subtitles")
    @Expose
    private Subtitles subtitles;
    @SerializedName("tags")
    @Expose
    private ArrayList<Object> tags = null;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("ui")
    @Expose

    /* renamed from: ui */
    private Ui f509ui;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("userbrand")
    @Expose
    private boolean userbrand;
    @SerializedName("verified")
    @Expose
    private boolean verified;
    @SerializedName("view_id")
    @Expose
    private String viewId;
    @SerializedName("watchlater")
    @Expose
    private boolean watchlater;

    public String getFilmstripUrl() {
        return this.filmstripUrl;
    }

    public void setFilmstripUrl(String str) {
        this.filmstripUrl = str;
    }

    public String getPosterUrl() {
        return this.posterUrl;
    }

    public void setPosterUrl(String str) {
        this.posterUrl = str;
    }

    public boolean isHardwareEncoded() {
        return this.hardwareEncoded;
    }

    public void setHardwareEncoded(boolean z) {
        this.hardwareEncoded = z;
    }

    public boolean isProtectedDelivery() {
        return this.protectedDelivery;
    }

    public void setProtectedDelivery(boolean z) {
        this.protectedDelivery = z;
    }

    public boolean isShowLiveViewers() {
        return this.showLiveViewers;
    }

    public void setShowLiveViewers(boolean z) {
        this.showLiveViewers = z;
    }

    public String getChannel() {
        return this.channel;
    }

    public void setChannel(String str) {
        this.channel = str;
    }

    public long getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(long j) {
        this.createdTime = j;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long j) {
        this.duration = j;
    }

    public boolean isExplicit() {
        return this.explicit;
    }

    public void setExplicit(boolean z) {
        this.explicit = z;
    }

    public String getId() {
        return this.f508id;
    }

    public void setId(String str) {
        this.f508id = str;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String str) {
        this.language = str;
    }

    public String getMediaType() {
        return this.mediaType;
    }

    public void setMediaType(String str) {
        this.mediaType = str;
    }

    public String getStreamChromecastUrl() {
        return this.streamChromecastUrl;
    }

    public void setStreamChromecastUrl(String str) {
        this.streamChromecastUrl = str;
    }

    public boolean isPartner() {
        return this.partner;
    }

    public void setPartner(boolean z) {
        this.partner = z;
    }

    public boolean isPromoted() {
        return this.promoted;
    }

    public void setPromoted(boolean z) {
        this.promoted = z;
    }

    public boolean isRepost() {
        return this.repost;
    }

    public void setRepost(boolean z) {
        this.repost = z;
    }

    public boolean isLiveShowViewers() {
        return this.liveShowViewers;
    }

    public void setLiveShowViewers(boolean z) {
        this.liveShowViewers = z;
    }

    public boolean isStudio() {
        return this.studio;
    }

    public void setStudio(boolean z) {
        this.studio = z;
    }

    public ArrayList<Object> getTags() {
        return this.tags;
    }

    public void setTags(ArrayList<Object> arrayList) {
        this.tags = arrayList;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public boolean isUserbrand() {
        return this.userbrand;
    }

    public void setUserbrand(boolean z) {
        this.userbrand = z;
    }

    public boolean isVerified() {
        return this.verified;
    }

    public void setVerified(boolean z) {
        this.verified = z;
    }

    public String getViewId() {
        return this.viewId;
    }

    public void setViewId(String str) {
        this.viewId = str;
    }

    public String getAccessId() {
        return this.accessId;
    }

    public void setAccessId(String str) {
        this.accessId = str;
    }

    public Advertising getAdvertising() {
        return this.advertising;
    }

    public void setAdvertising(Advertising advertising2) {
        this.advertising = advertising2;
    }

    public Posters getPosters() {
        return this.posters;
    }

    public void setPosters(Posters posters2) {
        this.posters = posters2;
    }

    public Endscreen getEndscreen() {
        return this.endscreen;
    }

    public void setEndscreen(Endscreen endscreen2) {
        this.endscreen = endscreen2;
    }

    public Owner getOwner() {
        return this.owner;
    }

    public void setOwner(Owner owner2) {
        this.owner = owner2;
    }

    public Qualities getQualities() {
        return this.qualities;
    }

    public void setQualities(Qualities qualities2) {
        this.qualities = qualities2;
    }

    public Reporting getReporting() {
        return this.reporting;
    }

    public void setReporting(Reporting reporting2) {
        this.reporting = reporting2;
    }

    public Object getSharing() {
        return this.sharing;
    }

    public void setSharing(Object obj) {
        this.sharing = obj;
    }

    public String getStreamType() {
        return this.streamType;
    }

    public void setStreamType(String str) {
        this.streamType = str;
    }

    public Subtitles getSubtitles() {
        return this.subtitles;
    }

    public void setSubtitles(Subtitles subtitles2) {
        this.subtitles = subtitles2;
    }

    public Ui getUi() {
        return this.f509ui;
    }

    public void setUi(Ui ui) {
        this.f509ui = ui;
    }

    public boolean isLike() {
        return this.like;
    }

    public void setLike(boolean z) {
        this.like = z;
    }

    public boolean isWatchlater() {
        return this.watchlater;
    }

    public void setWatchlater(boolean z) {
        this.watchlater = z;
    }

    public Info getInfo() {
        return this.info;
    }

    public void setInfo(Info info2) {
        this.info = info2;
    }

    public static class Reporting {
        @SerializedName("comScore")
        @Expose
        private ComScore comScore;
        @SerializedName("enable")
        @Expose
        private boolean enable;
        @SerializedName("tracking")
        @Expose
        private Tracking tracking;

        public boolean isEnable() {
            return this.enable;
        }

        public void setEnable(boolean z) {
            this.enable = z;
        }

        public Tracking getTracking() {
            return this.tracking;
        }

        public void setTracking(Tracking tracking2) {
            this.tracking = tracking2;
        }

        public ComScore getComScore() {
            return this.comScore;
        }

        public void setComScore(ComScore comScore2) {
            this.comScore = comScore2;
        }
    }

    public static class _360 {
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("url")
        @Expose
        private String url;

        public String getType() {
            return this.type;
        }

        public void setType(String str) {
            this.type = str;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String str) {
            this.url = str;
        }
    }

    public static class Ima {
        @SerializedName("enable")
        @Expose
        private boolean enable;
        @SerializedName("timeout")
        @Expose
        private Timeout timeout;
        @SerializedName("url")
        @Expose
        private Object url;

        public boolean isEnable() {
            return this.enable;
        }

        public void setEnable(boolean z) {
            this.enable = z;
        }

        public Timeout getTimeout() {
            return this.timeout;
        }

        public void setTimeout(Timeout timeout2) {
            this.timeout = timeout2;
        }

        public Object getUrl() {
            return this.url;
        }

        public void setUrl(Object obj) {
            this.url = obj;
        }
    }

    public static class Tracking {
        @SerializedName("comscore")
        @Expose
        private String comscore;
        @SerializedName("gravity@50%|10m")
        @Expose
        private String gravity5010m;
        @SerializedName("internal")
        @Expose
        private String internal;
        @SerializedName("krux")
        @Expose
        private String krux;
        @SerializedName("loggerv3")
        @Expose
        private String loggerv3;
        @SerializedName("play15@15s")
        @Expose
        private String play1515s;
        @SerializedName("progress@/10%")
        @Expose
        private String progress10;

        public String getComscore() {
            return this.comscore;
        }

        public void setComscore(String str) {
            this.comscore = str;
        }

        public String getInternal() {
            return this.internal;
        }

        public void setInternal(String str) {
            this.internal = str;
        }

        public String getLoggerv3() {
            return this.loggerv3;
        }

        public void setLoggerv3(String str) {
            this.loggerv3 = str;
        }

        public String getKrux() {
            return this.krux;
        }

        public void setKrux(String str) {
            this.krux = str;
        }

        public String getProgress10() {
            return this.progress10;
        }

        public void setProgress10(String str) {
            this.progress10 = str;
        }

        public String getGravity5010m() {
            return this.gravity5010m;
        }

        public void setGravity5010m(String str) {
            this.gravity5010m = str;
        }

        public String getPlay1515s() {
            return this.play1515s;
        }

        public void setPlay1515s(String str) {
            this.play1515s = str;
        }
    }

    public static class ComScore {
        @SerializedName("c2")
        @Expose

        /* renamed from: c2 */
        private String f504c2;
        @SerializedName("c3")
        @Expose

        /* renamed from: c3 */
        private String f505c3;
        @SerializedName("c4")
        @Expose

        /* renamed from: c4 */
        private String f506c4;
        @SerializedName("id")
        @Expose

        /* renamed from: id */
        private String f507id;

        public String getId() {
            return this.f507id;
        }

        public void setId(String str) {
            this.f507id = str;
        }

        public String getC2() {
            return this.f504c2;
        }

        public void setC2(String str) {
            this.f504c2 = str;
        }

        public String getC3() {
            return this.f505c3;
        }

        public void setC3(String str) {
            this.f505c3 = str;
        }

        public String getC4() {
            return this.f506c4;
        }

        public void setC4(String str) {
            this.f506c4 = str;
        }
    }

    public static class _480 {
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("url")
        @Expose
        private String url;

        public String getType() {
            return this.type;
        }

        public void setType(String str) {
            this.type = str;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String str) {
            this.url = str;
        }
    }

    public static class _1080 {
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("url")
        @Expose
        private String url;

        public String getType() {
            return this.type;
        }

        public void setType(String str) {
            this.type = str;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String str) {
            this.url = str;
        }
    }

    public static class _380 {
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("url")
        @Expose
        private String url;

        public String getType() {
            return this.type;
        }

        public void setType(String str) {
            this.type = str;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String str) {
            this.url = str;
        }
    }

    public static class _184 {
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("url")
        @Expose
        private String url;

        public String getType() {
            return this.type;
        }

        public void setType(String str) {
            this.type = str;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String str) {
            this.url = str;
        }
    }

    public static class Endscreen {
        @SerializedName("action")
        @Expose
        private String action;
        @SerializedName("enable")
        @Expose
        private boolean enable;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("url")
        @Expose
        private String url;

        public boolean isEnable() {
            return this.enable;
        }

        public void setEnable(boolean z) {
            this.enable = z;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String str) {
            this.url = str;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String str) {
            this.type = str;
        }

        public String getAction() {
            return this.action;
        }

        public void setAction(String str) {
            this.action = str;
        }
    }

    public static class Owner {
        @SerializedName("id")
        @Expose

        /* renamed from: id */
        private String f510id;
        @SerializedName("parent")
        @Expose
        private Object parent;
        @SerializedName("partner")
        @Expose
        private boolean partner;
        @SerializedName("screenname")
        @Expose
        private String screenname;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("verified")
        @Expose
        private boolean verified;

        public String getId() {
            return this.f510id;
        }

        public void setId(String str) {
            this.f510id = str;
        }

        public Object getParent() {
            return this.parent;
        }

        public void setParent(Object obj) {
            this.parent = obj;
        }

        public boolean isPartner() {
            return this.partner;
        }

        public void setPartner(boolean z) {
            this.partner = z;
        }

        public String getScreenname() {
            return this.screenname;
        }

        public void setScreenname(String str) {
            this.screenname = str;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String str) {
            this.url = str;
        }

        public String getUsername() {
            return this.username;
        }

        public void setUsername(String str) {
            this.username = str;
        }

        public boolean isVerified() {
            return this.verified;
        }

        public void setVerified(boolean z) {
            this.verified = z;
        }
    }

    public static class Advertising {
        @SerializedName("ad_error_url")
        @Expose
        private String adErrorUrl;
        @SerializedName("ad_url")
        @Expose
        private String adUrl;
        @SerializedName("fb_placement_id")
        @Expose
        private String fbPlacementId;
        @SerializedName("ima")
        @Expose
        private Ima ima;

        public String getAdUrl() {
            return this.adUrl;
        }

        public void setAdUrl(String str) {
            this.adUrl = str;
        }

        public String getAdErrorUrl() {
            return this.adErrorUrl;
        }

        public void setAdErrorUrl(String str) {
            this.adErrorUrl = str;
        }

        public String getFbPlacementId() {
            return this.fbPlacementId;
        }

        public void setFbPlacementId(String str) {
            this.fbPlacementId = str;
        }

        public Ima getIma() {
            return this.ima;
        }

        public void setIma(Ima ima2) {
            this.ima = ima2;
        }
    }

    public static class _112 {
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("url")
        @Expose
        private String url;

        public String getType() {
            return this.type;
        }

        public void setType(String str) {
            this.type = str;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String str) {
            this.url = str;
        }
    }

    public static class Subtitles {
        @SerializedName("data")
        @Expose
        private ArrayList<Object> data = null;
        @SerializedName("enable")
        @Expose
        private boolean enable;

        public boolean isEnable() {
            return this.enable;
        }

        public void setEnable(boolean z) {
            this.enable = z;
        }

        public ArrayList<Object> getData() {
            return this.data;
        }

        public void setData(ArrayList<Object> arrayList) {
            this.data = arrayList;
        }
    }

    public static class _240 {
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("url")
        @Expose
        private String url;

        public String getType() {
            return this.type;
        }

        public void setType(String str) {
            this.type = str;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String str) {
            this.url = str;
        }
    }

    /* renamed from: com.fastdownloader.freevideo.downloadmanager.downloadingmodels.DailyMotionModel.Ui */
    public static class Ui {
    }

    public static class Posters {
        @SerializedName("1080")
        @Expose
        private String _1080;
        @SerializedName("120")
        @Expose
        private String _120;
        @SerializedName("180")
        @Expose
        private String _180;
        @SerializedName("240")
        @Expose
        private String _240;
        @SerializedName("360")
        @Expose
        private String _360;
        @SerializedName("480")
        @Expose
        private String _480;
        @SerializedName("60")
        @Expose
        private String _60;
        @SerializedName("720")
        @Expose
        private String _720;

        public String get60() {
            return this._60;
        }

        public void set60(String str) {
            this._60 = str;
        }

        public String get120() {
            return this._120;
        }

        public void set120(String str) {
            this._120 = str;
        }

        public String get180() {
            return this._180;
        }

        public void set180(String str) {
            this._180 = str;
        }

        public String get240() {
            return this._240;
        }

        public void set240(String str) {
            this._240 = str;
        }

        public String get360() {
            return this._360;
        }

        public void set360(String str) {
            this._360 = str;
        }

        public String get480() {
            return this._480;
        }

        public void set480(String str) {
            this._480 = str;
        }

        public String get720() {
            return this._720;
        }

        public void set720(String str) {
            this._720 = str;
        }

        public String get1080() {
            return this._1080;
        }

        public void set1080(String str) {
            this._1080 = str;
        }
    }

    public static class _144 {
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("url")
        @Expose
        private String url;

        public String getType() {
            return this.type;
        }

        public void setType(String str) {
            this.type = str;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String str) {
            this.url = str;
        }
    }

    public static class Qualities {
        @SerializedName("1080")
        @Expose
        private ArrayList<DMVideo._1080> _1080 = null;
        @SerializedName("112")
        @Expose
        private ArrayList<DMVideo._112> _112 = null;
        @SerializedName("144")
        @Expose
        private ArrayList<DMVideo._144> _144 = null;
        @SerializedName("184")
        @Expose
        private ArrayList<DMVideo._184> _184 = null;
        @SerializedName("240")
        @Expose
        private ArrayList<DMVideo._240> _240 = null;
        @SerializedName("288")
        @Expose
        private ArrayList<_288> _288 = null;
        @SerializedName("360")
        @Expose
        private ArrayList<DMVideo._360> _360 = null;
        @SerializedName("380")
        @Expose
        private ArrayList<DMVideo._380> _380 = null;
        @SerializedName("480")
        @Expose
        private ArrayList<DMVideo._480> _480 = null;
        @SerializedName("720")
        @Expose
        private ArrayList<_720> _720 = null;
        @SerializedName("auto")
        @Expose
        private ArrayList<Auto> auto = null;

        public ArrayList<Auto> getAuto() {
            return this.auto;
        }

        public void setAuto(ArrayList<Auto> arrayList) {
            this.auto = arrayList;
        }

        public ArrayList<DMVideo._144> get144() {
            return this._144;
        }

        public void set144(ArrayList<DMVideo._144> arrayList) {
            this._144 = arrayList;
        }

        public ArrayList<DMVideo._184> get184() {
            return this._184;
        }

        public void set184(ArrayList<DMVideo._184> arrayList) {
            this._184 = arrayList;
        }

        public ArrayList<DMVideo._240> get240() {
            return this._240;
        }

        public void set240(ArrayList<DMVideo._240> arrayList) {
            this._240 = arrayList;
        }

        public ArrayList<DMVideo._380> get380() {
            return this._380;
        }

        public void set380(ArrayList<DMVideo._380> arrayList) {
            this._380 = arrayList;
        }

        public ArrayList<DMVideo._480> get480() {
            return this._480;
        }

        public void set480(ArrayList<DMVideo._480> arrayList) {
            this._480 = arrayList;
        }

        public ArrayList<_720> get720() {
            return this._720;
        }

        public void set720(ArrayList<_720> arrayList) {
            this._720 = arrayList;
        }

        public ArrayList<DMVideo._1080> get1080() {
            return this._1080;
        }

        public void set1080(ArrayList<DMVideo._1080> arrayList) {
            this._1080 = arrayList;
        }

        public ArrayList<_288> get288() {
            return this._288;
        }

        public void set288(ArrayList<_288> arrayList) {
            this._288 = arrayList;
        }

        public ArrayList<DMVideo._112> get112() {
            return this._112;
        }

        public void set112(ArrayList<DMVideo._112> arrayList) {
            this._112 = arrayList;
        }

        public ArrayList<DMVideo._360> get360() {
            return this._360;
        }

        public void set360(ArrayList<DMVideo._360> arrayList) {
            this._360 = arrayList;
        }
    }

    public static class Auto {
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("url")
        @Expose
        private String url;

        public String getType() {
            return this.type;
        }

        public void setType(String str) {
            this.type = str;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String str) {
            this.url = str;
        }
    }

    public static class _720 {
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("url")
        @Expose
        private String url;

        public String getType() {
            return this.type;
        }

        public void setType(String str) {
            this.type = str;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String str) {
            this.url = str;
        }
    }

    public static class Info {
        @SerializedName("enable")
        @Expose
        private boolean enable;

        public boolean isEnable() {
            return this.enable;
        }

        public void setEnable(boolean z) {
            this.enable = z;
        }
    }

    public static class _288 {
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("url")
        @Expose
        private String url;

        public String getType() {
            return this.type;
        }

        public void setType(String str) {
            this.type = str;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String str) {
            this.url = str;
        }
    }

    public static class Timeout {
        @SerializedName("vast")
        @Expose
        private long vast;
        @SerializedName("video")
        @Expose
        private long video;

        public long getVast() {
            return this.vast;
        }

        public void setVast(long j) {
            this.vast = j;
        }

        public long getVideo() {
            return this.video;
        }

        public void setVideo(long j) {
            this.video = j;
        }
    }
}
