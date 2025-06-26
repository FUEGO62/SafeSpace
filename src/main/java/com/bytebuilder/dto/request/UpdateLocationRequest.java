package com.bytebuilder.dto.request;

import lombok.Data;

@Data
public class UpdateLocationRequest {
    private double latitude;
    private double longitude;
}
