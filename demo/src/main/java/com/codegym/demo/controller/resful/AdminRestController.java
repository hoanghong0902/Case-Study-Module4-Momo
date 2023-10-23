package com.codegym.demo.controller.resful;

import com.codegym.demo.dto.UserInfoRequestDto;
import com.codegym.demo.entity.User;
import com.codegym.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin")
public class AdminRestController {

    @Autowired
    private IUserService userService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(){
        List<User> users = userService.getAll();
        if(users!=null) {
            return new ResponseEntity<>(users, HttpStatus.ALREADY_REPORTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUser(){
        List<User> users = userService.getAllUser();
        if(users!=null) {
            return new ResponseEntity<>(users, HttpStatus.ALREADY_REPORTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/viewUserInfo/{id}")
    public ResponseEntity<?> viewUserInfo(@PathVariable long id){
        User user = userService.getUserById(id);
        if(user!=null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editUser(@RequestBody UserInfoRequestDto user){
        userService.edit(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        User user = userService.getUserById(id);
        userService.delete(user.getUsername());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
