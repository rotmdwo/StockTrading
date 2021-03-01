package util

import java.sql.Timestamp
import javax.mail.Message
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class Email(val SMTP_PASSWORD: String) {
    fun notifyError(content: String) {
        val subject = "(" + Timestamp(System.currentTimeMillis()) + ") 매매 프로그램 종료 알림"
        sendEmail(subject, content)
    }

    fun notifyTrading(content: String) {
        val subject = "(" + Timestamp(System.currentTimeMillis()) + ") 매매 체결 알림"
        sendEmail(subject, content)
    }

    fun sendEmail(subject: String, content: String) {
        val PORT = 25
        val FROM_ADDRESS = "rotmdehf@naver.com"
        val FROM_NAME = "AUTO_TRADER"
        val TO_ADDRESS = "rotmdehf@gmail.com"
        val HOST = "smtp.naver.com"
        val SMTP_USERNAME = "rotmdehf@naver.com"

        val properties = System.getProperties()
        properties.put("mail.transport.protocol", "smtp")
        properties.put("mail.smtp.port", PORT)
        properties.put("mail.smtp.starttls.enable", "true")
        properties.put("mail.smtp.auth", "true")

        val session = Session.getDefaultInstance(properties)
        val message = MimeMessage(session)
        message.setFrom(InternetAddress(FROM_ADDRESS, FROM_NAME))
        message.setRecipient(Message.RecipientType.TO, InternetAddress(TO_ADDRESS))
        message.subject = subject
        message.setContent(content, "text/html;charset=euc-kr")

        val transport = session.transport
        try {
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD)
            transport.sendMessage(message, message.allRecipients)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            transport.close()
        }
    }
}