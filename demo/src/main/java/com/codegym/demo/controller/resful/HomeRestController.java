package com.codegym.demo.controller.resful;

import com.codegym.demo.dto.RechargeRequestDto;
import com.codegym.demo.dto.TransferMoneyToBankRequestDto;
import com.codegym.demo.dto.TransferMoneyToMomoRequestDto;
import com.codegym.demo.dto.UserInfoRequestDto;
import com.codegym.demo.entity.User;
import com.codegym.demo.security.JwtRequestFilter;
import com.codegym.demo.security.JwtTokenUtil;
import com.codegym.demo.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class HomeRestController {

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/profile")
    public ResponseEntity<?> createAuthenticationToken(HttpServletRequest request){
        String token = jwtRequestFilter.getJwtFromRequest(request);
        String username = jwtTokenUtil.getUsernameFromJWT(token);
        UserInfoRequestDto user = new UserInfoRequestDto();
        BeanUtils.copyProperties(userService.getUserByUsername(username), user);
        return ResponseEntity.ok(user);
    }

    @PutMapping ("/edit")
    public ResponseEntity<?> editInfo(@RequestBody UserInfoRequestDto user){
        userService.edit(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(){
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/transferMoneyToMomo")
    public ResponseEntity<String> transferMoneyToMomo(@RequestBody TransferMoneyToMomoRequestDto transferMoneyToMomoRequestDto, HttpServletRequest request){
        String token = jwtRequestFilter.getJwtFromRequest(request);
        String username = jwtTokenUtil.getUsernameFromJWT(token);
        User user = userService.getUserByUsername(username);
        String phone = transferMoneyToMomoRequestDto.getPhoneNumber();
        long money = transferMoneyToMomoRequestDto.getMoney();

        if (!userService.isMomoAccount(phone)){
            String message = "Not Account Momo";
            return new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
        } else if (user.getMoney() < money || money < 0 || user.getMoney() <= 0){
            String message = "Not Enough Money";
            return new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
        } else if (userService.transferMoneyToMomo(user.getPhoneNumber(),phone,money)){
            String message = "ok";
            return new ResponseEntity<String>(message, HttpStatus.OK);
        }

        return new ResponseEntity<>("Transfer fail", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/transferMoneyToBank")
    public ResponseEntity<String> transferMoneyToBank(@RequestBody TransferMoneyToBankRequestDto transferMoneyToBankRequestDto, HttpServletRequest request){
        String token = jwtRequestFilter.getJwtFromRequest(request);
        String username = jwtTokenUtil.getUsernameFromJWT(token);
        User user = userService.getUserByUsername(username);
        String bankAccount = transferMoneyToBankRequestDto.getBankAccountNumber();
        long money = transferMoneyToBankRequestDto.getMoney();

        if (user.getMoney() < money || money < 0 || user.getMoney() <= 0){
            String message = "Not Enough Money";
            return new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
        } else {
            userService.transferMoneyToBank(user.getPhoneNumber(),bankAccount,money);
            String message = "ok";
            return new ResponseEntity<String>(message, HttpStatus.OK);
        }
    }

    @PostMapping("/recharge")
    public ResponseEntity<String> recharge(@RequestBody RechargeRequestDto rechargeRequestDto, HttpServletRequest request){
        String token = jwtRequestFilter.getJwtFromRequest(request);
        String username = jwtTokenUtil.getUsernameFromJWT(token);
        User user = userService.getUserByUsername(username);
        long money = rechargeRequestDto.getMoney();

        if (money < 0){
            String message = "Not Enough Money";
            return new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
        } else {
            userService.recharge(user.getPhoneNumber(),money);
            String message = "ok";
            return new ResponseEntity<String>(message, HttpStatus.OK);
        }
    }
}
