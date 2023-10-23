package com.codegym.demo.repository;

import com.codegym.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    User findByPhoneNumber(String phoneNumber);

    User findById(long id);

}
