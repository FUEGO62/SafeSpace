package com.bytebuilder.service;

import com.bytebuilder.data.model.Location;

import com.bytebuilder.data.model.Report;
import com.bytebuilder.data.model.Type;
import com.bytebuilder.data.repository.ReportRepository;
import com.bytebuilder.data.repository.UserRepository;
import com.bytebuilder.dto.request.LogInRequest;
import com.bytebuilder.dto.request.SignUpRequest;
import com.bytebuilder.dto.response.AuthResponse;
import com.bytebuilder.util.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtService jwtService;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    UserService userService;

    @Test
    public void test(){
        Report report = new Report();
        report.setLatitude(6.4551703);
        report.setLongitude(3.5179751);
        report.setDislikes(new ArrayList<>());
        report.setLikes(new ArrayList<>());
        report.setIssuer("3baba");
        report.setVerified(true);
        report.setTitle("unknown gun men ");
        reportRepository.save(report);
    }

    @Test
    public void testUserCanRegister() {

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setName("jesse");
        signUpRequest.setPassword("*****");
        signUpRequest.setLocations(List.of(new Location(), new Location()));

        if(userRepository.existsByName(signUpRequest.getName())) {
            userRepository.delete(userRepository.findByName(signUpRequest.getName()));
        }

        AuthResponse authResponse = userService.register(signUpRequest);
        assertNotNull(authResponse.getToken());
        System.out.println(authResponse.getToken());
    }

    @Test
    public void testUserServiceThrowsExceptionWithNameInUse() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setName("jesse");
        signUpRequest.setPassword("*****");
        assertThrows(IllegalArgumentException.class,()-> userService.register(signUpRequest));
    }

    @Test
    public void testUserCanLogin() {
        LogInRequest loginRequest = new LogInRequest();
        loginRequest.setName("jesse");
        loginRequest.setPassword("*****");
        AuthResponse authResponse = userService.login(loginRequest);
        assertNotNull(authResponse.getToken());
        assertEquals(jwtService.extractUsername(authResponse.getToken()), loginRequest.getName());
        System.out.println(authResponse.getToken());
    }

    @Test
    public void testUserCanLikeReports() {
        Report report = new Report();
        report.setEmergency(false);
        report.setType(Type.FLOOD);
        report.setTime("yoo");
        report.setIssuer("");
        report.setLatitude(6.788);
        report.setLongitude(3.29);
        reportRepository.save(report);

    }
}
