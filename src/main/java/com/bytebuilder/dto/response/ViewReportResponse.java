package com.bytebuilder.dto.response;

import com.bytebuilder.data.model.Comment;
import com.bytebuilder.data.model.Type;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ViewReportResponse {

    private String reportId;
    private String title;
    private double longitude;
    private double latitude;
    private List<String> likes = new ArrayList<>();
    private List<String> dislikes = new ArrayList<>();
    private Type type;
    private String issuer;
    private byte[] picture;
    private String time;
    private boolean isVerified;
    private boolean isEmergency;
    private List<Comment> comments = new ArrayList<>();
}
