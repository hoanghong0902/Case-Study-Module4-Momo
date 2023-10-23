package com.codegym.demo.controller;

import com.codegym.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewController {

    @GetMapping("/admin")
    public ModelAndView admin(){
        return new ModelAndView("admin");
    }

    @GetMapping("/login")
    public ModelAndView login(){
        return new ModelAndView("login");
    }

    @GetMapping("/home")
    public ModelAndView home(){
        return new ModelAndView("home");
    }


}
