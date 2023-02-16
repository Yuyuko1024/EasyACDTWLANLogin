package org.yuyu.easylogin.bean;

public class BannerDataBean {

    int imgRes;
    String imgUrl;
    String imgTitle;
    String imgDesc;

    public BannerDataBean() {}

    public BannerDataBean(int imgRes) {
        this.imgRes = imgRes;
    }

    public BannerDataBean(int imgRes, String imgUrl) {
        this.imgRes = imgRes;
        this.imgUrl = imgUrl;
    }

    public BannerDataBean(int imgRes, String imgUrl, String imgTitle){
        this.imgRes = imgRes;
        this.imgUrl = imgUrl;
        this.imgTitle = imgTitle;
    }

    public BannerDataBean(int imgRes, String imgUrl, String imgTitle, String imgDesc){
        this.imgRes = imgRes;
        this.imgUrl = imgUrl;
        this.imgTitle = imgTitle;
        this.imgDesc = imgDesc;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgTitle() {
        return imgTitle;
    }

    public void setImgTitle(String imgTitle) {
        this.imgTitle = imgTitle;
    }

    public String getImgDesc() {
        return imgDesc;
    }

    public void setImgDesc(String imgDesc) {
        this.imgDesc = imgDesc;
    }
}
