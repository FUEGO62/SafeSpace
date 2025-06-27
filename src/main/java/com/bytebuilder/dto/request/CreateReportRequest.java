package com.bytebuilder.dto.request;

import com.bytebuilder.data.model.Type;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class CreateReportRequest {

    private double longitude;
    private double latitude;
    private Type type;
    private String issuer;
    private String pictureId;
    private String time;
    private boolean isVerified;
    private boolean isEmergency;
    private MultipartFile picture;
}
