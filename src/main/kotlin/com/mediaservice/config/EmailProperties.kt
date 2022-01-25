package com.mediaservice.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "spring.mail")
class EmailProperties {
    lateinit var host: String
    var port: Int = 0
    lateinit var username: String
    lateinit var password: String
}
