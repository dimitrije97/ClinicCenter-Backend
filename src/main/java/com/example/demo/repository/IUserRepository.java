package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IUserRepository extends JpaRepository<User, UUID> {

    User findOneById(UUID id);

    User findOneByEmail(String email);

    List<User> findAllByDeleted(boolean deleted);
}
