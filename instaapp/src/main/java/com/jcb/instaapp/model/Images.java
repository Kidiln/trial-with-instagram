package com.jcb.instaapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Images implements Serializable {

    @SerializedName("low_resolution")
    @Expose
    private LowResolution_ lowResolution;
    @SerializedName("thumbnail")
    @Expose
    private Thumbnail thumbnail;
    @SerializedName("standard_resolution")
    @Expose
    private StandardResolution_ standardResolution;

    /**
     * @return The lowResolution
     */
    public LowResolution_ getLowResolution() {
        return lowResolution;
    }

    /**
     * @param lowResolution The low_resolution
     */
    public void setLowResolution(LowResolution_ lowResolution) {
        this.lowResolution = lowResolution;
    }

    /**
     * @return The thumbnail
     */
    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    /**
     * @param thumbnail The thumbnail
     */
    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * @return The standardResolution
     */
    public StandardResolution_ getStandardResolution() {
        return standardResolution;
    }

    /**
     * @param standardResolution The standard_resolution
     */
    public void setStandardResolution(StandardResolution_ standardResolution) {
        this.standardResolution = standardResolution;
    }

}
