package com.share.model;

import java.util.Date;

public class share_project_adj {
    private Integer id;

    private Integer piId;

    private Float buymuch;

    private Float buymoney;

    private Date buytime;

    private Date updatetime;

    private Date suretime;

    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPiId() {
        return piId;
    }

    public void setPiId(Integer piId) {
        this.piId = piId;
    }

    public Float getBuymuch() {
        return buymuch;
    }

    public void setBuymuch(Float buymuch) {
        this.buymuch = buymuch;
    }

    public Float getBuymoney() {
        return buymoney;
    }

    public void setBuymoney(Float buymoney) {
        this.buymoney = buymoney;
    }

    public Date getBuytime() {
        return buytime;
    }

    public void setBuytime(Date buytime) {
        this.buytime = buytime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Date getSuretime() {
        return suretime;
    }

    public void setSuretime(Date suretime) {
        this.suretime = suretime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}