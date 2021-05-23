package com.upgrade.bookingservice.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@org.springframework.context.annotation.Configuration
@EnableSwagger2
public class Configuration {

    @Bean
    public RestTemplate restTemplateBean(RestTemplateBuilder builder) {
        return builder.build();
    }


    @Bean
    public Docket restApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(""))
                .build()
                .apiInfo(metaInfo());
    }

    public ApiInfo metaInfo() {
        return new ApiInfo(
                "Hotel Management",
                "Hotel Management Booking Seervice Rest API System",
                "1.0",
                "https://Ruhohotel.com",
                new Contact("Ruhohotel Corpration","https://rhhotel.com", "Ruhohotel@bnm.com"),
                "Ruhohotel. lincense",
                "https://Ruhohotel.com", new ArrayList<>());
    }

}
