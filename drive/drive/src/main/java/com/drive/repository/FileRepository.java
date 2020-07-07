package com.drive.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.drive.model.File;
@Repository
public interface FileRepository extends JpaRepository<File , Integer> {
	Set<File> findByUser_Id(int userid);
	File findById(int fileid);
	

}
