package com.codegym.demo.service;

import com.codegym.demo.dto.LoginRequestDto;
import com.codegym.demo.dto.RegisterRequestDto;
import com.codegym.demo.dto.UserInfoRequestDto;
import com.codegym.demo.entity.User;

import java.util.List;

public interface IUserService {
    String login(LoginRequestDto authenticationRequest);

    User register(RegisterRequestDto registerRequest);

    User getUserByUsername(String username);

    User getUserById(long id);

    List<User> getAllUser();

    List<User> getAll();

    void edit(UserInfoRequestDto user);

    void delete(String username);

    boolean transferMoneyToMomo(String phoneFrom, String phoneTo, long money);

    boolean transferMoneyToBank(String phoneNumber, String bankAccountNumber, long money);

    void recharge(String phoneNumber, long money);

    boolean isMomoAccount(String phoneNumber);

}
