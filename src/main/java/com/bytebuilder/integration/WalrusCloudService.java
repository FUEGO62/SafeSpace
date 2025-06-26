package com.bytebuilder.integration;


import com.bytebuilder.dto.response.walrus.WalrusUploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpMethod.PUT;

@Component
public class WalrusCloudService implements CloudService{

//    @Value("${walrus.app.url}")
    private String walrusUrl = "https://publisher.walrus-testnet.walrus.space/v1/blobs";

//    @Value("${walrus.app.aggregator}")
    private String walrusAggregator = "https://aggregator.walrus-testnet.walrus.space/v1/blobs/";

//    @Value("${walrus.app.epoch}")
    private String epoch = "5";

//    @Value("${walrus.app.address}")
    private String walrusUploadAddress = "0x202f0872cf991ed2091c1a949891bd2b45cdc2a687343a54064244d3066b43b6";

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public String upload(MultipartFile file) {

        return extractBlobIdFrom(restTemplate.exchange(walrusUrl, PUT,
                buildUploadRequest(file), WalrusUploadResponse.class,
                createQueryParams()));

    }

    @Override
    public byte[] getFileBy(String blobId) {
        ResponseEntity<byte[]> response = restTemplate.getForEntity(walrusAggregator.concat(blobId),
                byte[].class);
        return response.getBody();
    }

    private static String extractBlobIdFrom(ResponseEntity<WalrusUploadResponse> response) {
        WalrusUploadResponse walrusUploadResponse = response.getBody();
        boolean isFileAlreadyExists = walrusUploadResponse != null
                && walrusUploadResponse.getNewlyCreated() == null;
        if (isFileAlreadyExists) return walrusUploadResponse.getAlreadyCertified().getBlobId();
        return walrusUploadResponse.getNewlyCreated().getBlobObject().getBlobId();
    }

    public HttpEntity<?> buildUploadRequest(MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        Resource resource = file.getResource();
        return new HttpEntity<>(resource, headers);
    }

    private Map<String, ?> createQueryParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("epoch", Integer.parseInt(epoch));
        params.put("send_object_to", walrusUploadAddress);
        return params;
    }
}
