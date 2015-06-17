package com.share.model;

import java.util.Date;

public class share_project_info {
    private Integer id;

    private Integer pid;

    private Float dayval;

    private Float weekval;

    private Float yearval;

    private Float allval;

    private Date creatime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Float getDayval() {
        return dayval;
    }

    public void setDayval(Float dayval) {
        this.dayval = dayval;
    }

    public Float getWeekval() {
        return weekval;
    }

    public void setWeekval(Float weekval) {
        this.weekval = weekval;
    }

    public Float getYearval() {
        return yearval;
    }

    public void setYearval(Float yearval) {
        this.yearval = yearval;
    }

    public Float getAllval() {
        return allval;
    }

    public void setAllval(Float allval) {
        this.allval = allval;
    }

    public Date getCreatime() {
        return creatime;
    }

    public void setCreatime(Date creatime) {
        this.creatime = creatime;
    }
}