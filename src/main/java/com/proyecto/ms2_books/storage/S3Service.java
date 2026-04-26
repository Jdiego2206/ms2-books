package com.proyecto.ms2_books.storage;

import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

public class S3Service {

    private final S3Client client;
    private final String bucket;
    private final String region;
    private final boolean configured;

    public S3Service(String accessKey, String secretKey, String region, String bucket) {
        this.bucket = bucket;
        this.region = region;
        this.configured = !accessKey.isBlank() && !secretKey.isBlank()
                && !region.isBlank() && !bucket.isBlank();

        if (this.configured) {
            this.client = S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(accessKey, secretKey)))
                    .build();
        } else {
            this.client = null;
        }
    }

    public boolean isConfigured() {
        return configured;
    }

    public String uploadBookPhoto(MultipartFile file, Long bookId) throws IOException {
        String ext = getExtension(file.getOriginalFilename());
        String key = String.format("book-photos/%d/%s%s",
                bookId, UUID.randomUUID().toString().replace("-", ""), ext);

        client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, key);
    }

    public void deleteBookPhoto(String photoUrl) {
        String prefix = String.format("https://%s.s3.%s.amazonaws.com/", bucket, region);
        if (!photoUrl.startsWith(prefix)) return;
        String key = photoUrl.substring(prefix.length());
        client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf("."));
    }
}
