package com.bytebuilder.data.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    @Id
    private String id;
    private double currentLongitude;
    private double currentLatitude;
    private String name;
    private String password;
    private List<Location> locations;
    private List<Report> inbox = new ArrayList<Report>();
}
