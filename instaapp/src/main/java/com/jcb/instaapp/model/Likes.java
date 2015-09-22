package com.jcb.instaapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Likes implements Serializable {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("data")
    @Expose
    private List<Object> data = new ArrayList<Object>();

    /**
     * @return The count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * @param count The count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * @return The data
     */
    public List<Object> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<Object> data) {
        this.data = data;
    }

}
