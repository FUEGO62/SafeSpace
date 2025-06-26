package com.bytebuilder.dto.request;

import com.bytebuilder.data.model.Location;
import lombok.Data;

import java.util.List;

@Data
public class SignUpRequest {
    private String name;
    private String password;
    private List<Location> locations;
}
