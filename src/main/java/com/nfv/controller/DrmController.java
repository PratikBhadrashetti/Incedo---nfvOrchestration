package com.nfv.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nfv.error.NFVException;
import com.nfv.utils.NFVUtility;
import com.nfv.utils.RestClient;

@RestController
public class DrmController {
	private static final Logger logger = LoggerFactory.getLogger(DrmController.class);

	@Value("${disable.drm}")
	String disableDrm;

	@Value("${drm.statistics}")
	String getDrmStatisticsURI;

	@Autowired
	RestClient restClient;

	@Autowired
	NFVUtility utility;

	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping(value = "/drm/statistics", method = RequestMethod.GET)
	private ResponseEntity<String> openStackInstanceLimits() throws NFVException {
		if (Boolean.parseBoolean(disableDrm))
			return null;
		try {		
			return new ResponseEntity<>(restClient.get(getDrmStatisticsURI, null), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("", e);
			throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
}