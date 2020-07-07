package com.drive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.drive.model.User;
@Repository
public interface UserRepository extends JpaRepository<  User , Integer> {
	User findByEmail(String email);

	User findById(int id);

	User findByUsername(String username);

 

}
