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
import com.bytebuilder.integration.CloudService;
import com.bytebuilder.util.JwtService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private CloudService cloudService;

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
        List<String> dislikes = report.getDislikes();
        if(dislikes.contains(request.getUsername()))dislikes.remove(request.getUsername());
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
        List<String> likes = report.getLikes();
        if(likes.contains(request.getUsername()))likes.remove(request.getUsername());
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
                )).filter(x->(!x.isPending() &&!x.isValid())).toList();


        List<ViewReportResponse> responses = new ArrayList<>();
        ViewReportResponse response;
        byte[] image;
        for(Report report : reports){
            response = modelMapper.map(report, ViewReportResponse.class);
            String blobId = report.getPictureId();

            if(!blobId.equals("dog")) {
                image = cloudService.getFileBy(blobId);
                response.setPicture(image);
            }
            responses.add(response);

        }

        return responses;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {

        double EARTH_RADIUS_KM = 6371;
        System.out.println(lat1);
        System.out.println(lon1);
        System.out.println(lat2);
        System.out.println(lon2);


        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);


        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        System.out.println((EARTH_RADIUS_KM * c));
        return EARTH_RADIUS_KM * c;
    }


    @Override
    public List<Comment> comment(CommentRequest request) {
        Comment comment = modelMapper.map(request, Comment.class);
        commentRepository.save(comment);
        return  commentRepository.findAll().stream().filter(x->(x.getReportId().equals(request.getReportId()))).toList();
    }

    @Override
    public void createReport(CreateReportRequest request) {
        Report report = modelMapper.map(request, Report.class);
        report.setPending(true);
        reportRepository.save(report);
        randomAlgorithm(report);
    }

    @Override
    public List<String> confirm(ReportInteractRequest request) {
        Report report = reportRepository.findById(request.getReportId()).get();
        List<String> confirm = report.getConfirms();
        confirm.add(request.getUsername());
        User user = userRepository.findByName(request.getUsername());
        List<Report> inbox = user.getInbox();
        inbox.remove(report);
        user.setInbox(inbox);
        userRepository.save(user);
        report.setConfirms(confirm);
        reportRepository.save(report);
        checkRequirement(report);
        return confirm;
    }

    private void checkRequirement(Report report) {
        int confirms = report.getConfirms().size();
        int denys = report.getDenys().size();
        int total = report.getConsensusNumber();
        int pass = confirms/total *100;
        int fail = denys/total *100;
        if (fail>65)report.setValid(false);
        if(pass>65)report.setPending(false);
        reportRepository.save(report);
    }

    @Override
    public List<String> deny(ReportInteractRequest request) {
        Report report = reportRepository.findById(request.getReportId()).get();
        List<String> denys = report.getDenys();
        denys.add(request.getUsername());
        User user = userRepository.findByName(request.getUsername());
        List<Report> inbox = user.getInbox();
        inbox.remove(report);
        user.setInbox(inbox);
        userRepository.save(user);
        report.setDenys(denys);
        reportRepository.save(report);
        checkRequirement(report);
        return denys;
    }

    @Override
    public List<Comment> viewComments(ReportInteractRequest request) {
        return  commentRepository.findAll().stream().filter(x->(x.getReportId().equals(request.getReportId()))).toList();
    }

    @Override
    public List<ViewReportResponse> viewInbox(ReportInteractRequest request) {
        String username = request.getUsername();
        User user = userRepository.findByName(username);
        List<ViewReportResponse> responses = new ArrayList<>();
        List<Report> inbox = user.getInbox();
        for(Report report : inbox){
            responses.add(modelMapper.map(report, ViewReportResponse.class));
        }
        return responses;
    }

    private void randomAlgorithm(Report report) {
        double latitude = report.getLatitude();
        double longitude = report.getLongitude();

        List<User> candidates = userRepository.findAll()
                .stream()
                .filter(x->(calculateDistance(latitude,longitude, x.getCurrentLatitude(), x.getCurrentLongitude())<0.77))
                .toList();

        if(candidates.isEmpty()){
            report.setPending(false);
            reportRepository.save(report);
            return;
        }

        int randomSize = (int) (candidates.size()*0.65) + 1;
        Set<Integer> indexes = new HashSet<>();
        List<User> agreements = new ArrayList<>();
        Random rand = new Random();
        int max = candidates.size()-1;
        int min = 0;
        for (int i = 0; indexes.size()<randomSize; i++) {
            int num = rand.nextInt(max - min + 1) + min;
            indexes.add(num);
        }
        indexes.forEach(x->agreements.add(candidates.get(x)));
        agreements.forEach(user->{
            List<Report> reports = user.getInbox();
            reports.add(report);
            user.setInbox(reports);
            userRepository.save(user);
            report.setConsensusNumber(randomSize);
            reportRepository.save(report);
        });
    }



    private void validate(final String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name cannot be null or empty");
        if (userRepository.existsByName(name)) throw new IllegalArgumentException("Username already exists");
    }
}
