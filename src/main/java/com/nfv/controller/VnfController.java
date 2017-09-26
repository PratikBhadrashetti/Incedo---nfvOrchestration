package com.nfv.controller;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nfv.entity.Catalog;
import com.nfv.entity.Tenant;
import com.nfv.entity.TenantNetwork;
import com.nfv.entity.User;
import com.nfv.entity.Vnf;
import com.nfv.entity.VnfInstance;
import com.nfv.error.NFVException;
import com.nfv.model.Attributes;
import com.nfv.model.Project;
import com.nfv.model.RestVnfImages;
import com.nfv.model.RestVnfd;
import com.nfv.model.RestVnfdRegister;
import com.nfv.model.ServiceTypes;
import com.nfv.model.VnfImage;
import com.nfv.model.VnfImageUpload;
import com.nfv.model.Vnfd;
import com.nfv.repository.CatalogRepository;
import com.nfv.repository.TenantNetworkRepository;
import com.nfv.repository.TenantRepository;
import com.nfv.repository.VimRepository;
import com.nfv.repository.VnfInstanceRepository;
import com.nfv.repository.VnfRepository;
import com.nfv.utils.NFVConstants;
import com.nfv.utils.NFVUtility;
import com.nfv.utils.RestClient;

@RestController
public class VnfController {
	private static final Logger logger = LoggerFactory.getLogger(VnfController.class);
	
	private static final String VNF = "VNF";
	
	@Value("${get.vnf.uri}")
	String getVnfURI;
	
	@Value("${content.type.key}")
	String contentTypeKey;
	
	@Value("${content.type.value}")
	String contentTypeValue;

	@Value("${auth.token.key}")
	String authTokenKey;
	
	@Value("${disable.openstack}")
	String disableOpenstack;
	
	@Value("${tacker.vnfd}")
	String vnfdURI;
	
	@Value("${tacker.images}")
	String vnfImages;
	
	@Value("${vim}")
	String vim;
	
	@Value("${tenantadmin.db.name}")
	String tenantadminDbName;
	
	@Resource
	VnfRepository vnfRepository;
	
	@Resource
	VnfInstanceRepository vnfInstanceRepository;
	
	@Resource
	CatalogRepository catalogRepository;
	
	@Resource
	private TenantRepository tenantRepository;
	
	@Resource
	private TenantNetworkRepository tenantNetworkRepository;

	@Resource
	VimRepository vimRepository;
	
	@Autowired
	RestClient restClient;

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	NFVUtility utility;
	
