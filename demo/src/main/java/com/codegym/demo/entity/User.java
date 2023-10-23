package com.codegym.demo.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "USER")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "AGE")
    private int age;

    @Column(name = "BANK_ACCOUNT_NUMBER")
    private String bankAccountNumber;

    @Column(name = "MONEY")
    private long money;
}
