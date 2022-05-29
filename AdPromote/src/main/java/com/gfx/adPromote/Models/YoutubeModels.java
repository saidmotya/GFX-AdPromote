package com.gfx.adPromote.Models;

/**
 * Created by SAID MOTYA on 01/01/2022.
 * contact on Facebook : https://web.facebook.com/motya.said
 * This library created specially for SecretGFX group & it free to used.
 */
public class YoutubeModels {

    private String title;
    private String icon;
    private String preview;
    private String watch;
    private String channelID;
    private String allModels ;

    public YoutubeModels(String title, String icon, String preview, String watch, String channelID) {
        this.title = title;
        this.icon = icon;
        this.preview = preview;
        this.watch = watch;
        this.channelID = channelID;
        this.allModels = title+icon+preview+watch+channelID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getWatch() {
        return watch;
    }

    public void setWatch(String watch) {
        this.watch = watch;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getAllModels() {
        return allModels;
    }

    public void setAllModels(String allModels) {
        this.allModels = allModels;
    }
}
