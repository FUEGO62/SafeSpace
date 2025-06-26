package com.bytebuilder.data.model;

import lombok.Data;

@Data
public class Comment {
    private String comment;
    private String author;
    private String reportId;
}
