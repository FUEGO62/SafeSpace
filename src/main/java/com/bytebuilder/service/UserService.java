package com.bytebuilder.service;

import com.bytebuilder.data.model.Comment;
import com.bytebuilder.data.model.Location;
import com.bytebuilder.dto.request.*;
import com.bytebuilder.dto.response.AuthResponse;

import java.util.List;

public interface UserService {
    AuthResponse login(LogInRequest request);
    AuthResponse register(SignUpRequest request);
    void updateLocation(String name,UpdateLocationRequest request);
    List<String> like(ReportInteractRequest request);
    List<String> dislike(ReportInteractRequest request);
    List<Comment> comment(CommentRequest request);
    List<Location> getLocation(String username);
}
