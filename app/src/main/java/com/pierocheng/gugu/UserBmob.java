package com.pierocheng.gugu;

import cn.bmob.v3.BmobObject;

/**
 * Created by PieroCheng on 2016/5/15.
 */
public class UserBmob extends BmobObject {
    private String nameBmob;//用户姓名
    private String addressBmob;//注册地址
    private String avatarFileBmob;//头像储存
    private int comboBmobREG;//注册连击次数
    private int comboBmobVER;//验证连击次数
    private float fingerAeraBmobREG;//注册按压面积
    private float fingerAeraBmobVER;//验证按压面积
    private Boolean listernerBack = false;//声纹识别返回的布尔值
    private Boolean cameraBack = false;//头像认证返回的布尔值

    private String imgURL;

    public String getAddressBmob(){return addressBmob;}
    public void setAddressBmob(String addressBmob){this.addressBmob = addressBmob;}

    public String getImgURL(){return imgURL;}
    public void setImgURL(String imgURL){this.imgURL = imgURL;}

    public String getNameBmob() {
        return nameBmob;
    }

    public void setNameBmob(String nameBmob) {
        this.nameBmob = nameBmob;
    }

    public String getAvatarFileBmob() {
        return avatarFileBmob;
    }

    public void setAvatarFileBmob(String avatarFileBmob) {
        this.avatarFileBmob = avatarFileBmob;
    }

    public int getComboBmobREG() {
        return comboBmobREG;
    }

    public void setComboBmobREG(int comboBmobREG) {
        this.comboBmobREG = comboBmobREG;
    }

    public int getComboBmobVER() {
        return comboBmobVER;
    }

    public void setComboBmobVER(int comboBmobVER) {
        this.comboBmobVER = comboBmobVER;
    }

    public float getFingerAeraBmobREG() {
        return fingerAeraBmobREG;
    }

    public void setFingerAeraBmobREG(float fingerAeraBmobREG) {
        this.fingerAeraBmobREG = fingerAeraBmobREG;
    }

    public float getFingerAeraBmobVER() {
        return fingerAeraBmobVER;
    }

    public void setFingerAeraBmobVER(float fingerAeraBmobVER) {
        this.fingerAeraBmobVER = fingerAeraBmobVER;
    }

    public Boolean getListernerBack() {
        return listernerBack;
    }

    public void setListernerBack(Boolean listernerBack) {
        this.listernerBack = listernerBack;
    }

    public Boolean getCameraBack() {
        return cameraBack;
    }

    public void setCameraBack(Boolean cameraBack) {
        this.cameraBack = cameraBack;
    }
}
