package com.hd.rsa.rsademo.util;

import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.NoSuchAlgorithmException;

public class HMACSHA1 {
    /**
     * 定义加密方式
     * MAC算法可选以下多种算法
     * <pre>
     * HmacMD5
     * HmacSHA1
     * HmacSHA256
     * HmacSHA384
     * HmacSHA512
     * </pre>
     */
    //private final static String KEY_MAC = "HmacMD5";
    //private final static String KEY_MAC = "HmacSHA1";
    private final static String KEY_MAC = "HmacSHA512";

    /**
     * 全局数组
     */
    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    /**
     * 构造函数
     */
    public HMACSHA1() {

    }

    /**
     * BASE64 加密
     * @param key 需要加密的字节数组
     * @return 字符串
     * @throws Exception
     */
    public static String encryptBase64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    /**
     * BASE64 解密
     * @param key 需要解密的字符串
     * @return 字节数组
     * @throws Exception
     */
    public static byte[] decryptBase64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * 初始化HMAC密钥
     * @return
     */
    public static String init() {
        SecretKey key;
        String str = "";
        try {
            KeyGenerator generator = KeyGenerator.getInstance(KEY_MAC);
            key = generator.generateKey();
            str = encryptBase64(key.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * HMAC加密
     * @param data 需要加密的字节数组
     * @param key 密钥
     * @return 字节数组
     */
    public static byte[] encryptHMAC(byte[] data, String key) {
        SecretKey secretKey;
        byte[] bytes = null;
        try {
            secretKey = new SecretKeySpec(decryptBase64(key), KEY_MAC);
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * HMAC加密
     * @param data 需要加密的字符串
     * @param key 密钥
     * @return 字符串
     */
    public static String encryptHMAC(String data, String key) {
        if (StringUtils.isBlank(data)) {
            return null;
        }
        byte[] bytes = encryptHMAC(data.getBytes(), key);
        return byteArrayToHexString(bytes);
    }

    /**
     * 验证用户完整性
     * @param filePath
     * @param key
     * @return
     */
    public static String fileEncryptHMAC(String filePath, String key) {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(filePath));
            byte[] data = file2byte(fileInputStream);
            byte[] bytes = encryptHMAC(data, key);
            return byteArrayToHexString(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static byte[] file2byte(InputStream inStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[inStream.available()];

        int rc = 0;
        while((rc = inStream.read(buff))!=-1){
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        swapStream.close();
        return in2b;
    }


    /**
     * 将一个字节转化成十六进制形式的字符串
     * @param b 字节数组
     * @return 字符串
     */
    private static String byteToHexString(byte b) {
        int ret = b;
        //System.out.println("ret = " + ret);
        if (ret < 0) {
            ret += 256;
        }
        int m = ret / 16;
        int n = ret % 16;
        return hexDigits[m] + hexDigits[n];
    }

    /**
     * 转换字节数组为十六进制字符串
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private static String byteArrayToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(byteToHexString(bytes[i]));
        }
        return sb.toString();
    }

    /**
     * 测试方法
     * @param args
     */
    public static void main(String[] args) throws Exception {
        String key = HMACSHA1.init();
        System.out.println("Mac密钥:\n" + key);
        String word = "123";
        System.out.println(encryptHMAC(word, key));


        System.out.println(fileEncryptHMAC("F:\\Spring Data JPA从入门到精通@www.java1234.com.pdf",key));

    }
}

