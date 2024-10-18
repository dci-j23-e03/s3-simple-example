package com.dzenang;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {

        // need to provide env variables for authentication
        // run configuration for your program, .env file, user system env variables (setenv/set
        // AWS_ACCESS_KEY_ID
        // AWS_SECRET_ACCESS_KEY

//        createBucket();
//        readSingleObject("dzenan-dci-bucket-1");
//        putSingleObject("dzenan-dci-bucket-3");
    }

    private static void putSingleObject(String bucketName) {
        try(S3Client s3Client = createClient(bucketName)) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key("sdkUploadedInvoice")
                    .build();
            RequestBody requestBody = RequestBody.fromFile(Path.of("src", "main", "resources", "invoice.png"));
            s3Client.putObject(putObjectRequest, requestBody);
        } catch (Exception e) {
            System.out.println("Error putting file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void readSingleObject(String bucketName) {
        try(S3Client s3Client = createClient(bucketName)) {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key("invoice.png")
                    .build();
            ResponseInputStream<GetObjectResponse> inputStream = s3Client.getObject(getObjectRequest);
            try(FileOutputStream outputStream = new FileOutputStream("src/main/resources/invoice.png")) {
                byte[] bytes = inputStream.readAllBytes();
                outputStream.write(bytes);
            } catch (Exception e) {
                System.out.println("Error reading file: " + e.getMessage());
            }
        }
    }

    private static S3Client createClient(String bucketName) {
        Region region = Region.EU_CENTRAL_1;
        return S3Client.builder().region(region).build();
    }

    private static void createBucket() {
        String bucketName = "dzenan-dci-bucket-3";
        try(S3Client s3Client = createClient(bucketName)){
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.createBucket(createBucketRequest);
        } catch(Exception e){
            System.out.println("Error creating bucket: " + e.getMessage());
        }
    }
}