package utils;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtils {



    private static final String SENDER_EMAIL = "hungnguyen20052301@gmail.com";
    //private static final String APP_PASSWORD = "dzqn zdjr ugec ygsj";
    private static final String APP_PASSWORD = "your_app_password_here";


    public static void sendEmail(String toEmail, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // Bắt buộc cho Java đời cao

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL, "Hệ Thống CSKH"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=UTF-8");

            Transport.send(message);
            System.out.println("✅ Đã gửi email thành công tới: " + toEmail);

        } catch (Exception e) {
            System.out.println("❌ Lỗi khi gửi email: " + e.getMessage());
        }
    }
}
