
package com.retrytech.medialoot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class VimeoModels {

    @SerializedName("cdn_url")
    private String cdnUrl;
    @Expose
    private Embed embed;
    @SerializedName("player_url")
    private String playerUrl;
    @Expose
    private Request request;
    @Expose
    private User user;
    @Expose
    private Video video;
    @Expose
    private Long view;
    @SerializedName("vimeo_api_url")
    private String vimeoApiUrl;
    @SerializedName("vimeo_url")
    private String vimeoUrl;

    public String getCdnUrl() {
        return cdnUrl;
    }

    public void setCdnUrl(String cdnUrl) {
        this.cdnUrl = cdnUrl;
    }

    public Embed getEmbed() {
        return embed;
    }

    public void setEmbed(Embed embed) {
        this.embed = embed;
    }

    public String getPlayerUrl() {
        return playerUrl;
    }

    public void setPlayerUrl(String playerUrl) {
        this.playerUrl = playerUrl;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Long getView() {
        return view;
    }

    public void setView(Long view) {
        this.view = view;
    }

    public String getVimeoApiUrl() {
        return vimeoApiUrl;
    }

    public void setVimeoApiUrl(String vimeoApiUrl) {
        this.vimeoApiUrl = vimeoApiUrl;
    }

    public String getVimeoUrl() {
        return vimeoUrl;
    }

    public void setVimeoUrl(String vimeoUrl) {
        this.vimeoUrl = vimeoUrl;
    }


    @SuppressWarnings("unused")
    public static class Sentry {

        @SerializedName("debug_enabled")
        private Boolean debugEnabled;
        @SerializedName("debug_intent")
        private Long debugIntent;
        @Expose
        private Boolean enabled;
        @Expose
        private String url;

        public Boolean getDebugEnabled() {
            return debugEnabled;
        }

        public void setDebugEnabled(Boolean debugEnabled) {
            this.debugEnabled = debugEnabled;
        }

        public Long getDebugIntent() {
            return debugIntent;
        }

        public void setDebugIntent(Long debugIntent) {
            this.debugIntent = debugIntent;
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }


    @SuppressWarnings("unused")
    public static class Owner {

        @SerializedName("account_type")
        private String accountType;
        @Expose
        private Long id;
        @Expose
        private String img;
        @SerializedName("img_2x")
        private String img2X;
        @Expose
        private String name;
        @Expose
        private String url;

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getImg2X() {
            return img2X;
        }

        public void setImg2X(String img2X) {
            this.img2X = img2X;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }


    @SuppressWarnings("unused")
    public static class Settings {

//        @Expose
//        private Badge badge;
        @Expose
        private Long byline;
        @Expose
        private Long collections;
        @Expose
        private Long color;
        @Expose
        private Long embed;
        @Expose
        private Long fullscreen;
        @SerializedName("info_on_pause")
        private Long infoOnPause;
        @Expose
        private Long like;
        @Expose
        private Long logo;
        @Expose
        private Long playbar;
        @Expose
        private Long portrait;
        @Expose
        private Long scaling;
        @Expose
        private Long share;
        @SerializedName("spatial_compass")
        private Long spatialCompass;
        @SerializedName("spatial_label")
        private Long spatialLabel;
        @Expose
        private Long speed;
        @Expose
        private Long title;
        @Expose
        private Long volume;
        @SerializedName("watch_later")
        private Long watchLater;

//        public Badge getBadge() {
//            return badge;
//        }
//
//        public void setBadge(Badge badge) {
//            this.badge = badge;
//        }

        public Long getByline() {
            return byline;
        }

        public void setByline(Long byline) {
            this.byline = byline;
        }

        public Long getCollections() {
            return collections;
        }

        public void setCollections(Long collections) {
            this.collections = collections;
        }

        public Long getColor() {
            return color;
        }

        public void setColor(Long color) {
            this.color = color;
        }

        public Long getEmbed() {
            return embed;
        }

        public void setEmbed(Long embed) {
            this.embed = embed;
        }

        public Long getFullscreen() {
            return fullscreen;
        }

        public void setFullscreen(Long fullscreen) {
            this.fullscreen = fullscreen;
        }

        public Long getInfoOnPause() {
            return infoOnPause;
        }

        public void setInfoOnPause(Long infoOnPause) {
            this.infoOnPause = infoOnPause;
        }

        public Long getLike() {
            return like;
        }

        public void setLike(Long like) {
            this.like = like;
        }

        public Long getLogo() {
            return logo;
        }

        public void setLogo(Long logo) {
            this.logo = logo;
        }

        public Long getPlaybar() {
            return playbar;
        }

        public void setPlaybar(Long playbar) {
            this.playbar = playbar;
        }

        public Long getPortrait() {
            return portrait;
        }

        public void setPortrait(Long portrait) {
            this.portrait = portrait;
        }

        public Long getScaling() {
            return scaling;
        }

        public void setScaling(Long scaling) {
            this.scaling = scaling;
        }

        public Long getShare() {
            return share;
        }

        public void setShare(Long share) {
            this.share = share;
        }

        public Long getSpatialCompass() {
            return spatialCompass;
        }

        public void setSpatialCompass(Long spatialCompass) {
            this.spatialCompass = spatialCompass;
        }

        public Long getSpatialLabel() {
            return spatialLabel;
        }

        public void setSpatialLabel(Long spatialLabel) {
            this.spatialLabel = spatialLabel;
        }

        public Long getSpeed() {
            return speed;
        }

        public void setSpeed(Long speed) {
            this.speed = speed;
        }

        public Long getTitle() {
            return title;
        }

        public void setTitle(Long title) {
            this.title = title;
        }

        public Long getVolume() {
            return volume;
        }

        public void setVolume(Long volume) {
            this.volume = volume;
        }

        public Long getWatchLater() {
            return watchLater;
        }

        public void setWatchLater(Long watchLater) {
            this.watchLater = watchLater;
        }

    }


    @SuppressWarnings("unused")
    public static class CdnPreference {

        @Expose
        private Data data;
        @Expose
        private Boolean group;
        @Expose
        private Boolean track;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public Boolean getGroup() {
            return group;
        }

        public void setGroup(Boolean group) {
            this.group = group;
        }

        public Boolean getTrack() {
            return track;
        }

        public void setTrack(Boolean track) {
            this.track = track;
        }

    }


    @SuppressWarnings("unused")
    public static class AkfireInterconnectQuic {

        @Expose
        private String origin;
        @Expose
        private String url;

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }


    @SuppressWarnings("unused")
    public static class Cdns {

        @SerializedName("akfire_interconnect_quic")
        private AkfireInterconnectQuic akfireInterconnectQuic;
        @SerializedName("fastly_skyfire")
        private FastlySkyfire fastlySkyfire;

        public AkfireInterconnectQuic getAkfireInterconnectQuic() {
            return akfireInterconnectQuic;
        }

        public void setAkfireInterconnectQuic(AkfireInterconnectQuic akfireInterconnectQuic) {
            this.akfireInterconnectQuic = akfireInterconnectQuic;
        }

        public FastlySkyfire getFastlySkyfire() {
            return fastlySkyfire;
        }

        public void setFastlySkyfire(FastlySkyfire fastlySkyfire) {
            this.fastlySkyfire = fastlySkyfire;
        }

    }


    @SuppressWarnings("unused")
    public static class User {

        @SerializedName("account_type")
        private String accountType;
        @Expose
        private Long id;
        @Expose
        private Long liked;
        @SerializedName("logged_in")
        private Long loggedIn;
        @Expose
        private Long mod;
        @Expose
        private Long owner;
        @SerializedName("team_id")
        private Long teamId;
        @SerializedName("team_origin_user_id")
        private Long teamOriginUserId;
        @SerializedName("vimeo_api_client_token")
        private Object vimeoApiClientToken;
        @SerializedName("vimeo_api_interaction_tokens")
        private Object vimeoApiInteractionTokens;
        @SerializedName("watch_later")
        private Long watchLater;

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getLiked() {
            return liked;
        }

        public void setLiked(Long liked) {
            this.liked = liked;
        }

        public Long getLoggedIn() {
            return loggedIn;
        }

        public void setLoggedIn(Long loggedIn) {
            this.loggedIn = loggedIn;
        }

        public Long getMod() {
            return mod;
        }

        public void setMod(Long mod) {
            this.mod = mod;
        }

        public Long getOwner() {
            return owner;
        }

        public void setOwner(Long owner) {
            this.owner = owner;
        }

        public Long getTeamId() {
            return teamId;
        }

        public void setTeamId(Long teamId) {
            this.teamId = teamId;
        }

        public Long getTeamOriginUserId() {
            return teamOriginUserId;
        }

        public void setTeamOriginUserId(Long teamOriginUserId) {
            this.teamOriginUserId = teamOriginUserId;
        }

        public Object getVimeoApiClientToken() {
            return vimeoApiClientToken;
        }

        public void setVimeoApiClientToken(Object vimeoApiClientToken) {
            this.vimeoApiClientToken = vimeoApiClientToken;
        }

        public Object getVimeoApiInteractionTokens() {
            return vimeoApiInteractionTokens;
        }

        public void setVimeoApiInteractionTokens(Object vimeoApiInteractionTokens) {
            this.vimeoApiInteractionTokens = vimeoApiInteractionTokens;
        }

        public Long getWatchLater() {
            return watchLater;
        }

        public void setWatchLater(Long watchLater) {
            this.watchLater = watchLater;
        }

    }


    @SuppressWarnings("unused")
    public static class Embed {

        @Expose
        private Object api;
        @SerializedName("app_id")
        private String appId;
        @Expose
        private Long autopause;
        @Expose
        private Long autoplay;
        @Expose
        private String color;
        @Expose
        private String context;
        @Expose
        private Long dnt;
        @Expose
        private Boolean editor;
        @SerializedName("log_plays")
        private Long logPlays;
        @Expose
        private Long loop;
        @Expose
        private Long muted;
        @SerializedName("on_site")
        private Long onSite;
        @Expose
        private String outro;
        @SerializedName("player_id")
        private String playerId;
        @Expose
        private Long playsinline;
        @Expose
        private Object quality;
        @Expose
        private Settings settings;
        @Expose
        private String texttrack;
        @Expose
        private Long time;
        @Expose
        private Long transparent;

        public Object getApi() {
            return api;
        }

        public void setApi(Object api) {
            this.api = api;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public Long getAutopause() {
            return autopause;
        }

        public void setAutopause(Long autopause) {
            this.autopause = autopause;
        }

        public Long getAutoplay() {
            return autoplay;
        }

        public void setAutoplay(Long autoplay) {
            this.autoplay = autoplay;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public Long getDnt() {
            return dnt;
        }

        public void setDnt(Long dnt) {
            this.dnt = dnt;
        }

        public Boolean getEditor() {
            return editor;
        }

        public void setEditor(Boolean editor) {
            this.editor = editor;
        }

        public Long getLogPlays() {
            return logPlays;
        }

        public void setLogPlays(Long logPlays) {
            this.logPlays = logPlays;
        }

        public Long getLoop() {
            return loop;
        }

        public void setLoop(Long loop) {
            this.loop = loop;
        }

        public Long getMuted() {
            return muted;
        }

        public void setMuted(Long muted) {
            this.muted = muted;
        }

        public Long getOnSite() {
            return onSite;
        }

        public void setOnSite(Long onSite) {
            this.onSite = onSite;
        }

        public String getOutro() {
            return outro;
        }

        public void setOutro(String outro) {
            this.outro = outro;
        }

        public String getPlayerId() {
            return playerId;
        }

        public void setPlayerId(String playerId) {
            this.playerId = playerId;
        }

        public Long getPlaysinline() {
            return playsinline;
        }

        public void setPlaysinline(Long playsinline) {
            this.playsinline = playsinline;
        }

        public Object getQuality() {
            return quality;
        }

        public void setQuality(Object quality) {
            this.quality = quality;
        }

        public Settings getSettings() {
            return settings;
        }

        public void setSettings(Settings settings) {
            this.settings = settings;
        }

        public String getTexttrack() {
            return texttrack;
        }

        public void setTexttrack(String texttrack) {
            this.texttrack = texttrack;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }

        public Long getTransparent() {
            return transparent;
        }

        public void setTransparent(Long transparent) {
            this.transparent = transparent;
        }

    }


    @SuppressWarnings("unused")
    public static class Badge {

//        @SerializedName("height")
//        @Expose
//        private Long height;
//        @SerializedName("id")
//        @Expose
//        private Long id;
//        @SerializedName("img")
//        @Expose
//        private String img;
//        @SerializedName("img_2x")
//        @Expose
//        private String img2X;
//        @SerializedName("link")
//        @Expose
//        private String link;
//        @SerializedName("name")
//        @Expose
//        private String name;
//        @SerializedName("svg")
//        @Expose
//        private String svg;
//        @SerializedName("width")
//        @Expose
//        private Long width;
//
//        public Long getHeight() {
//            return height;
//        }
//
//        public void setHeight(Long height) {
//            this.height = height;
//        }
//
//        public Long getId() {
//            return id;
//        }
//
//        public void setId(Long id) {
//            this.id = id;
//        }
//
//        public String getImg() {
//            return img;
//        }
//
//        public void setImg(String img) {
//            this.img = img;
//        }
//
//        public String getImg2X() {
//            return img2X;
//        }
//
//        public void setImg2X(String img2X) {
//            this.img2X = img2X;
//        }
//
//        public String getLink() {
//            return link;
//        }
//
//        public void setLink(String link) {
//            this.link = link;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getSvg() {
//            return svg;
//        }
//
//        public void setSvg(String svg) {
//            this.svg = svg;
//        }
//
//        public Long getWidth() {
//            return width;
//        }
//
//        public void setWidth(Long width) {
//            this.width = width;
//        }

    }


    @SuppressWarnings("unused")
    public static class Hevc {

        @Expose
        private List<Object> hdr;
        @Expose
        private List<Object> sdr;

        public List<Object> getHdr() {
            return hdr;
        }

        public void setHdr(List<Object> hdr) {
            this.hdr = hdr;
        }

        public List<Object> getSdr() {
            return sdr;
        }

        public void setSdr(List<Object> sdr) {
            this.sdr = sdr;
        }

    }


    @SuppressWarnings("unused")
    public static class Flags {

        @SerializedName("autohide_controls")
        private Long autohideControls;
        @Expose
        private Long dnt;
        @Expose
        private Long partials;
        @Expose
        private Long plays;
        @SerializedName("preload_video")
        private String preloadVideo;

        public Long getAutohideControls() {
            return autohideControls;
        }

        public void setAutohideControls(Long autohideControls) {
            this.autohideControls = autohideControls;
        }

        public Long getDnt() {
            return dnt;
        }

        public void setDnt(Long dnt) {
            this.dnt = dnt;
        }

        public Long getPartials() {
            return partials;
        }

        public void setPartials(Long partials) {
            this.partials = partials;
        }

        public Long getPlays() {
            return plays;
        }

        public void setPlays(Long plays) {
            this.plays = plays;
        }

        public String getPreloadVideo() {
            return preloadVideo;
        }

        public void setPreloadVideo(String preloadVideo) {
            this.preloadVideo = preloadVideo;
        }

    }


    @SuppressWarnings("unused")
    public static class GcDebug {

        @Expose
        private String bucket;

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

    }


    @SuppressWarnings("unused")
    public static class Chromecast {

        @Expose
        private Data data;
        @Expose
        private Boolean group;
        @Expose
        private Boolean track;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public Boolean getGroup() {
            return group;
        }

        public void setGroup(Boolean group) {
            this.group = group;
        }

        public Boolean getTrack() {
            return track;
        }

        public void setTrack(Boolean track) {
            this.track = track;
        }

    }


    @SuppressWarnings("unused")
    public static class FileCodecs {

        @Expose
        private List<Object> av1;
        @Expose
        private List<Long> avc;
        @Expose
        private Hevc hevc;

        public List<Object> getAv1() {
            return av1;
        }

        public void setAv1(List<Object> av1) {
            this.av1 = av1;
        }

        public List<Long> getAvc() {
            return avc;
        }

        public void setAvc(List<Long> avc) {
            this.avc = avc;
        }

        public Hevc getHevc() {
            return hevc;
        }

        public void setHevc(Hevc hevc) {
            this.hevc = hevc;
        }

    }


    @SuppressWarnings("unused")
    public static class FastlySkyfire {

        @Expose
        private String origin;
        @Expose
        private String url;

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }


    @SuppressWarnings("unused")
    public static class AbTests {

        @SerializedName("cdn_preference")
        private CdnPreference cdnPreference;
        @Expose
        private Chromecast chromecast;

        public CdnPreference getCdnPreference() {
            return cdnPreference;
        }

        public void setCdnPreference(CdnPreference cdnPreference) {
            this.cdnPreference = cdnPreference;
        }

        public Chromecast getChromecast() {
            return chromecast;
        }

        public void setChromecast(Chromecast chromecast) {
            this.chromecast = chromecast;
        }

    }


    @SuppressWarnings("unused")
    public static class Rating {

        @Expose
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

    }


    @SuppressWarnings("unused")
    public static class Progressive {

        @Expose
        private String cdn;
        @Expose
        private Long fps;
        @Expose
        private Long height;
        @Expose
        private Long id;
        @Expose
        private String mime;
        @Expose
        private String origin;
        @Expose
        private Long profile;
        @Expose
        private String quality;
        @Expose
        private String url;
        @Expose
        private Long width;

        public String getCdn() {
            return cdn;
        }

        public void setCdn(String cdn) {
            this.cdn = cdn;
        }

        public Long getFps() {
            return fps;
        }

        public void setFps(Long fps) {
            this.fps = fps;
        }

        public Long getHeight() {
            return height;
        }

        public void setHeight(Long height) {
            this.height = height;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getMime() {
            return mime;
        }

        public void setMime(String mime) {
            this.mime = mime;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public Long getProfile() {
            return profile;
        }

        public void setProfile(Long profile) {
            this.profile = profile;
        }

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Long getWidth() {
            return width;
        }

        public void setWidth(Long width) {
            this.width = width;
        }

    }


    @SuppressWarnings("unused")
    public static class Available {

        @SerializedName("file_id")
        private Long fileId;
        @Expose
        private Long id;
        @SerializedName("is_current")
        private Long isCurrent;

        public Long getFileId() {
            return fileId;
        }

        public void setFileId(Long fileId) {
            this.fileId = fileId;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getIsCurrent() {
            return isCurrent;
        }

        public void setIsCurrent(Long isCurrent) {
            this.isCurrent = isCurrent;
        }

    }


    @SuppressWarnings("unused")
    public static class Files {

        @Expose
        private Dash dash;
        @Expose
        private Hls hls;
        @Expose
        private List<Progressive> progressive;

        public Dash getDash() {
            return dash;
        }

        public void setDash(Dash dash) {
            this.dash = dash;
        }

        public Hls getHls() {
            return hls;
        }

        public void setHls(Hls hls) {
            this.hls = hls;
        }

        public List<Progressive> getProgressive() {
            return progressive;
        }

        public void setProgressive(List<Progressive> progressive) {
            this.progressive = progressive;
        }

    }


    @SuppressWarnings("unused")
    public static class Video {

        @SerializedName("allow_hd")
        private Long allowHd;
        @SerializedName("bypass_token")
        private String bypassToken;
        @SerializedName("default_to_hd")
        private Long defaultToHd;
        @Expose
        private Long duration;
        @SerializedName("embed_code")
        private String embedCode;
        @SerializedName("embed_permission")
        private String embedPermission;
        @Expose
        private Double fps;
        @Expose
        private Long hd;
        @Expose
        private Long height;
        @Expose
        private Long id;
        @Expose
        private Object lang;
        @SerializedName("live_event")
        private Object liveEvent;
        @Expose
        private Owner owner;
        @Expose
        private String privacy;
        @Expose
        private Rating rating;
        @SerializedName("share_url")
        private String shareUrl;
        @Expose
        private Long spatial;
        @Expose
        private Thumbs thumbs;
        @Expose
        private String title;
        @SerializedName("unlisted_hash")
        private Object unlistedHash;
        @Expose
        private String url;
        @Expose
        private Version version;
        @Expose
        private Long width;

        public Long getAllowHd() {
            return allowHd;
        }

        public void setAllowHd(Long allowHd) {
            this.allowHd = allowHd;
        }

        public String getBypassToken() {
            return bypassToken;
        }

        public void setBypassToken(String bypassToken) {
            this.bypassToken = bypassToken;
        }

        public Long getDefaultToHd() {
            return defaultToHd;
        }

        public void setDefaultToHd(Long defaultToHd) {
            this.defaultToHd = defaultToHd;
        }

        public Long getDuration() {
            return duration;
        }

        public void setDuration(Long duration) {
            this.duration = duration;
        }

        public String getEmbedCode() {
            return embedCode;
        }

        public void setEmbedCode(String embedCode) {
            this.embedCode = embedCode;
        }

        public String getEmbedPermission() {
            return embedPermission;
        }

        public void setEmbedPermission(String embedPermission) {
            this.embedPermission = embedPermission;
        }

        public Double getFps() {
            return fps;
        }

        public void setFps(Double fps) {
            this.fps = fps;
        }

        public Long getHd() {
            return hd;
        }

        public void setHd(Long hd) {
            this.hd = hd;
        }

        public Long getHeight() {
            return height;
        }

        public void setHeight(Long height) {
            this.height = height;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Object getLang() {
            return lang;
        }

        public void setLang(Object lang) {
            this.lang = lang;
        }

        public Object getLiveEvent() {
            return liveEvent;
        }

        public void setLiveEvent(Object liveEvent) {
            this.liveEvent = liveEvent;
        }

        public Owner getOwner() {
            return owner;
        }

        public void setOwner(Owner owner) {
            this.owner = owner;
        }

        public String getPrivacy() {
            return privacy;
        }

        public void setPrivacy(String privacy) {
            this.privacy = privacy;
        }

        public Rating getRating() {
            return rating;
        }

        public void setRating(Rating rating) {
            this.rating = rating;
        }

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }

        public Long getSpatial() {
            return spatial;
        }

        public void setSpatial(Long spatial) {
            this.spatial = spatial;
        }

        public Thumbs getThumbs() {
            return thumbs;
        }

        public void setThumbs(Thumbs thumbs) {
            this.thumbs = thumbs;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Object getUnlistedHash() {
            return unlistedHash;
        }

        public void setUnlistedHash(Object unlistedHash) {
            this.unlistedHash = unlistedHash;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Version getVersion() {
            return version;
        }

        public void setVersion(Version version) {
            this.version = version;
        }

        public Long getWidth() {
            return width;
        }

        public void setWidth(Long width) {
            this.width = width;
        }

    }


    @SuppressWarnings("unused")
    public static class Urls {

        @SerializedName("barebone_js")
        private String bareboneJs;
        @SerializedName("chromeless_css")
        private String chromelessCss;
        @SerializedName("chromeless_js")
        private String chromelessJs;
        @Expose
        private String css;
        @Expose
        private String fresnel;
        @SerializedName("fresnel_chunk_url")
        private String fresnelChunkUrl;
        @SerializedName("fresnel_manifest_url")
        private String fresnelManifestUrl;
        @Expose
        private String js;
        @SerializedName("mux_url")
        private String muxUrl;
        @Expose
        private String proxy;
        @SerializedName("sentry_url")
        private String sentryUrl;
        @SerializedName("test_imp")
        private String testImp;
        @SerializedName("three_js")
        private String threeJs;
        @SerializedName("vuid_js")
        private String vuidJs;

        public String getBareboneJs() {
            return bareboneJs;
        }

        public void setBareboneJs(String bareboneJs) {
            this.bareboneJs = bareboneJs;
        }

        public String getChromelessCss() {
            return chromelessCss;
        }

        public void setChromelessCss(String chromelessCss) {
            this.chromelessCss = chromelessCss;
        }

        public String getChromelessJs() {
            return chromelessJs;
        }

        public void setChromelessJs(String chromelessJs) {
            this.chromelessJs = chromelessJs;
        }

        public String getCss() {
            return css;
        }

        public void setCss(String css) {
            this.css = css;
        }

        public String getFresnel() {
            return fresnel;
        }

        public void setFresnel(String fresnel) {
            this.fresnel = fresnel;
        }

        public String getFresnelChunkUrl() {
            return fresnelChunkUrl;
        }

        public void setFresnelChunkUrl(String fresnelChunkUrl) {
            this.fresnelChunkUrl = fresnelChunkUrl;
        }

        public String getFresnelManifestUrl() {
            return fresnelManifestUrl;
        }

        public void setFresnelManifestUrl(String fresnelManifestUrl) {
            this.fresnelManifestUrl = fresnelManifestUrl;
        }

        public String getJs() {
            return js;
        }

        public void setJs(String js) {
            this.js = js;
        }

        public String getMuxUrl() {
            return muxUrl;
        }

        public void setMuxUrl(String muxUrl) {
            this.muxUrl = muxUrl;
        }

        public String getProxy() {
            return proxy;
        }

        public void setProxy(String proxy) {
            this.proxy = proxy;
        }

        public String getSentryUrl() {
            return sentryUrl;
        }

        public void setSentryUrl(String sentryUrl) {
            this.sentryUrl = sentryUrl;
        }

        public String getTestImp() {
            return testImp;
        }

        public void setTestImp(String testImp) {
            this.testImp = testImp;
        }

        public String getThreeJs() {
            return threeJs;
        }

        public void setThreeJs(String threeJs) {
            this.threeJs = threeJs;
        }

        public String getVuidJs() {
            return vuidJs;
        }

        public void setVuidJs(String vuidJs) {
            this.vuidJs = vuidJs;
        }

    }


    @SuppressWarnings("unused")
    public static class Stream {

        @Expose
        private Long fps;
        @Expose
        private Long id;
        @Expose
        private Long profile;
        @Expose
        private String quality;

        public Long getFps() {
            return fps;
        }

        public void setFps(Long fps) {
            this.fps = fps;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getProfile() {
            return profile;
        }

        public void setProfile(Long profile) {
            this.profile = profile;
        }

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

    }

    @SuppressWarnings("unused")
    public static class Thumbs {
        @SerializedName("1280")
        @Expose
        private String _1280;
        @SerializedName("640")
        @Expose
        private String _640;
        @SerializedName("960")
        @Expose
        private String _960;
        @SerializedName("base")
        @Expose
        private String base;

        public String get1280() {
            return this._1280;
        }

        public void set1280(String str) {
            this._1280 = str;
        }

        public String get960() {
            return this._960;
        }

        public void set960(String str) {
            this._960 = str;
        }

        public String get640() {
            return this._640;
        }

        public void set640(String str) {
            this._640 = str;
        }

        public String getBase() {
            return this.base;
        }

        public void setBase(String str) {
            this.base = str;
        }
    }



    @SuppressWarnings("unused")
    public static class Version {

        @Expose
        private List<Available> available;
        @Expose
        private Object current;

        public List<Available> getAvailable() {
            return available;
        }

        public void setAvailable(List<Available> available) {
            this.available = available;
        }

        public Object getCurrent() {
            return current;
        }

        public void setCurrent(Object current) {
            this.current = current;
        }

    }


    @SuppressWarnings("unused")
    public static class Cookie {

        @Expose
        private Object captions;
        @Expose
        private Long hd;
        @Expose
        private Object quality;
        @Expose
        private Long scaling;
        @Expose
        private Long volume;

        public Object getCaptions() {
            return captions;
        }

        public void setCaptions(Object captions) {
            this.captions = captions;
        }

        public Long getHd() {
            return hd;
        }

        public void setHd(Long hd) {
            this.hd = hd;
        }

        public Object getQuality() {
            return quality;
        }

        public void setQuality(Object quality) {
            this.quality = quality;
        }

        public Long getScaling() {
            return scaling;
        }

        public void setScaling(Long scaling) {
            this.scaling = scaling;
        }

        public Long getVolume() {
            return volume;
        }

        public void setVolume(Long volume) {
            this.volume = volume;
        }

    }


    @SuppressWarnings("unused")
    public static class Hls {

        @Expose
        private Cdns cdns;
        @SerializedName("default_cdn")
        private String defaultCdn;
        @SerializedName("separate_av")
        private Boolean separateAv;

        public Cdns getCdns() {
            return cdns;
        }

        public void setCdns(Cdns cdns) {
            this.cdns = cdns;
        }

        public String getDefaultCdn() {
            return defaultCdn;
        }

        public void setDefaultCdn(String defaultCdn) {
            this.defaultCdn = defaultCdn;
        }

        public Boolean getSeparateAv() {
            return separateAv;
        }

        public void setSeparateAv(Boolean separateAv) {
            this.separateAv = separateAv;
        }

    }


    @SuppressWarnings("unused")
    public static class Data {

        @Expose
        private String city;
        @SerializedName("country_code")
        private String countryCode;
        @SerializedName("dash_pref_found")
        private Boolean dashPrefFound;
        @SerializedName("hls_pref_found")
        private Boolean hlsPrefFound;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public Boolean getDashPrefFound() {
            return dashPrefFound;
        }

        public void setDashPrefFound(Boolean dashPrefFound) {
            this.dashPrefFound = dashPrefFound;
        }

        public Boolean getHlsPrefFound() {
            return hlsPrefFound;
        }

        public void setHlsPrefFound(Boolean hlsPrefFound) {
            this.hlsPrefFound = hlsPrefFound;
        }

    }


    @SuppressWarnings("unused")
    public static class Dash {

        @Expose
        private Cdns cdns;
        @SerializedName("default_cdn")
        private String defaultCdn;
        @SerializedName("separate_av")
        private Boolean separateAv;
        @Expose
        private List<Stream> streams;

        public Cdns getCdns() {
            return cdns;
        }

        public void setCdns(Cdns cdns) {
            this.cdns = cdns;
        }

        public String getDefaultCdn() {
            return defaultCdn;
        }

        public void setDefaultCdn(String defaultCdn) {
            this.defaultCdn = defaultCdn;
        }

        public Boolean getSeparateAv() {
            return separateAv;
        }

        public void setSeparateAv(Boolean separateAv) {
            this.separateAv = separateAv;
        }

        public List<Stream> getStreams() {
            return streams;
        }

        public void setStreams(List<Stream> streams) {
            this.streams = streams;
        }

    }


    @SuppressWarnings("unused")
    public static class Build {

        @Expose
        private String backend;
        @Expose
        private String js;

        public String getBackend() {
            return backend;
        }

        public void setBackend(String backend) {
            this.backend = backend;
        }

        public String getJs() {
            return js;
        }

        public void setJs(String js) {
            this.js = js;
        }

    }


    @SuppressWarnings("unused")
    public static class Request {

        @SerializedName("ab_tests")
        private AbTests abTests;
        @Expose
        private Build build;
        @Expose
        private Cookie cookie;
        @SerializedName("cookie_domain")
        private String cookieDomain;
        @Expose
        private String country;
        @Expose
        private String currency;
        @Expose
        private Long expires;
        @SerializedName("file_codecs")
        private FileCodecs fileCodecs;
        @Expose
        private Files files;
        @Expose
        private Flags flags;
        @SerializedName("gc_debug")
        private GcDebug gcDebug;
        @Expose
        private String lang;
        @Expose
        private Object referrer;
        @Expose
        private Sentry sentry;
        @Expose
        private String session;
        @Expose
        private String signature;
        @Expose
        private Long timestamp;
        @Expose
        private Urls urls;

        public AbTests getAbTests() {
            return abTests;
        }

        public void setAbTests(AbTests abTests) {
            this.abTests = abTests;
        }

        public Build getBuild() {
            return build;
        }

        public void setBuild(Build build) {
            this.build = build;
        }

        public Cookie getCookie() {
            return cookie;
        }

        public void setCookie(Cookie cookie) {
            this.cookie = cookie;
        }

        public String getCookieDomain() {
            return cookieDomain;
        }

        public void setCookieDomain(String cookieDomain) {
            this.cookieDomain = cookieDomain;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Long getExpires() {
            return expires;
        }

        public void setExpires(Long expires) {
            this.expires = expires;
        }

        public FileCodecs getFileCodecs() {
            return fileCodecs;
        }

        public void setFileCodecs(FileCodecs fileCodecs) {
            this.fileCodecs = fileCodecs;
        }

        public Files getFiles() {
            return files;
        }

        public void setFiles(Files files) {
            this.files = files;
        }

        public Flags getFlags() {
            return flags;
        }

        public void setFlags(Flags flags) {
            this.flags = flags;
        }

        public GcDebug getGcDebug() {
            return gcDebug;
        }

        public void setGcDebug(GcDebug gcDebug) {
            this.gcDebug = gcDebug;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public Object getReferrer() {
            return referrer;
        }

        public void setReferrer(Object referrer) {
            this.referrer = referrer;
        }

        public Sentry getSentry() {
            return sentry;
        }

        public void setSentry(Sentry sentry) {
            this.sentry = sentry;
        }

        public String getSession() {
            return session;
        }

        public void setSession(String session) {
            this.session = session;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        public Urls getUrls() {
            return urls;
        }

        public void setUrls(Urls urls) {
            this.urls = urls;
        }

    }
}
