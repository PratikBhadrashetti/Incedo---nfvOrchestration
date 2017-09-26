package com.nfv.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nfv.entity.Catalog;
import com.nfv.entity.CatalogLogo;
import com.nfv.entity.User;
import com.nfv.entity.Vim;
import com.nfv.entity.Vnf;
import com.nfv.entity.VnfInstance;
import com.nfv.error.NFVException;
import com.nfv.repository.CatalogLogoRepository;
import com.nfv.repository.CatalogRepository;
import com.nfv.repository.UserRepository;
import com.nfv.repository.VimRepository;
import com.nfv.repository.VnfInstanceRepository;
import com.nfv.repository.VnfRepository;
import com.nfv.utils.NFVUtility;
import com.nfv.utils.RestClient;

@RestController
public class CatalogController {
	
	private static final String CATALOG = "catalog";
	
	private static final String LOGO = "logo";
	
	@Resource
	CatalogRepository catalogRepository;

	@Resource
	CatalogLogoRepository catalogLogoRepository;
	
	@Resource
	VnfRepository vnfRepository;
	
	@Resource
	VnfInstanceRepository vnfInstanceRepository;
	
	@Resource
	VimRepository vimRepository;
	
	@Resource
	VnfController vnfController;

	@Resource
	UserRepository userRepository;
	
	@Resource
	UserController userController;

	@Autowired
	RestClient restClient;
	
