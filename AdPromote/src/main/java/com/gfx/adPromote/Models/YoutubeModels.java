package com.gfx.adPromote.Models;

/**
 * Created by SAID MOTYA on 01/01/2022.
 * contact on Facebook : https://web.facebook.com/motya.said
 * This library created specially for SecretGFX group & it free to used.
 */
public class YoutubeModels {

    private String title;
    private String icon;
    private String previewFull;
    private String previewSmall;
    private String watch;
    private String channelID;
    private String description;
    private String allModels;

    public YoutubeModels(String title, String icon, String previewFull, String previewSmall, String watch, String channelID,String description) {
        this.title = title;
        this.icon = icon;
        this.previewFull = previewFull;
        this.previewSmall = previewSmall;
        this.watch = watch;
        this.channelID = channelID;
        this.description = description ;
        this.allModels = title + icon + previewFull + previewSmall + watch + channelID+description;
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

    public String getPreviewFull() {
        return previewFull;
    }

    public void setPreviewFull(String previewFull) {
        this.previewFull = previewFull;
    }

    public String getPreviewSmall() {
        return previewSmall;
    }

    public void setPreviewSmall(String previewSmall) {
        this.previewSmall = previewSmall;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
