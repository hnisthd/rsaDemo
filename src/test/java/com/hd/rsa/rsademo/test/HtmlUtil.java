package com.hd.rsa.rsademo.test;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.web.util.HtmlUtils;

/**
 * @author HD
 * @description
 * @date 2019/9/25 22:31
 */
public class HtmlUtil {
    public static void main(String[] args) {
        String s = HtmlUtils.htmlEscape("<>''&&ddd⊗     ss多大的@#￥%");
        System.out.println(s);
        String s2 = HtmlUtils.htmlUnescape(s);
        System.out.println(s2);

        String userName = "1' or '1'='1 ";
        String password = "123456";
        String s1 = StringEscapeUtils.escapeSql(userName);
        String s3 = StringEscapeUtils.escapeSql(password);
        String s4 = StringEscapeUtils.escapeJava(userName);
        String s5 = StringEscapeUtils.escapeJava(password);
        System.out.println(s1);
        System.out.println(s3);
        System.out.println(s4);
        System.out.println(s5);
        System.out.println("MIIBTAIBADCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoEFwIVAJKJ/1HhcHBWzyzipWDUCRe1vWNO".length());
    }
}
