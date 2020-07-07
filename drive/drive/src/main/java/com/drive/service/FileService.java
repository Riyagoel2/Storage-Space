package com.drive.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.drive.exception.FileStorageException;
import com.drive.model.File;
import com.drive.model.User;
import com.drive.repository.FileRepository;
import com.drive.repository.UserRepository;

@Service
public class FileService {
	@Autowired
	FileRepository fileRepository;

	@Autowired
	UserRepository userRepo;
	
	public File storeFile(MultipartFile file , int userid) 
	{  
		String filename=StringUtils.cleanPath(file.getOriginalFilename());
		  try {
	            // Check if the file's name contains invalid characters
	            if(filename.contains("..")) {  
	                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + filename);
	            }
        User user= userRepo.findById(userid);   
		File dbfile=new File(filename,file.getContentType() , file.getBytes() , user, false );  
		return fileRepository.save(dbfile);
	}
		  
		  catch(Exception ex)
		  {
			  throw new FileStorageException("Could not store file " + filename + ". Please try again later!", ex);
		  }
}
	
	
	public File getFile(int id)
	{
		return fileRepository.findById(id);
		 
	}
	
	public Set<File> getFileByUser(int userid)
	{
		return fileRepository.findByUser_Id(userid);
	}
}
