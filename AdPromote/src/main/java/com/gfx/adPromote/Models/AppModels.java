package com.gfx.adPromote.Models;
/**
 * Created by SAID MOTYA on 01/01/2022.
 * contact on Facebook : https://web.facebook.com/motya.said
 * This library created specially for SecretGFX group & it free to used.
 */
public class AppModels {

    private String name;
    private String icons;
    private String shortDescription;
    private String packageName;
    private String appPreview;
    private String allModels;

    public AppModels(String name, String icons, String shortDescription, String packageName,String appPreview) {
        this.name = name;
        this.icons = icons;
        this.shortDescription = shortDescription;
        this.packageName = packageName;
        this.appPreview = appPreview ;
        this.allModels = name + icons + shortDescription + packageName + appPreview ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcons() {
        return icons;
    }

    public void setIcons(String icons) {
        this.icons = icons;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAllModels() {
        return allModels;
    }

    public void setAllModels(String allModels) {
        this.allModels = allModels;
    }

    public String getAppPreview() {
        return appPreview;
    }

    public void setAppPreview(String appPreview) {
        this.appPreview = appPreview;
    }
}