	@RequestMapping(value = "/vim/{id}/vnf", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Vnf>> get(@PathVariable("id") Long id) throws NFVException {
		utility.checkAdminPermissions();
		if (vim.equals("tacker"))
			vnfdMagic(id);
		else
			vnfMagic(id);

		User user = utility.getUser();
		if (utility.isAdminUser(user))
			return new ResponseEntity<>(vnfRepository.findByVim(vimRepository.findOne(id)), HttpStatus.OK);
		else
			return new ResponseEntity<>(vnfRepository.findByTenantAndVim(user.getTenant(), vimRepository.findOne(id)), HttpStatus.OK);
	}

	@RequestMapping(value = "/vim/{id}/vnfCatalog", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Vnf>> getVnfCatalog(@PathVariable("id") Long id) throws NFVException {
		utility.checkAdminPermissions();
		if (vim.equals("tacker"))
			vnfdMagic(id);
		else
			vnfMagic(id);

		User user = utility.getUser();
		if (utility.isAdminUser(user))
			return new ResponseEntity<>(vnfRepository.findDistinctByCatalogIsNotNullAndVim(vimRepository.findOne(id)), HttpStatus.OK);
		else
			return new ResponseEntity<>(vnfRepository.findDistinctByCatalogIsNotNullAndTenantIsNullAndVimOrCatalogIsNotNullAndTenantAndVim(vimRepository.findOne(id), user.getTenant(), vimRepository.findOne(id)), HttpStatus.OK);
	}

	@RequestMapping(value = "/vnf/{id}", method = RequestMethod.GET)
	public ResponseEntity<Vnf> getVnf(@PathVariable("id") Long id) throws NFVException {
		utility.checkAdminPermissions();
		Vnf vnf = vnfRepository.findOne(id);
		if (vnf != null) {
			utility.checkPermissions(vnf);
			return new ResponseEntity<>(vnf, HttpStatus.OK);
		}
		else
			throw new NFVException(messageSource.getMessage("not.found", new String[]{VNF}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/vnf/{id}/catalog", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Catalog>> getCatalog(@PathVariable("id") Long id) throws NFVException {
		utility.checkAdminPermissions();
		Vnf vnf = vnfRepository.findOne(id);
		if (vnf != null) {
			utility.checkPermissions(vnf);
			User user = utility.getUser();
			if (utility.isAdminUser(user))
				return new ResponseEntity<>(vnf.getCatalog(), HttpStatus.OK);
			else
				return new ResponseEntity<>(catalogRepository.findDistinctByVnfAndTenantIsNullOrVnfAndTenant(vnf, vnf, user.getTenant()), HttpStatus.OK);
		}
		else
			throw new NFVException(messageSource.getMessage("not.found", new String[]{VNF}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/vnf/{id}/vnfinstance", method = RequestMethod.GET)
	public ResponseEntity<Iterable<VnfInstance>> getVnfInstance(@PathVariable("id") Long id) throws NFVException {
		utility.checkAdminPermissions();
		Vnf vnf = vnfRepository.findOne(id);
		if (vnf != null) {
			User user = utility.getUser();
			if (utility.isAdminUser(user))
				return new ResponseEntity<>(vnf.getVnfInstance(), HttpStatus.OK);
			else
				return new ResponseEntity<>(vnfInstanceRepository.findByVnfAndTenant(vnf, user.getTenant()), HttpStatus.OK);
		}
		else
			throw new NFVException(messageSource.getMessage("not.found", new String[]{VNF}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/vnf", method = RequestMethod.POST)
	public ResponseEntity<Vnf> create(@RequestBody Vnf vnf) throws NFVException {
		utility.checkAdminPermissions();
		return new ResponseEntity<>(vnfRepository.save(vnf), HttpStatus.OK);
	}

	@RequestMapping(value = "/vim/{id}/vnfd", method = RequestMethod.POST)
	public ResponseEntity<Void> createVnfd(@PathVariable("id") Long id, @RequestParam(value = "template", required = true) MultipartFile template) throws NFVException {
		utility.checkAdminPermissions();
		String authTokenValue = restClient.getToken(utility.getUser().getTenant(), vimRepository.findOne(id));
		try {
			String json = new String(template.getBytes());
			Map<String, String> map = new HashMap<>();
			map.put(authTokenKey, authTokenValue);
			map.put(contentTypeKey, contentTypeValue);
			restClient.post(NFVConstants.HTTP + vimRepository.findOne(id).getIpaddress() + vnfdURI, map, json);
		} catch (Exception e) {
			logger.error("", e);
			throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/vnfregister", method = RequestMethod.POST)
	public ResponseEntity<Vnf> registerVnf(@RequestBody Vnf vnf) throws NFVException {
		utility.checkAdminPermissions();
		String authTokenValue = restClient.getToken(utility.getUser().getTenant(), vnf.getVim());
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			String projectId = null;
			if (vnf.getTenant().getName().equals(tenantadminDbName))
				projectId = vnf.getVim().getAdminprojectid();
			else
				projectId = vnf.getTenant().getProjectid();
			Vnfd vnfd = new Vnfd();
			vnfd.setName(vnf.getName());
			vnfd.setTenant_id(projectId);
			vnfd.setDescription(vnf.getDescription());
			vnfd.setMgmt_driver("noop");
			vnfd.setInfra_driver("heat");
			Set<TenantNetwork> networks = tenantNetworkRepository.findByTenantAndVim(utility.getUser().getTenant(), vnf.getVim());
			String nName = null;
			for (TenantNetwork network: networks) {
				nName = network.getNetwork_name() + "_" + utility.getUser().getTenant().getProjectid();
				break;
			}
			String attrs = vnf.getAttributes();
			String updatedAttrs = utility.replaceBy(attrs, nName);
			Attributes attributes = new Attributes();
			attributes.setVnfd(updatedAttrs);
			vnfd.setAttributes(attributes);
			ServiceTypes serviceType = new ServiceTypes();
			serviceType.setService_type("vnfd");
			List<ServiceTypes> serviceTypes = new ArrayList<>();
			serviceTypes.add(serviceType);
			vnfd.setService_types(serviceTypes);
			RestVnfdRegister restVnfdRegister = new RestVnfdRegister();
			restVnfdRegister.setVnfd(vnfd);
			
			Map<String, String> map = new HashMap<>();
			map.put(authTokenKey, authTokenValue);
			map.put(contentTypeKey, contentTypeValue);
			restClient.post(NFVConstants.HTTP + vnf.getVim().getIpaddress() + vnfdURI, map, mapper.writeValueAsString(restVnfdRegister));			
		} catch (Exception e) {
			logger.error("", e);
			throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(vnfRepository.save(vnf), HttpStatus.OK);
	}

	@RequestMapping(value = "/vim/{id}/vnfimage", method = RequestMethod.POST)
	public ResponseEntity<Void> createVnfImage(@PathVariable("id") Long id, @RequestParam(value = "image", required = true) MultipartFile image) throws NFVException {
		utility.checkAdminPermissions();
		String authTokenValue = restClient.getToken(utility.getUser().getTenant(), vimRepository.findOne(id));
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			VnfImageUpload imageUpload = new VnfImageUpload();
			imageUpload.setName(image.getOriginalFilename());
			imageUpload.setVisibility(utility.isAdminUser(utility.getUser())?"public":"private");
			Map<String, String> map = new HashMap<>();
			map.put(authTokenKey, authTokenValue);
			map.put(contentTypeKey, contentTypeValue);
			String result = restClient.post(NFVConstants.HTTP + vimRepository.findOne(id).getIpaddress() + vnfImages, map, mapper.writeValueAsString(imageUpload));
			
			Project object = mapper.readValue(result, Project.class);
			map.clear();
			map.put(authTokenKey, authTokenValue);
			map.put(contentTypeKey, "application/octet-stream");
			restClient.put(NFVConstants.HTTP + vimRepository.findOne(id).getIpaddress() + vnfImages + "/" + object.getId() + "/file", map, image.getBytes());
		} catch (Exception e) {
			logger.error("", e);
			throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/vnf/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<Void> update(@PathVariable("id") Long id, @RequestBody Vnf v) throws NFVException {
		utility.checkAdminPermissions();
		Vnf vnf = vnfRepository.findOne(id);
		if (vnf != null) {
			utility.checkPermissions(vnf);
			vnf.setName(v.getName());
			vnf.setImageid(v.getImageid());
			vnfRepository.save(vnf);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		throw new NFVException(messageSource.getMessage("not.found", new String[]{VNF}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/vnf/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws NFVException {
		utility.checkAdminPermissions();
		Vnf vnf = vnfRepository.findOne(id);
		if (vnf != null) {
			utility.checkPermissions(vnf);
			vnfRepository.delete(vnf);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else
			throw new NFVException(messageSource.getMessage("not.found", new String[]{VNF}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value="/download/{id}", method = RequestMethod.POST, produces = "text/plain")
	public ResponseEntity<Void> download(@PathVariable("id") Long id, HttpServletResponse response) throws NFVException 
	{
		Vnf vnf = getVnf(id).getBody();
		try 
		{
			String tosca = getTosca(vnf);
			String  mimeType = "text/plain;charset=utf-8";
			response.setContentType(mimeType);
			response.setContentLength(tosca.length());
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", vnf.getName() + ".txt");
			response.setHeader(headerKey, headerValue);
			OutputStream outStream = response.getOutputStream();
			outStream.write(tosca.getBytes(Charset.forName("UTF-8")));
			outStream.close();
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error("", e);
			if (e instanceof NFVException)
				throw (NFVException)e;
		}
		throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void vnfMagic(Long vimId) throws NFVException {
		if (Boolean.parseBoolean(disableOpenstack))
			return;
		String authTokenValue = restClient.getToken(utility.getUser().getTenant(), vimRepository.findOne(vimId));

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			Map<String, String> map = new HashMap<>();
			map.put(authTokenKey, authTokenValue);
			map.put(contentTypeKey, contentTypeValue);
			String result = restClient.get(NFVConstants.HTTP + vimRepository.findOne(vimId).getIpaddress() + getVnfURI, map);
			RestVnfImages object = mapper.readValue(result, RestVnfImages.class);
			Set<VnfImage> images = object.getImages();
			Iterable<Vnf> vnfs = vnfRepository.findAll();
			
			for (Iterator<VnfImage> i = images.iterator(); i.hasNext();) {
				VnfImage image = i.next();
				for (Iterator<Vnf> j = vnfs.iterator(); j.hasNext();) {
					Vnf vnf = j.next();
					if (image.getId().equals(vnf.getImageid())) {
						i.remove();
						j.remove();
					}
				}
			}
			
			for (VnfImage image: images) {
				Vnf vnf = new Vnf();
//				vnf.setFormat(image.getDisk_format());
				vnf.setImageid(image.getId());
				vnf.setName(image.getName());
//				vnf.setSize(String.valueOf(Math.round(image.getSize()/(1024 * 1024))) + " MB");
				create(vnf);
			}
			
			for (Vnf vnf: vnfs) {
				vnfInstanceRepository.deleteByVnf(vnf);
				Iterable<Catalog> catalogs = catalogRepository.findAll();
				for (Catalog catalog: catalogs) {
					Set<Vnf> vnfSet = catalog.getVnf();
					for (Iterator<Vnf> i = vnfSet.iterator(); i.hasNext();) {
						Vnf v = i.next();
						if (v.getId() == vnf.getId()) {
							i.remove();
						}
					}
					catalog.setVnf(vnfSet);
					catalogRepository.save(catalog);
				}
				vnfRepository.delete(vnf);
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private void vnfdMagic(Long vimId) throws NFVException {
		if (Boolean.parseBoolean(disableOpenstack))
			return;
		String authTokenValue = restClient.getToken(utility.getUser().getTenant(), vimRepository.findOne(vimId));

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			Map<String, String> map = new HashMap<>();
			map.put(authTokenKey, authTokenValue);
			map.put(contentTypeKey, contentTypeValue);
			String result = restClient.get(NFVConstants.HTTP + vimRepository.findOne(vimId).getIpaddress() + vnfdURI, map);
			RestVnfd object = mapper.readValue(result, RestVnfd.class);
			List<Vnfd> vnfds = object.getVnfds();
			Iterable<Vnf> vnfs;
			User user = utility.getUser();
			if (utility.isAdminUser(user))
				vnfs = vnfRepository.findByVim(vimRepository.findOne(vimId));
			else
				vnfs = vnfRepository.findByTenantAndVim(user.getTenant(), vimRepository.findOne(vimId));
			
			for (Iterator<Vnfd> i = vnfds.iterator(); i.hasNext();) {
				Vnfd vnfd = i.next();
				for (Iterator<Vnf> j = vnfs.iterator(); j.hasNext();) {
					Vnf vnf = j.next();
					if (vnfd.getId().equals(vnf.getImageid())) {
						i.remove();
						j.remove();
					}
				}
			}
			
			for (Vnfd vnfd: vnfds) {
				Vnf vnf = new Vnf();
				vnf.setImageid(vnfd.getId());
				vnf.setName(vnfd.getName());
				vnf.setDescription(vnfd.getDescription());
				vnf.setAttributes(vnfd.getAttributes().getVnfd());
				Tenant t = tenantRepository.findByProjectid(vnfd.getTenant_id());
				if (t == null)
					continue;
				vnf.setTenant(t);
				vnf.setVim(vimRepository.findOne(vimId));
				create(vnf);
			}
			
			for (Vnf vnf: vnfs) {
				vnfInstanceRepository.deleteByVnf(vnf);
				Iterable<Catalog> catalogs = catalogRepository.findAll();
				for (Catalog catalog: catalogs) {
					Set<Vnf> vnfSet = catalog.getVnf();
					for (Iterator<Vnf> i = vnfSet.iterator(); i.hasNext();) {
						Vnf v = i.next();
						if (v.getId() == vnf.getId()) {
							i.remove();
						}
					}
					catalog.setVnf(vnfSet);
					catalogRepository.save(catalog);
				}
				vnfRepository.delete(vnf);
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private String getTosca(Vnf vnf) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		String projectId = null;
		if (vnf.getTenant().getName().equals(tenantadminDbName))
			projectId = vnf.getVim().getAdminprojectid();
		else
			projectId = vnf.getTenant().getProjectid();
		Vnfd vnfd = new Vnfd();
		vnfd.setName(vnf.getName());
		vnfd.setTenant_id(projectId);
		vnfd.setDescription(vnf.getDescription());
		vnfd.setMgmt_driver("noop");
		vnfd.setInfra_driver("heat");
		Attributes attributes = new Attributes();
		attributes.setVnfd(vnf.getAttributes());
		vnfd.setAttributes(attributes);
		ServiceTypes serviceType = new ServiceTypes();
		serviceType.setService_type("vnfd");
		List<ServiceTypes> serviceTypes = new ArrayList<>();
		serviceTypes.add(serviceType);
		vnfd.setService_types(serviceTypes);
		RestVnfdRegister restVnfdRegister = new RestVnfdRegister();
		restVnfdRegister.setVnfd(vnfd);
		return mapper.writeValueAsString(restVnfdRegister);
	}
}
