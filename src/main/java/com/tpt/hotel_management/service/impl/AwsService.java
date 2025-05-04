package com.tpt.hotel_management.service.impl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.tpt.hotel_management.exception.OurException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwsService {
    @Value("${bucket.name}")
    private String bucketName;

    @Value("${aws.s3.access.key}")
    private String accessKey;

    @Value("${aws.s3.secret.key}")
    private String secretKey;

    public String saveImageToS3(MultipartFile photo) {
        String s3Filename = photo.getOriginalFilename();
        try {
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .withRegion(Regions.US_EAST_1)
                    .withRegion(Regions.AP_EAST_1)
                    .build();
            InputStream inputStream = photo.getInputStream();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(photo.getSize());
            objectMetadata.setContentType(photo.getContentType());
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3Filename, inputStream, objectMetadata);
            s3Client.putObject(putObjectRequest);
            return "https://" + bucketName + ".s3.amazonaws.com/" + s3Filename;
        } catch (Exception e) {
            log.error("Error aws: {}", e.getMessage());
            throw new OurException("Unable to upload image to s3 bucket: " + e.getMessage());
        }
    }
}
