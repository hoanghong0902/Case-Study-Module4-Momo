package com.codegym.demo.service.impl;

import com.codegym.demo.dto.UserInfoRequestDto;
import com.codegym.demo.security.JwtTokenUtil;
import com.codegym.demo.constraint.ERole;
import com.codegym.demo.dto.LoginRequestDto;
import com.codegym.demo.dto.RegisterRequestDto;
import com.codegym.demo.entity.User;
import com.codegym.demo.repository.IUserRepository;
import com.codegym.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String login(LoginRequestDto authenticationRequest) {
        User user = userRepository.findByUsername(authenticationRequest.getUsername());

        if(!passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword())){
            throw new AuthenticationServiceException("Wrong password");
        }
        return jwtTokenUtil.generateToken(user);
    }

    @Override
    @Transactional
    public User register(RegisterRequestDto registerRequest) {
        String password = registerRequest.getPassword();
        String cypherText = passwordEncoder.encode(password);
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(cypherText);
        user.setAge(registerRequest.getAge());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setFullName(registerRequest.getFullName());
        user.setBankAccountNumber(registerRequest.getBankAccountNumber());
        user.setRole("ROLE_".concat(ERole.USER.toString()));
        user.setMoney(0);
        userRepository.save(user);
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public List<User> getAllUser() {
        List<User> users = userRepository.findAll();
        List<User> userList = new ArrayList<>();
        for (User user : users){
            if(user.getRole().equals("ROLE_USER")){
                userList.add(user);
            }
        }
        return userList;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void edit(UserInfoRequestDto user) {
        String password = user.getPassword();
        String cypherText = passwordEncoder.encode(password);
        User users = userRepository.findById(user.getId());
        users.setUsername(user.getUsername());
        users.setFullName(user.getFullName());
        users.setPhoneNumber(users.getPhoneNumber());
        users.setAge(user.getAge());
        users.setPassword(cypherText);
        users.setBankAccountNumber(user.getBankAccountNumber());
        userRepository.save(users);
    }

    @Override
    @Transactional
    public void delete(String username) {
        User user = userRepository.findByUsername(username);
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public boolean transferMoneyToMomo(String phoneFrom, String phoneTo, long money) {
        User user = userRepository.findByPhoneNumber(phoneFrom);
        long moneys = user.getMoney();
        if(!isMomoAccount(phoneTo) || moneys < money || money < 0){
            return false;
        }
        User user1 = userRepository.findByPhoneNumber(phoneFrom);
        User user2 = userRepository.findByPhoneNumber(phoneTo);
        user1.setMoney(moneys - money);
        user2.setMoney(user2.getMoney() + money);
        userRepository.save(user1);
        userRepository.save(user2);
        return true;
    }

    @Override
    @Transactional
    public boolean transferMoneyToBank(String phoneNumber, String bankAccountNumber, long money) {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        long moneys = user.getMoney();
        if(moneys < money || money < 0){
            return false;
        }
        user.setMoney(moneys - money);
        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional
    public void recharge(String phoneNumber, long money) {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        user.setMoney(user.getMoney() + money);
        userRepository.save(user);
    }

    @Override
    public boolean isMomoAccount(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        return user != null;
    }


}
