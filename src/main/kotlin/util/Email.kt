package util

import java.sql.Timestamp
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.Message
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

class Email(val SMTP_PASSWORD: String) {
    val PORT = 25
    val FROM_ADDRESS = "rotmdehf@naver.com"
    val FROM_NAME = "AUTO_TRADER"
    val TO_ADDRESS = "rotmdehf@gmail.com"
    val HOST = "smtp.naver.com"
    val SMTP_USERNAME = "rotmdehf@naver.com"
    val properties = System.getProperties()
    var session: Session? = null
    val message = MimeMessage(session)
    var mbpContent: MimeBodyPart? = null
    var mbpFile: MimeBodyPart? = null

    fun notifyError(content: String) {
        val subject = "(" + Timestamp(System.currentTimeMillis()) + ") 매매 프로그램 종료 알림"
        sendEmail(subject, content)
    }

    fun notifyTrading(content: String) {
        val subject = "(" + Timestamp(System.currentTimeMillis()) + ") 매매 체결 알림"
        sendEmail(subject, content)
    }

    fun create(): Email {
        properties.put("mail.transport.protocol", "smtp")
        properties.put("mail.smtp.port", PORT)
        properties.put("mail.smtp.starttls.enable", "true")
        properties.put("mail.smtp.auth", "true")
        session = Session.getDefaultInstance(properties)

        return this
    }

    fun from(senderAddress: String, senderName: String): Email {
        message.setFrom(InternetAddress(senderAddress, senderName))
        return this
    }

    fun to(recipientAddress: String): Email {
        message.setRecipient(Message.RecipientType.TO, InternetAddress(recipientAddress))
        return this
    }

    fun subject(subject: String): Email {
        message.subject = subject
        return this
    }

    fun content(content: String): Email {
        mbpContent = MimeBodyPart()
        mbpContent?.setContent(content, "text/html;charset=euc-kr")
        return this
    }

    fun attachFile(filePath: String): Email {
        mbpFile = MimeBodyPart()
        val fds = FileDataSource(filePath)
        mbpFile?.dataHandler = DataHandler(fds)
        mbpFile?.fileName = fds.name
        return this
    }

    fun send() {
        val mp = MimeMultipart()
        if (mbpContent != null) mp.addBodyPart(mbpContent)
        if (mbpFile != null) mp.addBodyPart(mbpFile)
        message.setContent(mp)

        val transport = session?.transport
        try {
            transport?.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD)
            transport?.sendMessage(message, message.allRecipients)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            transport?.close()
        }
    }

    fun sendEmailWithFile(subject: String, content: String, filePath: String) {
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

        val mbpContent = MimeBodyPart()
        mbpContent.setContent(content, "text/html;charset=euc-kr")

        val mbpFile = MimeBodyPart()
        val fds = FileDataSource(filePath)
        mbpFile.dataHandler = DataHandler(fds)
        mbpFile.fileName = fds.name

        val mp = MimeMultipart()
        mp.addBodyPart(mbpContent)
        mp.addBodyPart(mbpFile)
        message.setContent(mp)

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