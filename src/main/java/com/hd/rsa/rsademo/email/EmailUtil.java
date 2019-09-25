package com.hd.rsa.rsademo.email;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * @author HD
 * @description
 * @date 2018/11/29 21:19
 */

import java.util.Properties;

/**
 * 使用SMTP协议发送电子邮件
 */
// TODO 发送各类邮件的方法都没有抽象出来的，真正项目中需要把里边的变量设置成方法的参数
public class EmailUtil {
    // 邮件发送协议
    private final static String PROTOCOL = "smtp";

    //todo SMTP邮件服务器(如果是其它邮件比如163，下面类容需要修改)
    private final static String HOST = "smtp.qq.com";

    //todo SMTP邮件服务器默认端口(如果是其它邮件比如163，下面类容需要修改)
    private final static String PORT = "465";

    // 是否要求身份认证
    private final static String IS_AUTH = "true";

    // 是否启用调试模式（启用调试模式可打印客户端与服务器交互过程时一问一答的响应消息）
    //TODO 生产时改为false   需要修改
    private final static String IS_ENABLED_DEBUG_MOD = "false";

    //TODO 你设置了smtp权限的邮箱  需要修改
    private static String from = "540753865@qq.com";

    //TODO 你设置了smtp权限的邮箱  需要修改
    private static String sendUserName="540753865@qq.com";

    //TODO 连接smtp.qq.com的密码  开启邮件服务时会自动生成  需要修改
    private static String sendUserPwd="rvtvwkbeejtfbedb";

    // 初始化连接邮件服务器的会话信息
    private static Properties props = null;

    static {
        props = new Properties();
        props.setProperty("mail.enable", "true");
        props.setProperty("mail.transport.protocol", PROTOCOL);
        props.setProperty("mail.smtp.host", HOST);
        props.setProperty("mail.smtp.port", PORT);
        props.setProperty("mail.smtp.auth", IS_AUTH);//视情况而定
        props.setProperty("mail.debug",IS_ENABLED_DEBUG_MOD);
        props.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");

    }

