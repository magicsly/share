package com.share.model;

import java.util.Date;

public class share_project_info {
    private Integer piId;

    private Integer pid;

    private String sid;

    private String sname;

    private Float nowmuch;

    private Float usemuch;

    private Float costprice;

    private Date createtime;

    private Date endtime;

    private Byte type;

    private Byte isok;

    public Integer getPiId() {
        return piId;
    }

    public void setPiId(Integer piId) {
        this.piId = piId;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid == null ? null : sid.trim();
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname == null ? null : sname.trim();
    }

    public Float getNowmuch() {
        return nowmuch;
    }

    public void setNowmuch(Float nowmuch) {
        this.nowmuch = nowmuch;
    }

    public Float getCostprice() {
        return costprice;
    }

    public void setCostprice(Float costprice) {
        this.costprice = costprice;
    }

    public Float getUsemuch() {
        return usemuch;
    }

    public void setUsemuch(Float usemuch) {
        this.usemuch = usemuch;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getIsok() {
        return isok;
    }

    public void setIsok(Byte isok) {
        this.isok = isok;
    }
}