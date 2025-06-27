package com.bytebuilder.controller;

import com.bytebuilder.dto.request.LogInRequest;
import com.bytebuilder.dto.request.ReportInteractRequest;
import com.bytebuilder.dto.request.SignUpRequest;
import com.bytebuilder.dto.request.UpdateLocationRequest;
import com.bytebuilder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("reachh")
    public String hola(){
        return "jesse";
    }


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

    @PostMapping("/like")
    public ResponseEntity<?> like(@RequestBody ReportInteractRequest reportInteractRequest ) {
        try {
            return ResponseEntity.ok(userService.like(reportInteractRequest));
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

    @GetMapping("/getLocation")
    public ResponseEntity<?> getLocation(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getLocation(userDetails.getUsername()));
    }

    @PostMapping("/updateLocation")
    public ResponseEntity<?> updateLocation(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestBody UpdateLocationRequest updateLocationRequest,
                                            BindingResult bindingResult) {

        try {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
            }

            String username = userDetails.getUsername();
            userService.updateLocation(username, updateLocationRequest);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
