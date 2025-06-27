package com.bytebuilder.service;

import com.bytebuilder.data.model.Comment;
import com.bytebuilder.data.model.Location;
import com.bytebuilder.data.model.Report;
import com.bytebuilder.dto.request.*;
import com.bytebuilder.dto.response.AuthResponse;
import com.bytebuilder.dto.response.ViewReportResponse;

import java.util.List;

public interface UserService {
    AuthResponse login(LogInRequest request);
    AuthResponse register(SignUpRequest request);
    void updateLocation(String name,UpdateLocationRequest request);
    List<String> like(ReportInteractRequest request);
    List<String> dislike(ReportInteractRequest request);
    List<Comment> comment(CommentRequest request);
    List<Location> getLocation(String username);
    List<ViewReportResponse> viewReport(ViewReportRequest request);
    void createReport(CreateReportRequest request);
}
