package com.hd.rsa.rsademo.controller;

import com.google.gson.Gson;
import com.hd.rsa.rsademo.util.Base64;
import com.hd.rsa.rsademo.util.RSAEncrypt;
import com.hd.rsa.rsademo.util.RSASignature;
import com.hd.rsa.rsademo.model.UserInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HD
 * @description
 * @date 2019/9/22 23:32
 */
@RestController
public class RsaTestController {

    private  static final String FILEPATH="E:/tmp/";

    private  static final String SIGNFILEPATH="E:/tmpSign/";

    @RequestMapping("userInfo/intoMethod")
    public UserInfo intoMethod() throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("1234555");
        userInfo.setUserName("周缘");
        userInfo.setPhoneNum("13556878079");
        String signStr=RSASignature.sign("huangdong",RSAEncrypt.loadPrivateKeyByFile(SIGNFILEPATH));
        userInfo.setSign(signStr);

        Gson gson = new Gson();
        String userInfoStr = gson.toJson(userInfo);


        //公钥加密过程
        byte[] cipherData=RSAEncrypt.encrypt(RSAEncrypt.loadPublicKeyByStr(RSAEncrypt.loadPublicKeyByFile(FILEPATH)),userInfoStr.getBytes());
        String cipher= Base64.encode(cipherData);
        UserInfo userInfo1 = getUserInfo(cipher);

        return userInfo1;
    }


    public UserInfo getUserInfo(String userInfoStr) throws Exception {
        byte[] res=RSAEncrypt.decrypt(RSAEncrypt.loadPrivateKeyByStr(RSAEncrypt.loadPrivateKeyByFile(FILEPATH)), Base64.decode(userInfoStr));
        Gson gson = new Gson();
        String restr=new String(res);
        UserInfo userInfo = gson.fromJson(restr, UserInfo.class);
        String sign = userInfo.getSign();
        boolean doCheck = RSASignature.doCheck("huangdong", sign, RSAEncrypt.loadPublicKeyByFile(SIGNFILEPATH));

        if(doCheck){
            userInfo.setSign("huangdong");
        }
        return userInfo;
    }
}
