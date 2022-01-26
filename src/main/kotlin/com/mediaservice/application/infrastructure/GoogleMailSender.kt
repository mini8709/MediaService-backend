package com.mediaservice.application.infrastructure

import com.mediaservice.config.EmailProperties
import com.mediaservice.exception.ErrorCode
import com.mediaservice.exception.ServerUnavailableException
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import java.util.Properties

@Component
class GoogleMailSender(val emailProperties: EmailProperties) {

    val mailSender: JavaMailSender = JavaMailSenderImpl().apply {
        val properties = Properties()
        properties.put("mail.smtp.auth", true)
        properties.put("mail.smtp.starttls.enable", true)
        properties.put("mail.smtp.starttls.required", true)
        properties.put("mail.smtp.ssl.trust", emailProperties.host)

        host = emailProperties.host
        port = emailProperties.port
        username = emailProperties.username
        password = emailProperties.password
        javaMailProperties = properties
        protocol = "smtp"
        defaultEncoding = "UTF-8"
    }

    fun sendMailWithNewPassword(to: String, newPassword: String) {
        try {
            val message = this.mailSender.createMimeMessage()
            val messageHelper = MimeMessageHelper(message, false, "UTF-8").apply {
                setFrom("cotlin.dev@gmail.com")
                setTo(to)
                setSubject("New password from Co-tlin")
                setText(
                    "The new password is \" $newPassword \". \nPlease update your password in our application."
                )
            }
        } catch (e: MailException) {
            throw ServerUnavailableException(ErrorCode.UNAVAILABLE_MAIL_SERVER, e.message!!)
        }
    }
}
