package com.share.model;

import java.util.Date;

public class share_project_value {
    private Integer id;

    private Integer pid;

    private Float dayval;

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

    public Date getCreatime() {
        return creatime;
    }

    public void setCreatime(Date creatime) {
        this.creatime = creatime;
    }
}