package com.nfv.controller;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
public class VimController {

	@Resource
	VimRepository vimRepository;

	@Resource
	TenantRepository tenantRepository;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	NFVUtility utility;

	@RequestMapping(value = "/vim", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Vim>> get() throws NFVException {
		User user = utility.getUser();
		return new ResponseEntity<>(user.getTenant().getVim(), HttpStatus.OK);
//		if (utility.isAdminUser(user))
//			return new ResponseEntity<>(vimRepository.findAll(), HttpStatus.OK);
//		else
//			throw new NFVException(messageSource.getMessage("user.unauthorized", null, utility.getLocale()), HttpStatus.UNAUTHORIZED);
	}

	@RequestMapping(value = "/vim/{id}", method = RequestMethod.GET)
	public ResponseEntity<Vim> get(@PathVariable("id") Long id) throws NFVException {
		User user = utility.getUser();
		if (utility.isAdminUser(user))
			return new ResponseEntity<>(vimRepository.findOne(id), HttpStatus.OK);
		else
			throw new NFVException(messageSource.getMessage("user.unauthorized", null, utility.getLocale()), HttpStatus.UNAUTHORIZED);
	}

	@RequestMapping(value = "/vim", method = RequestMethod.POST)
	public ResponseEntity<Vim> create(@RequestBody Vim vim) throws NFVException {
		User user = utility.getUser();
		if (utility.isAdminUser(user)) {
			Vim v = vimRepository.save(vim);
			Tenant t = user.getTenant();
			Set<Vim> vims = t.getVim();
			vims.add(v);
			t.setVim(vims);
			tenantRepository.save(t);
			return new ResponseEntity<>(v, HttpStatus.OK);
		}
		else
			throw new NFVException(messageSource.getMessage("user.unauthorized", null, utility.getLocale()), HttpStatus.UNAUTHORIZED);
	}

	@RequestMapping(value = "/vim/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws NFVException {
		User user = utility.getUser();
		if (utility.isAdminUser(user)) {
			vimRepository.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else
			throw new NFVException(messageSource.getMessage("user.unauthorized", null, utility.getLocale()), HttpStatus.UNAUTHORIZED);
	}

}
