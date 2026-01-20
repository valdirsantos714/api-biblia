package com.valdirsantos714.biblia.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Bíblia")
                        .version("0.2.0")
                        .description("API REST para acesso e consulta de conteúdo bíblico com múltiplas versões e recursos de busca avançada.")
                        .contact(new Contact()
                                .name("Valdir Santos")
                                .url("https://github.com/valdirsantos714"))
                );
    }
}

