package com.mediaservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

@EnableWebMvc
@Configuration
class SwaggerConfig {
    val title: String = "MediaSevice API"
    val version: String = "1.0.0"
    val description: String = "Tmax MediaService Toy Project"

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.mediaservice.web"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(this.apiInfo())
    }

    fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title(this.title)
            .version(this.version)
            .description(this.description)
            .build()
    }
}
