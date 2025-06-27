package com.bytebuilder.dto.request;

import com.bytebuilder.data.model.Type;
import lombok.Data;

@Data
public class CreateReportRequest {

    private String title;
    private double longitude;
    private double latitude;
    private Type type;
    private String username;
    private String time;
    private String pictureId;
    private boolean isEmergency;

}
