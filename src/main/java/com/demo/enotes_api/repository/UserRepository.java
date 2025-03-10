package com.demo.enotes_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.enotes_api.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	Boolean existsByEmail(String email);

	User findByEmail(String email);

}
