package com.hd.rsa.rsademo.model;

import java.io.Serializable;

/**
 * @author HD
 * @description
 * @date 2019/9/22 23:33
 */
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 3739254730223826957L;

    private String userId;

    private String userName;

    private String phoneNum;

    private  String sign;
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }


}