    /**
     * 发送简单的文本邮件
     */
    public static boolean sendTextEmail(String toUser,int code) throws Exception {
        try {
            // 创建Session实例对象
            Session session1 = Session.getDefaultInstance(props);

            // 创建MimeMessage实例对象
            MimeMessage message = new MimeMessage(session1);
            // 设置发件人
            message.setFrom(new InternetAddress(from));
            // 设置邮件主题
            message.setSubject("内燃机注册验证码");
            // 设置收件人
            message.setRecipient(RecipientType.TO, new InternetAddress(toUser));
            // 设置发送时间
            message.setSentDate(new Date());
            // 设置纯文本内容为邮件正文
            message.setText("您的验证码是："+code+"!验证码有效期是10分钟，过期后请重新获取！"
                    + "中国内燃机学会");
            // 保存并生成最终的邮件内容
            message.saveChanges();

            // 获得Transport实例对象
            Transport transport = session1.getTransport();
            // 打开连接
            transport.connect(sendUserName, sendUserPwd);
            // 将message对象传递给transport对象，将邮件发送出去
            transport.sendMessage(message, message.getAllRecipients());
            // 关闭连接
            transport.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        sendTextEmail("hnist_hd@163.com", 88888);
        //sendHtmlEmail("hnist_hd@163.com", 88888);
       // sendHtmlWithInnerImageEmail();
        //sendMultipleEmail();
    }

    /**
     * 发送简单的html邮件
     */
    public static boolean sendHtmlEmail(String to,int code) throws Exception {
        // 创建Session实例对象
        Session session1 = Session.getInstance(props, new MyAuthenticator());

        // 创建MimeMessage实例对象
        MimeMessage message = new MimeMessage(session1);
        // 设置邮件主题
        message.setSubject("设置的主题");
        // 设置发送人
        message.setFrom(new InternetAddress(from));
        // 设置发送时间
        message.setSentDate(new Date());
        // 设置收件人
        message.setRecipients(RecipientType.TO, InternetAddress.parse(to));
        // 设置html内容为邮件正文，指定MIME类型为text/html类型，并指定字符编码为gbk
        message.setContent("<div style='width: 600px;margin: 0 auto'><h3 style='color:#003E64; text-align:center; '>内燃机注册验证码</h3><p style=''>尊敬的用户您好：</p><p style='text-indent: 2em'>您在注册内燃机账号，此次的验证码是："+code+",有效期10分钟!如果过期请重新获取。</p><p style='text-align: right; color:#003E64; font-size: 20px;'>中国内燃机学会</p></div>","text/html;charset=utf-8");

        //设置自定义发件人昵称
        String nick="";
        try {
            //解决中文乱码
            nick=javax.mail.internet.MimeUtility.encodeText("设置的昵称");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        message.setFrom(new InternetAddress(nick+" <"+from+">"));
        // 保存并生成最终的邮件内容
        message.saveChanges();

        // 发送邮件
        try {
            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 发送带内嵌图片的HTML邮件
     */
    public static void sendHtmlWithInnerImageEmail() throws MessagingException {
        // 创建Session实例对象
        Session session = Session.getDefaultInstance(props, new MyAuthenticator());

        // 创建邮件内容
        MimeMessage message = new MimeMessage(session);
        // 邮件主题,并指定编码格式
        message.setSubject("带内嵌图片的HTML邮件", "utf-8");
        // 发件人
        message.setFrom(new InternetAddress(from));
        // 收件人
        message.setRecipients(RecipientType.TO, InternetAddress.parse("hnist_hd@163.com"));

        // 抄送（别人是可以看到这封邮件已经发给你了）
        message.setRecipient(RecipientType.CC, new InternetAddress("540753865@qq.com"));

        // 密送 (不会在邮件收件人名单中显示出来,别人看不到这封邮件也发给你了)
        message.setRecipient(RecipientType.BCC, new InternetAddress("945729084@qq.com"));
        // 发送时间
        message.setSentDate(new Date());

        // 创建一个MIME子类型为“related”的MimeMultipart对象
        MimeMultipart mp = new MimeMultipart("related");
        // 创建一个表示正文的MimeBodyPart对象，并将它加入到前面创建的MimeMultipart对象中
        MimeBodyPart htmlPart = new MimeBodyPart();
        mp.addBodyPart(htmlPart);
        // 创建一个表示图片资源的MimeBodyPart对象，将将它加入到前面创建的MimeMultipart对象中
        MimeBodyPart imagePart = new MimeBodyPart();
        mp.addBodyPart(imagePart);

        // 将MimeMultipart对象设置为整个邮件的内容
        message.setContent(mp);

        // 设置内嵌图片邮件体
        DataSource ds = new FileDataSource(new File("F:/123.png"));
        DataHandler dh = new DataHandler(ds);
        imagePart.setDataHandler(dh);
        imagePart.setContentID("123.png");  // 设置内容编号,用于其它邮件体引用

        // 创建一个MIME子类型为"alternative"的MimeMultipart对象，并作为前面创建的htmlPart对象的邮件内容
        MimeMultipart htmlMultipart = new MimeMultipart("alternative");
        // 创建一个表示html正文的MimeBodyPart对象
        MimeBodyPart htmlBodypart = new MimeBodyPart();
        // 其中cid=androidlogo.gif是引用邮件内部的图片，即imagePart.setContentID("androidlogo.gif");方法所保存的图片
        htmlBodypart.setContent("<span style='color:red;'>这是带内嵌图片的HTML邮件哦！！！<img src=\"cid:123.png\" /></span>","text/html;charset=utf-8");
        htmlMultipart.addBodyPart(htmlBodypart);
        htmlPart.setContent(htmlMultipart);

        // 保存并生成最终的邮件内容
        message.saveChanges();

        // 发送邮件
        Transport.send(message);
    }

    /**
     * 发送带内嵌图片、附件、多收件人(显示邮箱姓名)、邮件优先级、阅读回执的完整的HTML邮件
     */
    public static void sendMultipleEmail() throws Exception {
        String charset = "utf-8";   // 指定中文编码格式
        // 创建Session实例对象
        Session session = Session.getInstance(props,new MyAuthenticator());

        // 创建MimeMessage实例对象
        MimeMessage message = new MimeMessage(session);
        // 设置主题
        message.setSubject("使用JavaMail发送混合组合类型的邮件测试");
        // 设置发送人
        message.setFrom(new InternetAddress(from,"QQ测试邮箱",charset));
        // 设置收件人  //批量发送
        message.setRecipients(RecipientType.TO,
                new Address[] {
                        // 参数1：邮箱地址，参数2：姓名（在客户端收件只显示姓名，而不显示邮件地址），参数3：姓名中文字符串编码
                        new InternetAddress("540753865@qq.com", "张三_sohu", charset),
                        new InternetAddress("945729084@qq.com", "李四_163", charset),
                }
        );
        // 设置抄送（实际与上面设置的发送邮箱是不同的，因为没有邮箱测试了）
        message.setRecipient(RecipientType.CC, new InternetAddress("540753865@qq.com","王五_gmail",charset));
        // 设置密送（实际与上面设置的发送邮箱是不同的，因为没有邮箱测试了）
        message.setRecipient(RecipientType.BCC, new InternetAddress("945729084@qq.com", "赵六_QQ", charset));
        // 设置发送时间
        message.setSentDate(new Date());
        // 设置回复人(收件人回复此邮件时,默认收件人)
        message.setReplyTo(InternetAddress.parse("\"" + MimeUtility.encodeText("田七") + "\" <540753865@qq.com>"));
        // 设置优先级(1:紧急   3:普通    5:低)
        message.setHeader("X-Priority", "1");
        // 要求阅读回执(收件人阅读邮件时会提示回复发件人,表明邮件已收到,并已阅读)
        message.setHeader("Disposition-Notification-To", from);

        // 创建一个MIME子类型为"mixed"的MimeMultipart对象，表示这是一封混合组合类型的邮件
        MimeMultipart mailContent = new MimeMultipart("mixed");
        message.setContent(mailContent);

        // 附件
        MimeBodyPart attach1 = new MimeBodyPart();
        MimeBodyPart attach2 = new MimeBodyPart();
        // 内容
        MimeBodyPart mailBody = new MimeBodyPart();

        // 将附件和内容添加到邮件当中
        mailContent.addBodyPart(attach1);
        mailContent.addBodyPart(attach2);
        mailContent.addBodyPart(mailBody);

        // 附件1(利用jaf框架读取数据源生成邮件体)
        DataSource ds1 = new FileDataSource("F:/msdia80.dll");
        DataHandler dh1 = new DataHandler(ds1);
        attach1.setFileName(MimeUtility.encodeText("msdia80.dll"));
        attach1.setDataHandler(dh1);

        // 附件2
        DataSource ds2 = new FileDataSource("F:/1.sql");
        DataHandler dh2 = new DataHandler(ds2);
        attach2.setDataHandler(dh2);
        attach2.setFileName(MimeUtility.encodeText("1.sql"));

        // 邮件正文(内嵌图片+html文本)
        MimeMultipart body = new MimeMultipart("related");  //邮件正文也是一个组合体,需要指明组合关系
        mailBody.setContent(body);

        // 邮件正文由html和图片构成
        MimeBodyPart imgPart = new MimeBodyPart();
        MimeBodyPart htmlPart = new MimeBodyPart();
        body.addBodyPart(imgPart);
        body.addBodyPart(htmlPart);

        // 正文图片
        DataSource ds3 = new FileDataSource("F:/123.png");
        DataHandler dh3 = new DataHandler(ds3);
        imgPart.setDataHandler(dh3);
        imgPart.setContentID("123.png");

        // html邮件内容
        MimeMultipart htmlMultipart = new MimeMultipart("alternative");
        htmlPart.setContent(htmlMultipart);
        MimeBodyPart htmlContent = new MimeBodyPart();
        htmlContent.setContent(
                "<span style='color:red'>这是我自己用java mail发送的邮件哦！" +
                        "<img src='cid:123.png' /></span>"
                , "text/html;charset=gbk");
        htmlMultipart.addBodyPart(htmlContent);

        // 保存邮件内容修改
        message.saveChanges();

        /*File eml = buildEmlFile(message);
        sendMailForEml(eml);*/

        // 发送邮件
        Transport.send(message);
    }

    /**
     * 将邮件内容生成eml文件
     * @param message 邮件内容
     */
    public static File buildEmlFile(Message message) throws MessagingException, FileNotFoundException, IOException {
        File file = new File("c:\\" + MimeUtility.decodeText(message.getSubject())+".eml");
        message.writeTo(new FileOutputStream(file));
        return file;
    }

    /**
     * 发送本地已经生成好的email文件
     */
    public static void sendMailForEml(File eml) throws Exception {
        // 获得邮件会话
        Session session = Session.getInstance(props,new MyAuthenticator());
        // 获得邮件内容,即发生前生成的eml文件
        InputStream is = new FileInputStream(eml);
        MimeMessage message = new MimeMessage(session,is);
        //发送邮件
        Transport.send(message);
    }

    /**
     * 向邮件服务器提交认证信息
     */
    static class MyAuthenticator extends Authenticator {

        private String username = "";

        private String password = "";

        public MyAuthenticator() {
            super();
            this.password=sendUserPwd;
            this.username=sendUserName;
        }

        public MyAuthenticator(String username, String password) {
            super();
            this.username = username;
            this.password = password;
        }

        protected PasswordAuthentication getPasswordAuthentication() {

            return new PasswordAuthentication(username, password);
        }
    }
}
