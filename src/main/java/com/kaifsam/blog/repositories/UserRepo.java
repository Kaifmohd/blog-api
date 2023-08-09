package com.kaifsam.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaifsam.blog.entities.User;

public interface UserRepo extends JpaRepository<User, Integer> {

}
