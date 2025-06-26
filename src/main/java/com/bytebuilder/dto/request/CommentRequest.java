package com.bytebuilder.dto.request;

import lombok.Data;

@Data
public class CommentRequest {
    private String comment;
    private String author;
    private String reportId;
}

