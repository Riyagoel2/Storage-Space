package com.drive.exception;

public class FileStorageException  extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FileStorageException(String string) {
		 super(string);
	}

	public FileStorageException(String string, Throwable ex) {
		super(string , ex);
	}

}
