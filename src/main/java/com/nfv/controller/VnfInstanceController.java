package com.nfv.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

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
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nfv.entity.Tenant;
import com.nfv.entity.User;
import com.nfv.entity.Vnf;
import com.nfv.entity.VnfInstance;
import com.nfv.error.NFVException;
import com.nfv.model.AddFloatingIP;
import com.nfv.model.Extnet;
import com.nfv.model.FloatingIP;
import com.nfv.model.Instance;
import com.nfv.model.Networks;
import com.nfv.model.RestAddFloatingIP;
import com.nfv.model.RestFloatingIP;
import com.nfv.model.RestResource;
import com.nfv.model.RestStack;
//import com.nfv.model.RestRegisterVUIC;
import com.nfv.model.RestVnfInstance;
import com.nfv.model.RestVnfInstances;
import com.nfv.model.RestVnfdLaunch;
import com.nfv.model.SecurityGroups;
import com.nfv.model.Server;
import com.nfv.model.VnfdAttributes;
import com.nfv.model.VnfdVnf;
import com.nfv.repository.CatalogRepository;
import com.nfv.repository.TenantRepository;
import com.nfv.repository.UserRepository;
import com.nfv.repository.VimRepository;
import com.nfv.repository.VnfInstanceRepository;
import com.nfv.repository.VnfRepository;
import com.nfv.utils.ClearwaterUtil;
import com.nfv.utils.NFVConstants;
import com.nfv.utils.NFVUtility;
import com.nfv.utils.RestClient;
import com.nfv.utils.VUICRegister;

@RestController
public class VnfInstanceController {
	private static final Logger logger = LoggerFactory.getLogger(VnfInstanceController.class);

	private static final String VNFINSTANCE = "VNF Instance";

	@Value("${get.vnfinstance.uri}")
	String getVnfInstanceURI;

	@Value("${post.vnfinstance.uri}")
	String postVnfInstanceURI;

	@Value("${content.type.key}")
	String contentTypeKey;

	@Value("${content.type.value}")
	String contentTypeValue;

	@Value("${auth.token.key}")
	String authTokenKey;

	@Value("${disable.openstack}")
	String disableOpenstack;

	@Value("${post.openstack.servers}")
	String postopenStackServersURI;

	@Value("${post.openstack.limits}")
	String postopenStackInstanceLimitsURI;

	@Value("${get.os-hypervisors.detail}")
	String getoshypervisordetailsURI;

	@Value("${disable.elk}")
	String disableElk;

	@Value("${get.floatingips.uri}")
	String getFloatingIPsURI;

	@Value("${post.elk.default.dashboard.uri}")
	String elkDefaultDashboardUri;

	@Value("${elk.launch.dashboard.uri.a}")
	String elkLaunchDashboardUriA;

	@Value("${tacker.vnf}")
	String tackerVnfURI;

	@Value("${tenantadmin.db.name}")
	String tenantadminDbName;
	
	@Value("${vim}")
	String vim;

	@Value("${get.openstack.stacks}")
	String stacks;

	@Resource
	CatalogRepository catalogRepository;

	@Resource
	VnfRepository vnfRepository;
	
	@Resource
	VnfInstanceRepository vnfInstanceRepository;

	@Resource
	UserRepository userRepository;

	@Resource
	VnfController vnfController;

	@Resource
	UserController userController;

	@Resource
	TenantRepository tenantRepository;

	@Resource
	VimRepository vimRepository;
	
	@Autowired
	RestClient restClient;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	NFVUtility utility;

	@Autowired
	VUICRegister vUICRegister;

	@RequestMapping(value = "/vim/{id}/vnfinstance", method = RequestMethod.GET)
	public ResponseEntity<Iterable<VnfInstance>> get(@PathVariable("id") Long id) throws NFVException {
		vnfInstanceMagic(id);
		User user = utility.getUser();
		if (utility.isAdminUser(user))
			return new ResponseEntity<>(vnfInstanceRepository.findByVim(vimRepository.findOne(id)), HttpStatus.OK);
		else if (utility.isTenantAdminUser(user))
			return new ResponseEntity<>(vnfInstanceRepository.findByTenantAndVim(user.getTenant(), vimRepository.findOne(id)), HttpStatus.OK);
		else
			return userController.getVnfInstance(id, user.getId());
	}