	@Autowired
	NFVUtility utility;
	
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping(value = "/vim/{id}/catalog", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Catalog>> get(@PathVariable("id") Long id) throws NFVException {
		User user = utility.getUser();
		if (utility.isAdminUser(user))
			return new ResponseEntity<>(catalogRepository.findByVim(vimRepository.findOne(id)), HttpStatus.OK);
		else if (utility.isTenantAdminUser(user))
			return new ResponseEntity<>(catalogRepository.findByTenantAndVimOrTenantIsNullAndVim(user.getTenant(), vimRepository.findOne(id), vimRepository.findOne(id)), HttpStatus.OK);
		else {
			Iterable<Catalog> catalogs = userController.getCatalog(id, user.getId()).getBody();
			Set<Catalog> filteredCatalogs = new HashSet<>();
			Vim vim = vimRepository.findOne(id);
			for (Catalog catalog: catalogs) {
				if (catalog.getVim().getId() == vim.getId()) {
					filteredCatalogs.add(catalog);
				}
			}
			return new ResponseEntity<>(filteredCatalogs, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/catalog/{id}", method = RequestMethod.GET)
	public ResponseEntity<Catalog> getCatalog(@PathVariable("id") Long id) throws NFVException {
		Catalog catalog = catalogRepository.findOne(id);
		if (catalog != null) {
			utility.checkPermissions(catalog);
			return new ResponseEntity<>(catalog, HttpStatus.OK);
		}
		else
			throw new NFVException(messageSource.getMessage("not.found", new String[]{CATALOG}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/catalog/{id}/vnf", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Vnf>> getVnf(@PathVariable("id") Long id) throws NFVException {
		Catalog catalog = catalogRepository.findOne(id);
		if (catalog != null) {
			utility.checkPermissions(catalog);
			return new ResponseEntity<>(catalog.getVnf(), HttpStatus.OK);
		}
		else
			throw new NFVException(messageSource.getMessage("not.found", new String[]{CATALOG}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/catalog/{id}/vnfMinus", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Vnf>> getVnfMinus(@PathVariable("id") Long id) throws NFVException {
		utility.checkAdminPermissions();
		Catalog c = getCatalog(id).getBody();
		Iterable<Vnf> allVnfs = vnfController.get(c.getVim().getId()).getBody();
		Iterable<Vnf> cVnfs = getVnf(id).getBody();
		
		for (Iterator<Vnf> i = allVnfs.iterator(); i.hasNext();) {
			Vnf v1 = i.next();
			for (Iterator<Vnf> j = cVnfs.iterator(); j.hasNext();) {
				Vnf v2 = j.next();
				if (v1.getId() == v2.getId()) {
					i.remove();
					break;
				}
			}
		}
		return new ResponseEntity<>(allVnfs, HttpStatus.OK);
	}

	@RequestMapping(value = "/catalog/{id}/user", method = RequestMethod.GET)
	public ResponseEntity<Iterable<User>> getUser(@PathVariable("id") Long id) throws NFVException {
		Catalog catalog = catalogRepository.findOne(id);
		if (catalog != null) {
			User user = utility.getUser();
			if (utility.isAdminUser(user))
				return new ResponseEntity<>(catalog.getUser(), HttpStatus.OK);
			else {
				if (utility.isTenantAdminUser(user)) {
					if (catalog.getTenant() == null || catalog.getTenant() == user.getTenant())
						return new ResponseEntity<>(userRepository.findByCatalogAndTenant(catalog, user.getTenant()), HttpStatus.OK);
					else
						throw new NFVException(messageSource.getMessage("user.unauthorized", null, utility.getLocale()), HttpStatus.UNAUTHORIZED);
				} else
					throw new NFVException(messageSource.getMessage("user.unauthorized", null, utility.getLocale()), HttpStatus.UNAUTHORIZED);
			}
		}
		else
			throw new NFVException(messageSource.getMessage("not.found", new String[]{CATALOG}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/catalog/{id}/vnfinstance", method = RequestMethod.GET)
	public ResponseEntity<Iterable<VnfInstance>> getVnfInstance(@PathVariable("id") Long id) throws NFVException {
		Catalog catalog = catalogRepository.findOne(id);
		if (catalog != null) {
			User user = utility.getUser();
			if (utility.isAdminUser(user))
				return new ResponseEntity<>(catalog.getVnfInstance(), HttpStatus.OK);
			else {
				if (utility.isTenantAdminUser(user)) {
					if (catalog.getTenant() == null || catalog.getTenant() == user.getTenant()) {
						return new ResponseEntity<>(vnfInstanceRepository.findByCatalogAndTenant(catalog, user.getTenant()), HttpStatus.OK);
					} else
						throw new NFVException(messageSource.getMessage("user.unauthorized", null, utility.getLocale()), HttpStatus.UNAUTHORIZED);
				} else {
					if (catalog.getUser().contains(user)) {
						return new ResponseEntity<>(vnfInstanceRepository.findByCatalogAndTenantAndUser(catalog, user.getTenant(), user), HttpStatus.OK);
					} else
						throw new NFVException(messageSource.getMessage("user.unauthorized", null, utility.getLocale()), HttpStatus.UNAUTHORIZED);
				}
			}
		}
		else
			throw new NFVException(messageSource.getMessage("not.found", new String[]{CATALOG}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/catalog/{id}/logo", produces = MediaType.ALL_VALUE, method = RequestMethod.GET)
	public ResponseEntity<byte[]> getLogo(@PathVariable("id") Long id) throws NFVException {
		Catalog catalog = catalogRepository.findOne(id);
		if (catalog != null) {
			utility.checkPermissions(catalog);
			CatalogLogo logo = catalog.getCatalogLogo();
			if (logo != null) {
				return new ResponseEntity<>(logo.getLogo(), HttpStatus.OK);
			}
			throw new NFVException(messageSource.getMessage("not.found", new String[]{LOGO}, utility.getLocale()), HttpStatus.BAD_REQUEST);
		}
		throw new NFVException(messageSource.getMessage("not.found", new String[]{CATALOG}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/catalog", method = RequestMethod.POST)
	public ResponseEntity<Catalog> create(@RequestBody Catalog catalog) throws NFVException {
		utility.checkAdminPermissions();
		return new ResponseEntity<>(catalogRepository.save(catalog), HttpStatus.OK);
	}

	@RequestMapping(value = "/catalog/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<Void> update(@PathVariable("id") Long id, @RequestBody Catalog c) throws NFVException {
		Catalog catalog = catalogRepository.findOne(id);
		if (catalog != null) {
			utility.checkAdminPermissions();
			utility.checkPermissions(catalog);
			catalog.setName(c.getName());
			catalog.setDescription(c.getDescription());
			catalogRepository.save(catalog);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		throw new NFVException(messageSource.getMessage("not.found", new String[]{CATALOG}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/catalog/{id}/vnf", method = RequestMethod.PATCH)
	public ResponseEntity<Void> updateVnf(@PathVariable("id") Long id, @RequestBody Set<Vnf> vnf) throws NFVException {
		Catalog catalog = catalogRepository.findOne(id);
		if (catalog != null) {
			utility.checkAdminPermissions();
			utility.checkPermissions(catalog);
			catalog.setVnf(vnf);
			catalogRepository.save(catalog);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		throw new NFVException(messageSource.getMessage("not.found", new String[]{CATALOG}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/catalog/{id}/vnfAdd", method = RequestMethod.PATCH)
	public ResponseEntity<Void> updateVnfAdd(@PathVariable("id") Long id, @RequestBody Set<Vnf> vnf) throws NFVException {
		Catalog catalog = catalogRepository.findOne(id);
		if (catalog != null) {
			utility.checkAdminPermissions();
			utility.checkPermissions(catalog);
			vnf.addAll(catalog.getVnf());
			catalog.setVnf(vnf);
			catalogRepository.save(catalog);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		throw new NFVException(messageSource.getMessage("not.found", new String[]{CATALOG}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/catalog/{id}/user", method = RequestMethod.PATCH)
	public ResponseEntity<Void> updateUser(@PathVariable("id") Long id, @RequestBody Set<User> user) throws NFVException {
		Catalog catalog = catalogRepository.findOne(id);
		if (catalog != null) {
			utility.checkAdminPermissions();
			utility.checkPermissions(catalog);
			User loggedInUser = utility.getUser();
			Set<VnfInstance> list;
			if (utility.isAdminUser(loggedInUser))
				list = vnfInstanceRepository.findByCatalog(catalog);
			else
				list = vnfInstanceRepository.findByCatalogAndTenant(catalog, loggedInUser.getTenant());
			
			for (VnfInstance vnfInstance: list) {
				Set<User> users = vnfInstance.getUser();
				for (Iterator<User> i = users.iterator(); i.hasNext(); ) {
					User u = i.next();
					if (utility.isTenantAdminUser(loggedInUser) && u.getTenant() != loggedInUser.getTenant())
						continue;
					boolean found = false;
					for (User v: user) {
						if (u.getId() == v.getId()) {
							found = true;
							break;
						}
					}
					if (!found) {
						i.remove();
					}
				}
				vnfInstance.setUser(users);
				vnfInstanceRepository.save(vnfInstance);
			}
			
			if (utility.isAdminUser(loggedInUser))
				catalog.setUser(user);
			else {
				Set<User> users = userRepository.findByCatalogAndTenantIsNot(catalog, loggedInUser.getTenant());
				users.addAll(user);
				catalog.setUser(users);
			}
			catalogRepository.save(catalog);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		throw new NFVException(messageSource.getMessage("not.found", new String[]{CATALOG}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/catalog/{id}/logo", method = RequestMethod.PATCH)
	public ResponseEntity<Void> updateLogo(@PathVariable("id") Long id, @RequestParam(value = "icon", required = true) MultipartFile icon) throws NFVException {
		Catalog catalog = catalogRepository.findOne(id);
		if (catalog != null) {
			utility.checkAdminPermissions();
			utility.checkPermissions(catalog);
			CatalogLogo catalogLogo = catalog.getCatalogLogo();
			if (null == catalogLogo)
				catalogLogo = new CatalogLogo();
			try {
				catalogLogo.setLogo(icon.getBytes());
			} catch (IOException e) {
				throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.BAD_REQUEST);
			}
			catalogLogoRepository.save(catalogLogo);
			catalog.setCatalogLogo(catalogLogo);
			catalogRepository.save(catalog);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		throw new NFVException(messageSource.getMessage("not.found", new String[]{CATALOG}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/catalog/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws NFVException {
		Catalog catalog = catalogRepository.findOne(id);
		if (catalog != null) {
			utility.checkAdminPermissions();
			utility.checkPermissions(catalog);
			catalogRepository.delete(catalog);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else
			throw new NFVException(messageSource.getMessage("not.found", new String[]{CATALOG}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}
	
}
