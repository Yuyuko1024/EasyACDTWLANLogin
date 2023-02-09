package org.yuyu.easylogin.bean;

public class BannerDataBean {

    int imgRes;
    String imgDesc;
    String imgUrl;

    public BannerDataBean() {}

    public BannerDataBean(int imgRes) {
        this.imgRes = imgRes;
    }

    public BannerDataBean(int imgRes, String imgUrl) {
        this.imgRes = imgRes;
        this.imgUrl = imgUrl;
    }

    public BannerDataBean(int imgRes, String imgDesc, String imgUrl) {
        this.imgRes = imgRes;
        this.imgDesc = imgDesc;
        this.imgUrl = imgUrl;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public String getImgDesc() {
        return imgDesc;
    }

    public void setImgDesc(String imgDesc) {
        this.imgDesc = imgDesc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
