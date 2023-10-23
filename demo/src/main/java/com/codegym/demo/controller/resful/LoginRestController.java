package com.codegym.demo.controller.resful;

import com.codegym.demo.dto.JwtResponse;
import com.codegym.demo.dto.LoginRequestDto;
import com.codegym.demo.dto.RegisterRequestDto;
import com.codegym.demo.entity.User;
import com.codegym.demo.security.JwtTokenUtil;
import com.codegym.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class LoginRestController {

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping(value = "/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequestDto authenticationRequest){

        String token = userService.login(authenticationRequest);
        String username = jwtTokenUtil.getUsernameFromJWT(token);
        User user = userService.getUserByUsername(username);
        String role = user.getRole();

        return ResponseEntity.ok(new JwtResponse(token,role));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto registerRequest){
        User user = userService.register(registerRequest);
        return ResponseEntity.ok(user);
    }

}
