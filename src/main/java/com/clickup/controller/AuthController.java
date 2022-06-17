package com.clickup.controller;

import com.clickup.entity.User;
import com.clickup.payload.ApiResponse;
import com.clickup.payload.LoginDto;
import com.clickup.payload.RegisterDto;
import com.clickup.security.JwtProvider;
import com.clickup.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/register")
    public HttpEntity<?> registerUser(@Valid @RequestBody RegisterDto registerDto){
        ApiResponse apiResponse=authService.registerUser(registerDto);
        return ResponseEntity.status(apiResponse.isSucces()?200:409).body(apiResponse);
    }
    @PostMapping("/login")
    public HttpEntity<?> login(@Valid @RequestBody LoginDto loginDto){
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            User user = (User)authenticate.getPrincipal();
            String token = jwtProvider.generateToken(user.getEmail());
            return ResponseEntity.ok(token);
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse("parol yoki login xato ", false));
        }
    }
    @PutMapping("/verifyEmail")
    public HttpEntity<?> verifyEmail(@RequestParam String email, @RequestParam String emailCode){
        ApiResponse apiResponse=authService.verifyEmail(email,emailCode);
        return ResponseEntity.status(apiResponse.isSucces()?200:409).body(apiResponse);
    }
}
