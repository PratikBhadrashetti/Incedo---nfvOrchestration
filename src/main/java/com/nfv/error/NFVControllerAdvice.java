package com.nfv.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.nfv.utils.NFVUtility;

@ControllerAdvice
public class NFVControllerAdvice {
	private static final Logger logger = LoggerFactory.getLogger(NFVControllerAdvice.class);
	
	@Autowired
	NFVUtility utility;
	
	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler(NFVException.class)
	public ResponseEntity<NFVError> customExceptionHandler(NFVException ex) {
		NFVError error = new NFVError();
		error.setError(ex.getMessage());
		return new ResponseEntity<>(error, ex.getHttpStatus());
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<NFVError> runtimeExceptionHandler(RuntimeException ex) {
		NFVError error = new NFVError();
		error.setError(messageSource.getMessage("unexpected.error", null, utility.getLocale()));
		logger.error("General error: ", ex);
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
