package mail;

import util.PropertiesUtil;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by SAM on 2018/1/8.
 */
public class Mail {
    static PropertiesUtil propertiesUtil=new PropertiesUtil();
    public static void mailToMe(String downloadNote)  {
        Properties properties = new Properties();
        properties.setProperty("mail.host", "smtp.exmail.qq.com");
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.socketFactory.port", "465");
        Session session = Session.getInstance(properties,new Authenticator() {      //
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //第一个参数代表邮箱的用户名，如果是163邮箱，则是******@163.com
                //第二个参数代表密码。在qq邮箱中，需要填写的是一个授权码. 126同样需要授权码。
                return new PasswordAuthentication(propertiesUtil.getProperty("mail_sender_address"), propertiesUtil.getProperty("mail_password"));
            }
        });

        //开启Session的debug模式
        session.setDebug(true);
        //2、通过session得到transport对象
        MimeMessage message = createSimpleMail(session ,downloadNote);
        try {
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static MimeMessage createSimpleMail(Session session ,String note) {
        //创建邮件对象
        MimeMessage message = new MimeMessage(session);
        //指明发件人
        try {
            message.setFrom(new InternetAddress(propertiesUtil.getProperty("mail_sender_address")));

            String receivers =propertiesUtil.getProperty("mail_receiver_address");  //多个接收者。
            if(null != receivers && !receivers.isEmpty()){
                //@SuppressWarnings("static-access")
                InternetAddress[] internetAddressTo = new InternetAddress().parse(receivers);
                message.setRecipients(Message.RecipientType.TO, internetAddressTo);
            }

            //message.setRecipient(Message.RecipientType.TO, new InternetAddress(propertiesUtil.getProperty("mail_receiver01_address")));
            //message.setRecipient(Message.RecipientType.CC, new InternetAddress(propertiesUtil.getProperty("mail_receiver02_address")));
            //设置主题
            message.setSubject("MYJ数据下载通告");
            //设置内容
            message.setContent(note,"text/html;charset=UTF-8");
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }


}
