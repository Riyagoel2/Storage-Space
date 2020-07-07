package com.drive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.drive.model.File;
import com.drive.model.User;
import com.drive.payload.UploadResponse;
import com.drive.repository.FileRepository;
import com.drive.repository.UserRepository;
import com.drive.service.FileService;
import com.drive.service.UserService;

@RestController
public class FileController {

	@Autowired
	FileService fileService;
	@Autowired
	UserRepository userRepo;
	@Autowired
	UserService userService;
	@Autowired
	FileRepository fileRepo;

	@PostMapping(value = "/home/uploadFile/{userid}")
	public UploadResponse uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("userid") int userid,
			Model model) {
		File dbfile = fileService.storeFile(file, userid);
		User user = userRepo.findById(userid);
		user.getUserfiles().add(dbfile);
		System.out.println(user.getUserfiles().toString());
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("file/")
				.path(Integer.toString(dbfile.getFileId())).path("/0R3SAYCCGRRW").toUriString();
		return new UploadResponse(dbfile.getFilename(), fileDownloadUri, file.getContentType(), file.getSize(),
				dbfile.getUser(), dbfile.isShared());
	}

	@PostMapping(value = "/home")
	public ModelAndView shareFile(@RequestParam("fileid") int fileid, @RequestParam("radioName") String privacy) {
		File getfile = fileRepo.findById(fileid);

		if (privacy.equals("private")) {

			getfile.setShared(false);
		} else {

			getfile.setShared(true);
		}

		fileRepo.save(getfile);
		return new ModelAndView("redirect:/home");
	}

	@GetMapping(value = "/file/{fileId}/0R3SAYCCGRRW")
	public ResponseEntity<Resource> downloadFile(@PathVariable int fileId) {
		File dbfile = fileService.getFile(fileId);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByUserName(auth.getName());
		if (dbfile.isShared() == false) {
			if (user.getId() == dbfile.getUser().getId()) {
				return ResponseEntity.ok().contentType(MediaType.parseMediaType(dbfile.getFiletype()))
						.header(HttpHeaders.CONTENT_DISPOSITION,
								"attachment; filename=\"" + dbfile.getFilename() + "\"")
						.body(new ByteArrayResource(dbfile.getData()));

			} else {
				return null;
			}
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(dbfile.getFiletype()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbfile.getFilename() + "\"")
				.body(new ByteArrayResource(dbfile.getData()));

	}

	@GetMapping(value = "/upload-file")
	public ModelAndView uploadFile(Model model)

	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByUserName(auth.getName());
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("file/uploadFile");
		model.addAttribute("user", user);
		return modelAndView;
	}

}
