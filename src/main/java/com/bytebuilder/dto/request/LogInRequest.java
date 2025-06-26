package com.bytebuilder.dto.request;

import lombok.Data;

@Data
public class LogInRequest {
    private String name;
    private String password;
}
