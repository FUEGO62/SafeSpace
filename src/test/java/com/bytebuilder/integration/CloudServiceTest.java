package com.bytebuilder.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class CloudServiceTest {

    @Autowired
    private CloudService cloudService;

    @Test
    void testCanUploadFile() {
        String fileLocation = "C:\\Users\\nzigw\\Downloads\\yuvraj-singh-4V07cUP8Sxc-unsplash.jpg";
        Path path = Paths.get(fileLocation);
        try(var inputStream = Files.newInputStream(path)) {
            MultipartFile file = new MockMultipartFile("image", inputStream);
            String blobId = cloudService.upload(file);
            System.out.println("blob id is "+blobId);
            assertThat(blobId).isNotNull();
            assertThat(blobId).isNotEmpty();
        } catch(IOException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    void testCanGetfile() {
        String blobId = "kEhIz0Hxm32BYmU4mJyTFcYoDWXXRskzNuKUJYQ1REg";
        byte[] fileContent = cloudService.getFileBy(blobId);
        assertThat(fileContent).isNotNull();
        assertThat(fileContent).isNotEmpty();
    }
}
