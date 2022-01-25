package com.mediaservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.Properties

@Configuration
class ApplicationConfig(val emailProperties: EmailProperties) {

    @Bean
    fun mailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl().apply {
            val properties = Properties()
            properties.put("mail.smtp.auth", true)
            properties.put("mail.smtp.starttls.enable", true)
            properties.put("mail.smtp.starttls.required", true)

            host = emailProperties.host
            port = emailProperties.port
            username = emailProperties.username
            password = emailProperties.password
            javaMailProperties = properties
            protocol = "smtp"
            defaultEncoding = "UTF-8"
        }

        return mailSender
    }
}