	@RequestMapping(value = "/vnfinstance/{id}", method = RequestMethod.GET)
	public ResponseEntity<VnfInstance> getVnfInstance(@PathVariable("id") Long id) throws NFVException {
		utility.checkAdminPermissions();
		VnfInstance vnfInstance = vnfInstanceRepository.findOne(id);
		if (vnfInstance != null) {
			utility.checkPermissions(vnfInstance);
			return new ResponseEntity<>(vnfInstance, HttpStatus.OK);
		}
		else
			throw new NFVException(messageSource.getMessage("not.found", new String[]{VNFINSTANCE}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/vnfinstance/{id}/user", method = RequestMethod.GET)
	public ResponseEntity<Iterable<User>> getUser(@PathVariable("id") Long id) throws NFVException {
		utility.checkAdminPermissions();
		VnfInstance vnfInstance = vnfInstanceRepository.findOne(id);
		if (vnfInstance != null) {
			utility.checkPermissions(vnfInstance);
			return new ResponseEntity<>(vnfInstance.getUser(), HttpStatus.OK);
		}
		else
			throw new NFVException(messageSource.getMessage("not.found", new String[]{VNFINSTANCE}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/vnfinstance", method = RequestMethod.POST)
	public ResponseEntity<Void> create(@RequestBody VnfInstance vnfInstance) throws NFVException {
		utility.checkAdminPermissions();
		if (vim.equals("tacker"))
			launchVnfd(vnfInstance);
		else
			launch(vnfInstance);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/vnfinstance/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<Void> update(@PathVariable("id") Long id, @RequestBody VnfInstance v) throws NFVException {
		//		utility.checkAdminPermissions();
		VnfInstance vnfInstance = vnfInstanceRepository.findOne(id);
		if (vnfInstance != null) {
			//			utility.checkPermissions(vnfInstance);
			vnfInstance.setPublicip(v.getPublicip());
			vnfInstance.setStatus(v.getStatus());
			vnfInstance.setName(v.getName());
			vnfInstance.setCreated(v.getCreated());
			vnfInstanceRepository.save(vnfInstance);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		throw new NFVException(messageSource.getMessage("not.found", new String[]{VNFINSTANCE}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/vnfinstance/{id}/user", method = RequestMethod.PATCH)
	public ResponseEntity<Void> updateUser(@PathVariable("id") Long id, @RequestBody Set<User> user) throws NFVException {
		utility.checkAdminPermissions();
		VnfInstance vnfInstance = vnfInstanceRepository.findOne(id);
		if (vnfInstance == null) {
			throw new NFVException(messageSource.getMessage("not.found", new String[]{VNFINSTANCE}, utility.getLocale()), HttpStatus.BAD_REQUEST);
		}
		utility.checkPermissions(vnfInstance);
		Set<VnfInstance> list = vnfInstanceRepository.findByCatalogAndVnf(vnfInstance.getCatalog(), vnfInstance.getVnf());
		for (VnfInstance vi: list) {
			if (vnfInstance.getId() != vi.getId()) {
				Set<User> users = vi.getUser();
				for (User u: users) {
					for (User v: user) {
						if (u.getId() == v.getId())
							throw new NFVException(messageSource.getMessage("user.already.assigned", null, utility.getLocale()), HttpStatus.BAD_REQUEST);
					}
				}
			}
		}

		if (!Boolean.parseBoolean(disableOpenstack)) {
			vUICRegister.manageUsers(vnfInstance.getPublicip(), vnfInstance.getUser(), user);
		}
		vnfInstance.setUser(user);
		vnfInstanceRepository.save(vnfInstance);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/vnfinstance/{id}/assignExternalIP", method = RequestMethod.PUT)
	public ResponseEntity<Void> assign(@PathVariable("id") Long id) throws NFVException {
		utility.checkAdminPermissions();
		VnfInstance vnfInstance = vnfInstanceRepository.findOne(id);
		if (vnfInstance != null) {
			utility.checkPermissions(vnfInstance);
			assignExternalIP(vnfInstance);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		throw new NFVException(messageSource.getMessage("not.found", new String[]{VNFINSTANCE}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/vnfinstance/{id}", method = RequestMethod.DELETE)
	private ResponseEntity<Void> delete(@PathVariable("id") Long id) throws  NFVException {
		utility.checkAdminPermissions();
		if (Boolean.parseBoolean(disableOpenstack))
			return null;

		try {
			VnfInstance vnfInstance = vnfInstanceRepository.findOne(id);
			if (vnfInstance != null) {
				String authTokenValue = restClient.getToken(utility.getUser().getTenant(), vnfInstance.getVim());
				utility.checkPermissions(vnfInstance);
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				Map<String, String> map = new HashMap<>();
				map.put(authTokenKey, authTokenValue);
				map.put(contentTypeKey, contentTypeValue);
				String projectId = null;
				if (vnfInstance.getTenant().getName().equals(tenantadminDbName))
					projectId = vnfInstance.getVim().getAdminprojectid();
				else
					projectId = vnfInstance.getTenant().getProjectid();
				restClient.delete(NFVConstants.HTTP + vnfInstance.getVim().getIpaddress() + postVnfInstanceURI + projectId + "/servers/" + vnfInstance.getInstanceid(), map);
				vnfInstanceRepository.delete(vnfInstance);
			}
		} catch (Exception e) {
			logger.info("result ="+e.getMessage());
			throw new NFVException(messageSource.getMessage("not.deleted", new String[]{VNFINSTANCE}, utility.getLocale()), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}


	@RequestMapping(value = "/vim/{vimId}/openstackInstance/{id}/servers/details", method = RequestMethod.GET)
	private ResponseEntity<String> postopenStackServersURI(@PathVariable("vimId") Long vimId, @PathVariable("id") String id) throws NFVException {
		try {
			if (id.equals("-1")) {
				Tenant adminTenant = tenantRepository.findByName("Admin");
				String authTokenValue = restClient.getToken(adminTenant, vimRepository.findOne(vimId));
				String projectId = vimRepository.findOne(vimId).getAdminprojectid();
				Map<String, String> map = new HashMap<>();
				map.put(authTokenKey, authTokenValue);
				String result = restClient.get(NFVConstants.HTTP + vimRepository.findOne(vimId).getIpaddress() + getVnfInstanceURI + projectId + "/servers/detail?all_tenants=1", map);
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
			else {
				vnfInstanceMagic(vimId);
				Set<VnfInstance> instances = vnfInstanceRepository.findByTenantAndVim(tenantRepository.findByProjectid(id), vimRepository.findOne(vimId));
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				return new ResponseEntity<>(mapper.writeValueAsString(instances), HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/vim/{vimId}/openstackInstance/{id}/limits", method = RequestMethod.GET)
	private ResponseEntity<String> openStackInstanceLimits(@PathVariable("vimId") Long vimId, @PathVariable("id") String id) throws NFVException {
		if (Boolean.parseBoolean(disableOpenstack))
			return null;

		Tenant tenant = tenantRepository.findByProjectid(id);		
		String authTokenValue = restClient.getToken(tenant, vimRepository.findOne(vimId));
		try {		
			Map<String, String> map = new HashMap<>();
			map.put(authTokenKey, authTokenValue);
			map.put(contentTypeKey, contentTypeValue);		

			String result = restClient.get(NFVConstants.HTTP + vimRepository.findOne(vimId).getIpaddress() + postopenStackInstanceLimitsURI, map);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("", e);
			throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/vim/{vimId}/os-hypervisors/detail", method = RequestMethod.GET)
	private ResponseEntity<String> getOsHypervisorDetailsURI(@PathVariable("vimId") Long vimId) throws NFVException {
		if (Boolean.parseBoolean(disableOpenstack))
			return null;

		String authTokenValue = restClient.getToken(utility.getUser().getTenant(), vimRepository.findOne(vimId));
		try {		
			Map<String, String> map = new HashMap<>();
			map.put(authTokenKey, authTokenValue);
			map.put(contentTypeKey, contentTypeValue);		

			String result = restClient.get(NFVConstants.HTTP + vimRepository.findOne(vimId).getIpaddress() + getoshypervisordetailsURI, map);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("", e);
			throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/openstackInstances", method = RequestMethod.GET)
	private ResponseEntity<String> openStackInstances() throws NFVException {
		if (Boolean.parseBoolean(disableOpenstack))
			return null;
		try {		
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			String instance = 
					"{ \"servers\": [{\"id\": \"22c91117-08de-4894-9aa9-6ef382400985\",\"links\":{\"href\": \"http://10.145.70.31/horizon/admin/instances/\",\"name\": \"10.145.70.31\" }] }";
			String result = mapper.writeValueAsString(instance);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();		
			throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private void launch(VnfInstance vnfInstance) throws NFVException {
		if (Boolean.parseBoolean(disableOpenstack))
			return;
		String authTokenValue = restClient.getToken(utility.getUser().getTenant(), vnfInstance.getVim());

		try {
			Vnf vnf = vnfController.getVnf(vnfInstance.getVnf().getId()).getBody();
			Instance instance = new Instance();
			Server server = new Server();
			List<SecurityGroups> listSecurity_groups = new ArrayList<>();
			List<Networks> listNetworks = new ArrayList<>();
			SecurityGroups securityGroups = new SecurityGroups();
			Networks networks = new Networks();
			listSecurity_groups.add(securityGroups);
			listNetworks.add(networks);
			long l = System.currentTimeMillis();
			server.setName("demo-"+l);
			server.setImageRef(vnf.getImageid());
			server.setSecurity_groups(listSecurity_groups);
			server.setNetworks(listNetworks);
			instance.setServer(server);
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			Map<String, String> map = new HashMap<>();
			map.put(authTokenKey, authTokenValue);
			map.put(contentTypeKey, contentTypeValue);
			String projectId = null;
			if (vnfInstance.getTenant().getName().equals(tenantadminDbName))
				projectId = vnfInstance.getVim().getAdminprojectid();
			else
				projectId = vnfInstance.getTenant().getProjectid();
			String pURI =  NFVConstants.HTTP + vnfInstance.getVim().getIpaddress() + postVnfInstanceURI + projectId + "/servers";
			String result = restClient.post(pURI, map, mapper.writeValueAsString(instance));

			RestVnfInstance object = mapper.readValue(result, RestVnfInstance.class);
			List<VnfInstance> vInst = new ArrayList<>();
			vnfInstance.setInstanceid(object.getServer().getId());
			vnfInstance.setCreated("");
			vnfInstance.setPublicip("");
			vnfInstance.setPrivateip("");
			vnfInstance.setName("");
			vnfInstance.setStatus("");
			vnfInstance.setServicestatus(false);
			vInst.add(vnfInstanceRepository.save(vnfInstance));
//			return vInst;
		} catch (Exception e) {
			logger.error("", e);
			throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private void launchVnfd(VnfInstance vnfInstance) throws NFVException {
		if (Boolean.parseBoolean(disableOpenstack))
			return;
		String authTokenValue = restClient.getToken(utility.getUser().getTenant(), vnfInstance.getVim());

		try {
			String projectId = null;
			if (vnfInstance.getTenant().getName().equals(tenantadminDbName))
				projectId = vnfInstance.getVim().getAdminprojectid();
			else
				projectId = vnfInstance.getTenant().getProjectid();
			Vnf vnf = vnfController.getVnf(vnfInstance.getVnf().getId()).getBody();
			RestVnfdLaunch vnfdLaunch = new RestVnfdLaunch();
			VnfdAttributes vnfdAttributes = new VnfdAttributes();
			long l = System.currentTimeMillis();
			vnfdAttributes.setStack_name("demo-" + l);
			VnfdVnf vnfdVnf = new VnfdVnf();
			vnfdVnf.setAttributes(vnfdAttributes);
			vnfdVnf.setVnfd_id(vnf.getImageid());
			vnfdVnf.setName("demo-" + l);
			vnfdVnf.setTenant_id(projectId);
			vnfdLaunch.setVnf(vnfdVnf);
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			Map<String, String> map = new HashMap<>();
			map.put(authTokenKey, authTokenValue);
			map.put(contentTypeKey, contentTypeValue);
			restClient.post(NFVConstants.HTTP + vnfInstance.getVim().getIpaddress() + tackerVnfURI, map, mapper.writeValueAsString(vnfdLaunch));
			
			map.clear();
			Tenant adminTenant = tenantRepository.findByName("Admin");
			projectId = vnfInstance.getVim().getAdminprojectid();
			authTokenValue = restClient.getToken(adminTenant, vnfInstance.getVim());
			map.put(authTokenKey, authTokenValue);
			String stackStatus = "";
			String stackId = null;
			while (!stackStatus.equalsIgnoreCase("CREATE_COMPLETE")) {
				Thread.sleep(5000);
				String result = restClient.get(NFVConstants.HTTP + vnfInstance.getVim().getIpaddress() + stacks + "/" + projectId + "/stacks/" + "demo-" + l, map);
				RestStack object = mapper.readValue(result, RestStack.class);
				stackStatus = object.getStack().getStack_status();
				stackId = object.getStack().getId();
				logger.info("Stack status is " + stackStatus);
			}
			
			vnfInstance.setCreated("");
			vnfInstance.setPublicip("");
			vnfInstance.setPrivateip("");
			vnfInstance.setServicestatus(false);
			vnfInstance.setFlavorid("");
			vnfInstance.setInitstatus(0l);
			vnfInstance.setTackername("demo-" + l);

			String result = restClient.get(NFVConstants.HTTP + vnfInstance.getVim().getIpaddress() + stacks + "/" + projectId + "/stacks/" + stackId + "/resources", map);
			RestResource object1 = mapper.readValue(result, RestResource.class);
			List<com.nfv.model.Resource> resources = object1.getResources();
			for (com.nfv.model.Resource resource: resources) {
				if (resource.getRequired_by().isEmpty()) {
					VnfInstance v = new VnfInstance();
					v.setCatalog(vnfInstance.getCatalog());
					v.setCreated(vnfInstance.getCreated());
					v.setDescription(vnfInstance.getDescription());
					v.setElk_uri(vnfInstance.getElk_uri());
					v.setInstanceid(resource.getPhysical_resource_id());
					v.setName(resource.getResource_name());
					v.setTackername(vnfInstance.getTackername());
					v.setPrivateip(vnfInstance.getPrivateip());
					v.setPublicip(vnfInstance.getPublicip());
					v.setServicestatus(vnfInstance.getServicestatus());
					v.setInitstatus(vnfInstance.getInitstatus());
					v.setStatus(resource.getResource_status());
					v.setTenant(vnfInstance.getTenant());
					v.setVnf(vnfInstance.getVnf());
					v.setFlavorid(vnfInstance.getFlavorid());
					v.setVim(vnfInstance.getVim());
					vnfInstanceRepository.save(v);
				}
			}

//			List<VnfInstance> vInst = new ArrayList<>();
//			List<String> ids = getId("demo-" + l, authTokenValue, utility.getUser().getTenant().getProjectid()); 
//			vnfInstance.setCreated("");
//			vnfInstance.setPublicip("");
//			vnfInstance.setPrivateip("");
//			vnfInstance.setName("");
//			vnfInstance.setStatus("");
//			vnfInstance.setServicestatus(false);
//			vnfInstance.setFlavorid("");
//			for (String id: ids) {
//				VnfInstance v = new VnfInstance();
//				v.setCatalog(vnfInstance.getCatalog());
//				v.setCreated(vnfInstance.getCreated());
//				v.setDescription(vnfInstance.getDescription());
//				v.setElk_uri(vnfInstance.getElk_uri());
//				v.setInstanceid(id);
//				v.setName(vnfInstance.getName());
//				v.setPrivateip(vnfInstance.getPrivateip());
//				v.setPublicip(vnfInstance.getPublicip());
//				v.setServicestatus(vnfInstance.getServicestatus());
//				v.setStatus(vnfInstance.getStatus());
//				v.setTenant(vnfInstance.getTenant());
//				v.setVnf(vnfInstance.getVnf());
//				v.setFlavorid(vnfInstance.getFlavorid());
//				vInst.add(vnfInstanceRepository.save(v));
//			}
//			return vInst;
		} catch (Exception e) {
			logger.error("", e);
			throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//	private List<String> getId(String name, String authTokenValue, String projectId) throws NFVException {
//		List<String> id = new ArrayList<>();
//		try {
//			Tenant adminTenant = tenantRepository.findByName("Admin");
//			authTokenValue = restClient.getToken(adminTenant); //RUBAL, need to fix later
//			projectId = adminTenant.getProjectid(); //RUBAL, need to fix later
//			ObjectMapper mapper = new ObjectMapper();
//			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//			Map<String, String> map = new HashMap<>();
//			map.put(authTokenKey, authTokenValue);
//			map.put(contentTypeKey, contentTypeValue);
//			while (id.isEmpty()) {
//				Thread.sleep(5000l);
//				String result = restClient.get(getVnfInstanceURI + projectId + "/servers/detail", map);
//				RestVnfInstances object = mapper.readValue(result, RestVnfInstances.class);
//				Set<Server> servers = object.getServers();
//				for (Server server: servers) {
//					if (server.getName().startsWith(name)) {
//						id.add(server.getId());
//					}
//				}
//			}
//		} catch (Exception e) {
//			logger.error("", e);
//			throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//		return id;
//	}

	private void vnfInstanceMagic(Long vimId) throws NFVException {
		if (Boolean.parseBoolean(disableOpenstack))
			return;
		String authTokenValue = restClient.getToken(utility.getUser().getTenant(), vimRepository.findOne(vimId));

		try {
			Tenant adminTenant = tenantRepository.findByName("Admin");
			authTokenValue = restClient.getToken(adminTenant, vimRepository.findOne(vimId)); //RUBAL, need to fix later
			//			String projectId = utility.getUser().getTenant().getProjectid();
			String projectId = vimRepository.findOne(vimId).getAdminprojectid(); //RUBAL, need to fix later

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			Map<String, String> map = new HashMap<>();
			map.put(authTokenKey, authTokenValue);
			map.put(contentTypeKey, contentTypeValue);
			String result = restClient.get(NFVConstants.HTTP + vimRepository.findOne(vimId).getIpaddress() + getVnfInstanceURI + projectId + "/servers/detail", map);
			RestVnfInstances object = mapper.readValue(result, RestVnfInstances.class);
			Set<Server> servers = object.getServers();
//			User user = utility.getUser();
//			Iterable<VnfInstance> vnfInstances = vnfInstanceRepository.findByTenant(user.getTenant()); //RUBAL, need to fix later
			Iterable<VnfInstance> vnfInstances = vnfInstanceRepository.findByVim(vimRepository.findOne(vimId));

			for (Iterator<Server> i = servers.iterator(); i.hasNext();) {
				Server server = i.next();
				for (Iterator<VnfInstance> j = vnfInstances.iterator(); j.hasNext();) {
					VnfInstance vnfInstance = j.next();
					if (server.getId().equals(vnfInstance.getInstanceid())) {
						if (vnfInstance.getInitstatus() == 0 || vnfInstance.getInitstatus() == 3)
							vnfInstance.setStatus(server.getStatus());
						vnfInstance.setName(server.getName());
						vnfInstance.setCreated(server.getCreated());
						vnfInstance.setFlavorid(server.getFlavor().getId());

						if (server.getAddresses() != null) {
							Map<String, List<Extnet>> m = server.getAddresses().getMap();
							Collection<List<Extnet>> c = m.values();
							for (List<Extnet> list: c) {
								for (Extnet net: list) {
									if (net.getType().equals("floating")) {
										vnfInstance.setPublicip(net.getAddr());
									}
									if (net.getType().equals("fixed")) {
										vnfInstance.setPrivateip(net.getAddr());
									}
								}
							}
						}

						update(vnfInstance.getId(), vnfInstance);
						i.remove();
						j.remove();
					}
				}
			}
			
			for (VnfInstance vnfInstance: vnfInstances) {
				vnfInstanceRepository.delete(vnfInstance);
			}
			
			vnfInstances = vnfInstanceRepository.findAll();
			for (VnfInstance vnfInstance: vnfInstances) {
				if (vnfInstance.getPublicip().equals("") && vnfInstance.getStatus().equalsIgnoreCase("ACTIVE")) {
					assignExternalIP(vnfInstance);
					Thread.sleep(5000);
				}
			}

			vnfInstances = vnfInstanceRepository.findByInitstatus(1l);
			Set<String> tackerNames = new HashSet<String>();
			for (VnfInstance vnfInstance: vnfInstances) {
				tackerNames.add(vnfInstance.getTackername());
			}
			
			for (String tackerName: tackerNames) {
				logger.info("Need init for " + tackerName);
				boolean startInit = true;
				vnfInstances = vnfInstanceRepository.findByTackername(tackerName);
				for (VnfInstance vnfInstance: vnfInstances) {
					logger.info(vnfInstance.getName() + ":" + vnfInstance.getInitstatus() + ":" + vnfInstance.getPublicip());
					if (vnfInstance.getInitstatus() != 1) {
						logger.info("Public IP not yet assigned for " + vnfInstance.getName());
						startInit = false;
						break;
					}
				}
				
				if (startInit) {
					logger.info("Starting init for " + tackerName);
					for (VnfInstance vnfInstance: vnfInstances) {
						vnfInstance.setInitstatus(2l);
						vnfInstanceRepository.save(vnfInstance);
					}
					ClearwaterUtil clearwaterUtil = new ClearwaterUtil();
					clearwaterUtil.set(vnfInstances, vnfInstanceRepository);
					clearwaterUtil.start();
				} else
					logger.info("Init pending for " + tackerName);
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private void assignExternalIP(VnfInstance vInstance)  throws NFVException {
		try {
			Tenant adminTenant = tenantRepository.findByName("Admin");
			//			String authTokenValue = restClient.getToken(utility.getUser().getTenant());
			String authTokenValue = restClient.getToken(adminTenant, vInstance.getVim()); //RUBAL, need to fix later
			String projectId = vInstance.getVim().getAdminprojectid(); //RUBAL, need to fix later

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			Map<String, String> map = new HashMap<>();
			map.put(authTokenKey, authTokenValue);
			map.put(contentTypeKey, contentTypeValue);
			String instanceId = vInstance.getInstanceid();

			VnfInstance vnfInstance = vnfInstanceRepository.findOne(vInstance.getId());
			if (null == vnfInstance) {
				throw new NFVException(messageSource.getMessage("not.found", new String[]{VNFINSTANCE}, utility.getLocale()), HttpStatus.BAD_REQUEST);
			}

			String serverHostName = vnfInstance.getName();
			String fixedIPAddress = vnfInstance.getPrivateip();
			String pURI =  NFVConstants.HTTP + vInstance.getVim().getIpaddress() + postVnfInstanceURI + projectId + "/servers";
			while (fixedIPAddress.equals("") || serverHostName.equals("") ) {
				Thread.sleep(5000);
				String result = restClient.get(pURI + "/" + instanceId, map);
				Server server = mapper.readValue(result, RestVnfInstance.class).getServer();
				serverHostName = server.getName();

				if (server.getAddresses() != null) {
					Map<String, List<Extnet>> m = server.getAddresses().getMap();
					Collection<List<Extnet>> c = m.values();
					for (List<Extnet> list: c) {
						for (Extnet net: list) {
							if (net.getType().equals("fixed")) {
								fixedIPAddress = net.getAddr();
								break;
							}
						}
					}
				}
			}

			String result = restClient.get(NFVConstants.HTTP + vInstance.getVim().getIpaddress() + getFloatingIPsURI, map);
			RestFloatingIP object = mapper.readValue(result, RestFloatingIP.class);
			List<FloatingIP> list = object.getFloatingips();
			String floating_ip_address = null;
			for (FloatingIP floatingIP: list) {
				if (floatingIP.getStatus().equalsIgnoreCase("DOWN") && floatingIP.getTenant_id().equals(projectId)) {
					floating_ip_address = floatingIP.getFloating_ip_address();
					AddFloatingIP addFloatingIP = new AddFloatingIP();
					addFloatingIP.setAddress(floating_ip_address);
					addFloatingIP.setFixed_address(fixedIPAddress);
					RestAddFloatingIP restAddFloatingIP = new RestAddFloatingIP();
					restAddFloatingIP.setAddFloatingIp(addFloatingIP);
					restClient.post(pURI + "/" + instanceId + "/action", map, mapper.writeValueAsString(restAddFloatingIP));
					break;
				}
			}

			//			if (floating_ip_address != null) {				
			//				while (check(floating_ip_address)) {
			//					Thread.sleep(5000);
			//				}
			//			}

			//ELK code start		
			if (!Boolean.parseBoolean(disableElk) && vnfInstance.getElk_uri() == null) {
				mapper = new ObjectMapper();
				mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				map = new HashMap<>();
				map.put(contentTypeKey, contentTypeValue);
				map.put("kbn-version" ,"4.4.2");
				
				result = restClient.get(NFVConstants.HTTP + vInstance.getVim().getElkipaddress() + elkDefaultDashboardUri + vInstance.getVim().getElkid() + "/_source", map);
				result = result.replaceAll(vInstance.getVim().getElkid(), serverHostName);	// launched instance name	
				result = result.replaceAll("NFVDashboard", serverHostName);
				
				result = restClient.post(NFVConstants.HTTP + vInstance.getVim().getElkipaddress() + elkDefaultDashboardUri+serverHostName, map,result.toString());
				String elkLaunchDashboardUriHost = NFVConstants.HTTP + vInstance.getVim().getElkipaddress() + elkLaunchDashboardUriA;
				elkLaunchDashboardUriHost = elkLaunchDashboardUriHost.replaceAll("X", serverHostName);
				
				logger.info("elkLaunchDashboardUriHost ="+elkLaunchDashboardUriHost);
				vnfInstance.setElk_uri(elkLaunchDashboardUriHost);
				vnfInstanceRepository.save(vnfInstance);
			}
			//ELK code end

			if (null != floating_ip_address) {
//				vnfInstance.setServicestatus(true);
				vnfInstance.setStatus("INITIALIZING");
				vnfInstance.setInitstatus(1l);
				vnfInstance.setPublicip(floating_ip_address);
				vnfInstanceRepository.save(vnfInstance);
			} else {
				throw new Exception("Floating IP not assigned");
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//	private boolean check(String ip) {
	//		try {
	//			ObjectMapper mapper = new ObjectMapper();
	//			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	//			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	//			Map<String, String> map = new HashMap<>();
	//			map.put(contentTypeKey, contentTypeValue);
	//			map.put("apitype", "nfvUM");
	//			RestRegisterVUIC register = new RestRegisterVUIC();
	//			register.setPassword("dummy");
	//			register.setUsername("dummy");
	//			restClient.postVUIC("https://" + ip + ":9998/registerUsers", map, mapper.writeValueAsString(register));
	//			return false;
	//		} catch (Exception e) {
	//			return true;
	//		}
	//	}
}
