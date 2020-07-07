package com.drive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.drive.model.Role;
@Repository
public interface RoleRepository extends JpaRepository<Role , Integer> {
	Role findByRole(String role);

}
