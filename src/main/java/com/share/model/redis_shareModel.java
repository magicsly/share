package com.share.model;

/**
 * Created by ElNino on 15/7/3.
 */
public class redis_shareModel {
    private String cname;

    private String shortchar;

    private  String code;

    private float yesterdayprice;

    private float liveprice;

    private String updatetime;

    private Integer state;

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCname() {
        return cname;
    }

    public void setShortchar(String shortchar){
        this.shortchar = shortchar;
    }

    public String getShortchar(){
        return shortchar;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }

    public void setLiveprice(float liveprice){
        this.liveprice = liveprice;
    }

    public float getLiveprice(){
        return liveprice;
    }

    public void setYesterdayprice(float yesterdayprice){
        this.yesterdayprice = yesterdayprice;
    }

    public float getYesterdayprice(){
        return yesterdayprice;
    }

    public void setUpdatetime(String updatetime){
        this.updatetime = updatetime;
    }

    public String getUpdatetime(){
        return updatetime;
    }

    public void setState(Integer state){
        this.state = state;
    }

    public Integer getState(){
        return  state;
    }

}
