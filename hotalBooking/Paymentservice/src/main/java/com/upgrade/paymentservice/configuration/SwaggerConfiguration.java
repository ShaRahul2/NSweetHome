package com.upgrade.paymentservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket restApi() {
        return new Docket(DocumentationType.SWAGGER_2.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(""))
                .build()
                .apiInfo(metaInfo());
    }

    public ApiInfo metaInfo() {
        return new ApiInfo(
                "Hotel Management",
                "Hotel Management Rest API System",
                "1.0",
                "https://Ruhohotel.com",
                new Contact("Ruhohotel Corpration","https://rhhotel.com", "Ruhohotel@bnm.com"),
                "Ruhohotel. lincense",
                "https://Ruhohotel.com", new ArrayList<>());
    }
}
