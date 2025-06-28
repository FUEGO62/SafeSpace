package com.bytebuilder.controller;

import com.bytebuilder.data.model.Report;
import com.bytebuilder.dto.request.*;
import com.bytebuilder.integration.CloudService;
import com.bytebuilder.service.UserService;
import com.bytebuilder.util.JwtService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;


import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {


    @Autowired
    private CloudService cloudService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("reach")
    public String hello(){
        return "ma sere";
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        try {
            return ResponseEntity.ok(userService.register(signUpRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/viewInbox")
    public ResponseEntity<?> viewInbox(@RequestBody ReportInteractRequest request){
        try {
            return ResponseEntity.ok(userService.viewInbox(request));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/viewComments")
    public ResponseEntity<?> viewComments(@RequestBody ReportInteractRequest request) {
        try {
            return ResponseEntity.ok(userService.viewComments(request));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/createReport")
    public ResponseEntity<?> createReport(@RequestBody CreateReportRequest createReportRequest) {
       try {

            userService.createReport(createReportRequest);
            return ResponseEntity.ok("Report created");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image) {
        try {
            String blobId = cloudService.upload(image);
            return ResponseEntity.ok(blobId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Upload failed: " + e.getMessage());
        }
    }

    @PostMapping("/like")
    public ResponseEntity<?> like(@RequestBody ReportInteractRequest reportInteractRequest ) {
        try {
            return ResponseEntity.ok(userService.like(reportInteractRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestBody ReportInteractRequest reportInteractRequest) {
        try {
           return ResponseEntity.ok(userService.confirm(reportInteractRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/deny")
    public ResponseEntity<?> deny(@RequestBody ReportInteractRequest reportInteractRequest) {
        try{
            return ResponseEntity.ok(userService.deny(reportInteractRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/dislike")
    public ResponseEntity<?> dislike(@RequestBody ReportInteractRequest reportInteractRequest ) {
        try {
            return ResponseEntity.ok(userService.dislike(reportInteractRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/logIn")
    public ResponseEntity<?> logIn(@RequestBody LogInRequest logInRequest) {
        try {
            return ResponseEntity.ok(userService.login(logInRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/getLocation")
    public ResponseEntity<?> getLocation(@RequestBody Token token) {
        String username  = jwtService.extractUsername(token.getToken());
        return ResponseEntity.ok(userService.getLocation(username));
    }

    @PostMapping("/comment")
    public ResponseEntity<?> comment(@RequestBody CommentRequest commentRequest) {
        try {
            return ResponseEntity.ok(userService.comment(commentRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/viewReport")
    public ResponseEntity<?> viewReport(@RequestBody ViewReportRequest viewReportRequest) {
        try {
            return ResponseEntity.ok(userService.viewReport(viewReportRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/updateLocation")
    public ResponseEntity<?> updateLocation(@RequestBody UpdateLocationRequest updateLocationRequest,
                                            BindingResult bindingResult) {

        try {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
            }

            String username = jwtService.extractUsername(updateLocationRequest.getToken());
            userService.updateLocation(username, updateLocationRequest);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
