package com.nfv.controller;

import java.util.Iterator;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nfv.entity.Tenant;
import com.nfv.entity.User;
import com.nfv.entity.Vim;
import com.nfv.error.NFVException;
import com.nfv.repository.TenantRepository;
import com.nfv.repository.VimRepository;
import com.nfv.utils.NFVUtility;
import com.nfv.utils.RestClient;
import com.nfv.utils.TenantCreationUtil;

@RestController
public class TenantController {
	
	private static final String TENANT = "tenant";
	
	@Value("${content.type.key}")
	String contentTypeKey;

	@Value("${content.type.value}")
	String contentTypeValue;

	@Value("${auth.token.key}")
	String authTokenKey;

	@Value("${post.openstack.project.uri}")
	String postOSProjectURI;

	@Value("${post.openstack.users.uri}")
	String postOSUsersURI;

	@Value("${get.openstack.roles.uri}")
	String getOSRolesURI;

	@Value("${get.floatingips.uri}")
	String getFloatingIPsURI;

	@Resource
	TenantRepository tenantRepository;

	@Resource
	VimRepository vimRepository;
	
	@Autowired
	RestClient restClient;

	@Autowired
	NFVUtility utility;
	
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping(value = "/tenant/{id}/vim", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Vim>> getVim(@PathVariable("id") Long id) throws NFVException {
		User user = utility.getUser();
		if (!utility.isAdminUser(user))
			throw new NFVException(messageSource.getMessage("user.unauthorized", null, utility.getLocale()), HttpStatus.UNAUTHORIZED);
		Tenant tenant = tenantRepository.findOne(id);
		if (tenant != null)
			return new ResponseEntity<>(tenant.getVim(), HttpStatus.OK);
		else
			throw new NFVException(messageSource.getMessage("not.found", new String[]{TENANT}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/tenant/{id}/vimunassigned", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Vim>> getVimUnassigned(@PathVariable("id") Long id) throws NFVException {
		User user = utility.getUser();
		if (!utility.isAdminUser(user))
			throw new NFVException(messageSource.getMessage("user.unauthorized", null, utility.getLocale()), HttpStatus.UNAUTHORIZED);
		Tenant tenant = tenantRepository.findOne(id);
		if (tenant != null) {
			Iterable<Vim> allVims = vimRepository.findAll();
			Iterable<Vim> assignedVims = getVim(id).getBody();
			for (Iterator<Vim> i = allVims.iterator(); i.hasNext();) {
				Vim v1 = i.next();
				for (Iterator<Vim> j = assignedVims.iterator(); j.hasNext();) {
					Vim v2 = j.next();
					if (v1.getId() == v2.getId()) {
						i.remove();
						j.remove();
					}
				}
			}
			return new ResponseEntity<>(allVims, HttpStatus.OK);
		}
		else
			throw new NFVException(messageSource.getMessage("not.found", new String[]{TENANT}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/tenant/{id}/vim", method = RequestMethod.PATCH)
	public ResponseEntity<Void> updateVim(@PathVariable("id") Long id, @RequestBody Vim vim) throws NFVException {
		User user = utility.getUser();
		if (!utility.isAdminUser(user))
			throw new NFVException(messageSource.getMessage("user.unauthorized", null, utility.getLocale()), HttpStatus.UNAUTHORIZED);
		Tenant tenant = tenantRepository.findOne(id);
		if (tenant != null) {
			TenantCreationUtil tenantCreationUtil = new TenantCreationUtil();
			tenantCreationUtil.set(restClient, contentTypeKey, contentTypeValue, authTokenKey, postOSProjectURI, postOSUsersURI, getOSRolesURI, tenantRepository, tenant, getFloatingIPsURI, vim);
			tenantCreationUtil.start();
			return new ResponseEntity<>(HttpStatus.OK);
		}
		throw new NFVException(messageSource.getMessage("not.found", new String[]{TENANT}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

}
