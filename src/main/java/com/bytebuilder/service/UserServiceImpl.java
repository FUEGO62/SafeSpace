package com.bytebuilder.service;

import com.bytebuilder.data.model.Comment;
import com.bytebuilder.data.model.Location;
import com.bytebuilder.data.model.Report;
import com.bytebuilder.data.model.User;
import com.bytebuilder.data.repository.CommentRepository;
import com.bytebuilder.data.repository.ReportRepository;
import com.bytebuilder.data.repository.UserRepository;
import com.bytebuilder.dto.request.*;
import com.bytebuilder.dto.response.AuthResponse;
import com.bytebuilder.dto.response.ViewReportResponse;
import com.bytebuilder.util.JwtService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AuthResponse login(LogInRequest request) {
        User user = userRepository.findByName(request.getName());
        if (user != null) {
            if (user.getPassword().equals(request.getPassword())) {
                AuthResponse authResponse = new AuthResponse();
                authResponse.setToken(jwtService.generateToken(request.getName()));
                return authResponse;
            }
        }
        throw new IllegalArgumentException("Invalid username or password");
    }


    @Override
    public AuthResponse register(SignUpRequest request) {
        validate(request.getName());
        User user = modelMapper.map(request, User.class);
        userRepository.save(user);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtService.generateToken(request.getName()));
        return authResponse;
    }

    @Override
    public void updateLocation(String name,UpdateLocationRequest request) {
        User user = userRepository.findByName(name);
        if (user != null) {
            user.setCurrentLatitude(request.getLatitude());
            user.setCurrentLongitude(request.getLongitude());
            userRepository.save(user);
        }
    }

    @Override
    public List<String> like(ReportInteractRequest request) {
        Report report = reportRepository.findById(request.getReportId()).get();
        List<String> likes = report.getLikes();
        if(likes.contains(request.getUsername()))likes.remove(request.getUsername());
        else likes.add(request.getUsername());
        report.setLikes(likes);
        reportRepository.save(report);
        return likes;
    }

    @Override
    public List<String> dislike(ReportInteractRequest request) {
        Report report = reportRepository.findById(request.getReportId()).get();
        List<String> dislikes = report.getDislikes();
        if(dislikes.contains(request.getUsername()))dislikes.remove(request.getUsername());
        else dislikes.add(request.getUsername());
        report.setDislikes(dislikes);
        reportRepository.save(report);
        return dislikes;
    }


    @Override
    public List<Location> getLocation(String username) {
        System.out.println(username);
        User user = userRepository.findByName(username);
        return user.getLocations();
    }

    @Override
    public List<ViewReportResponse> viewReport(ViewReportRequest request) {
        double long1 = Double.parseDouble(request.getLongitude());
        double lat1 = Double.parseDouble(request.getLatitude());
        List<Report> reports  = reportRepository.findAll().stream().filter(x->(
                calculateDistance(lat1,long1, x.getLatitude(), x.getLongitude())<0.77
                )).toList();
        List<ViewReportResponse> responses = new ArrayList<>();
        return responses;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {

        double EARTH_RADIUS_KM = 6371;

        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);


        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }


    @Override
    public List<Comment> comment(CommentRequest request) {
        Comment comment = modelMapper.map(request, Comment.class);
        commentRepository.save(comment);
        return  commentRepository.findAll().stream().filter(x->(x.getReportId().equals(request.getReportId()))).toList();
    }

    public void createReport(CreateReportRequest request) {
        Report report = modelMapper.map(request, Report.class);
    }

    private void validate(final String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name cannot be null or empty");
        if (userRepository.existsByName(name)) throw new IllegalArgumentException("Username already exists");
    }
}
