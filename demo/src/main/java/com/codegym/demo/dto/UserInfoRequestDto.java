package com.codegym.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoRequestDto {

    private long id;

    private String username;

    private String password;

    private String fullName;

    private String phoneNumber;

    private int age;

    private String bankAccountNumber;

    private long money;
}
