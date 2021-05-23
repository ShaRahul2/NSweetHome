package com.upgrade.bookingservice.configuration;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
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
import java.util.Properties;

@org.springframework.context.annotation.Configuration
@EnableSwagger2
public class Configuration {
	
	
	public final static String TOPIC = "message";
    public final static String BOOTSTRAP_SERVERS = "34.229.70.228:9092";
    
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

    public static Producer<Long, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                                            BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "upgrad-group");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                                        LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                                    StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }	
}
