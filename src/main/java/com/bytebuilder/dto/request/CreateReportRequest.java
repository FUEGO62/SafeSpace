package com.bytebuilder.dto.request;

import com.bytebuilder.data.model.Type;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class CreateReportRequest {

    private double longitude;
    private double latitude;
    private Type type;
    private String username;
    private String time;
    private MultipartFile picture;
}
