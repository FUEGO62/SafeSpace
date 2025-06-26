package com.bytebuilder.dto.request;

import com.bytebuilder.data.model.Type;
import lombok.Data;


import java.util.ArrayList;
import java.util.List;

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
}
