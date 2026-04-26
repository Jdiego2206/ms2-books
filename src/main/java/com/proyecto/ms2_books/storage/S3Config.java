package com.proyecto.ms2_books.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Bean
    public S3Service s3Service(
            @Value("${AWS_ACCESS_KEY_ID:}") String accessKey,
            @Value("${AWS_SECRET_ACCESS_KEY:}") String secretKey,
            @Value("${AWS_REGION:}") String region,
            @Value("${AWS_S3_BUCKET:}") String bucket) {
        return new S3Service(accessKey, secretKey, region, bucket);
    }
}
