package com.test.microblogging.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.test.microblogging.utils.Constantes;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(Constantes.SWAGGER_API_TITLE)
                        .description(Constantes.SWAGGER_API_DESCRIPTION)
                        .version(Constantes.SWAGGER_API_VERSION)
                        .contact(new Contact()
                                .name(Constantes.DEVELOPER_NAME)
                                .email(Constantes.DEVELOPER_EMAIL)
                                .url(Constantes.DEVELOPER_GITHUB)));
    }
}