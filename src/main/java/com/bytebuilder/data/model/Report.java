package com.bytebuilder.data.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
public class Report {

    @Id
    private String id;
    private double longitude;
    private double latitude;
    private List<String> likes = new ArrayList<>();
    private List<String> dislikes = new ArrayList<>();
    private Type type;
    private String issuer;
    private String pictureId;
    private String time;
    private boolean isVerified;
    private boolean isEmergency;
}
