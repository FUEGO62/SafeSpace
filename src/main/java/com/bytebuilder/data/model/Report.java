package com.bytebuilder.data.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
public class Report {

    @Id
    private String id;
    private String title;
    private double longitude;
    private double latitude;
    private List<String> likes = new ArrayList<>();
    private List<String> dislikes = new ArrayList<>();
    private Type type;
    private String username;
    private String pictureId;
    private String time;
    private boolean isVerified;
    private boolean isEmergency;
    private boolean isValid;
    private boolean isPending;
    private List<String> confirms = new ArrayList<>();
    private List<String> denys = new ArrayList<>();
}
