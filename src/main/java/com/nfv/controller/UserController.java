package com.nfv.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nfv.entity.Catalog;
import com.nfv.entity.Tenant;
import com.nfv.entity.User;
import com.nfv.entity.VnfInstance;
import com.nfv.error.NFVException;
import com.nfv.model.Beans;
import com.nfv.model.Email;
import com.nfv.model.Login;
import com.nfv.model.Register;
import com.nfv.model.RemoveUser;
import com.nfv.model.RestRegister;
import com.nfv.model.RestUser;
import com.nfv.model.SearchUser;
import com.nfv.repository.CatalogRepository;
import com.nfv.repository.TenantRepository;
import com.nfv.repository.UserRepository;
import com.nfv.repository.VnfInstanceRepository;
import com.nfv.utils.NFVUtility;
import com.nfv.utils.RestClient;

@RestController
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	private static final String USER = "user";

	@Value("${disable.openiam}")
	String disableOpeniam;

	@Value("${post.openiam.search.uri}")
	String openiamSearchURI;

	@Value("${post.openiam.login.uri}")
	String openiamLoginURI;

	@Value("${content.type.key}")
	String contentTypeKey;

	@Value("${content.type.value}")
	String contentTypeValue;
	
	@Value("${admin.role}")
	String adminRole;
	
	@Value("${enduser.roleid}")
	String enduserRoleId;
	
	@Value("${tenantadmin.roleid}")
	String tenantadminRoleId;
	
	@Value("${enduser.role.name}")
	String enduserRoleName;
	
	@Value("${tenant.admin.role}")
	String tenantadminRoleName;

	@Value("${post.openiam.register.uri}")
	String openiamRegisterURI;

	@Value("${post.openiam.activateUser.uri}")
	String openiamActivateUserURI;
	
	@Value("${post.openiam.removeUser.uri}")
	String openiamRemoveUserURI;
	
	@Resource
	UserRepository userRepository;

	@Resource
	VnfInstanceRepository vnfInstanceRepository;

	@Resource
	CatalogRepository catalogRepository;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	NFVUtility utility;

	@Autowired
	RestClient restClient;

	@Resource
	private TenantRepository tenantRepository;

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ResponseEntity<Iterable<User>> get() throws NFVException {
		utility.checkAdminPermissions();
		userMagic(enduserRoleId, enduserRoleName);
		userMagic(tenantadminRoleId, tenantadminRoleName);
		User user = utility.getUser();
		if (utility.isAdminUser(user))
			return new ResponseEntity<>(userRepository.findByRole("End User"), HttpStatus.OK);
		else
			return new ResponseEntity<>(userRepository.findByRoleAndTenant("End User", user.getTenant()), HttpStatus.OK);
	}

	@RequestMapping(value = "/user/all", method = RequestMethod.GET)
	public ResponseEntity<Iterable<User>> getAll() throws NFVException {
		utility.checkAdminPermissions();
		userMagic(enduserRoleId, enduserRoleName);
		userMagic(tenantadminRoleId, tenantadminRoleName);
		User user = utility.getUser();
		if (utility.isAdminUser(user))
			return new ResponseEntity<>(userRepository.findByRoleIsNot(adminRole), HttpStatus.OK);
		else
			return new ResponseEntity<>(userRepository.findByTenantAndIdIsNot(user.getTenant(), user.getId()), HttpStatus.OK);
	}

	@RequestMapping(value = "/user/info", method = RequestMethod.GET)
	public ResponseEntity<User> getInfo() throws NFVException {
		User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		if (user != null)
			return new ResponseEntity<>(user, HttpStatus.OK);
		else
			throw new NFVException(messageSource.getMessage("not.found", new String[]{USER}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> get(@PathVariable("id") Long id) throws NFVException {
		User user = userRepository.findOne(id);
		if (user != null) {
			utility.checkPermissions(user);
			return new ResponseEntity<>(user, HttpStatus.OK);
		}
		else
			throw new NFVException(messageSource.getMessage("not.found", new String[]{USER}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/vim/{vimId}/user/{id}/catalog", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Catalog>> getCatalog(@PathVariable("vimId") Long vimId, @PathVariable("id") Long id) throws NFVException {
		User user = userRepository.findOne(id);
		if (user != null) {
			utility.checkPermissions(user);
			Set<Catalog> catalogs = user.getCatalog();
			for (Iterator<Catalog> i = catalogs.iterator(); i.hasNext();) {
				Catalog c = i.next();
				if (c.getVim().getId() != vimId)
					i.remove();
			}
			return new ResponseEntity<>(catalogs, HttpStatus.OK);
		}
		else
			throw new NFVException(messageSource.getMessage("not.found", new String[]{USER}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/vim/{vimId}/user/{id}/vnfinstance", method = RequestMethod.GET)
	public ResponseEntity<Iterable<VnfInstance>> getVnfInstance(@PathVariable("vimId") Long vimId, @PathVariable("id") Long id) throws NFVException {
		User user = userRepository.findOne(id);
		if (user != null) {
			utility.checkPermissions(user);
			Set<VnfInstance> vnfInstances = user.getVnfInstance();
			for (Iterator<VnfInstance> i = vnfInstances.iterator(); i.hasNext();) {
				VnfInstance v = i.next();
				if (v.getVim().getId() != vimId)
					i.remove();
			}
			return new ResponseEntity<>(vnfInstances, HttpStatus.OK);
		}
		else
			throw new NFVException(messageSource.getMessage("not.found", new String[]{USER}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public ResponseEntity<User> create(@RequestBody User user) throws NFVException {
		utility.checkAdminPermissions();
		return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<Void> update(@PathVariable("id") Long id, @RequestBody User u) throws NFVException {
		utility.checkAdminPermissions();
		User user = userRepository.findOne(id);
		if (user != null) {
			utility.checkPermissions(user);
			user.setName(u.getName());
			user.setOpeniamid(u.getOpeniamid());
			user.setRole(u.getRole());
			user.setUsername(u.getUsername());
			userRepository.save(user);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		throw new NFVException(messageSource.getMessage("not.found", new String[]{USER}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws NFVException {
		utility.checkAdminPermissions();
		User user = userRepository.findOne(id);
		if (user != null) {
			utility.checkPermissions(user);
			userRepository.delete(user);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else
			throw new NFVException(messageSource.getMessage("not.found", new String[]{USER}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/getalluser", method = RequestMethod.GET)
	public ResponseEntity<List<Register>> getAllUser() throws NFVException 
	{
		logger.info("entering into getAllUser");
		List<Register> userList = new LinkedList<Register>();
		try
		{
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userRepository.findByUsername(username);
			if (null == user) {
				logger.info("User not found: " + username);
				throw new NFVException(messageSource.getMessage("user.unauthorized", null, utility.getLocale()), HttpStatus.UNAUTHORIZED);
			}
			for(int i = 0; i < 2; i++)
			{
				SearchUser su = new SearchUser();
				List<String> roleIds = new ArrayList<>();
				if(i == 0)
					roleIds.add(enduserRoleId);
				else
					roleIds.add(tenantadminRoleId);
				su.setRoleIds(roleIds);
				List<Beans> beans = utility.openiamSearch(su);
				if(null != beans)
				{
					for (Beans bean: beans) 
					{
						if(null != bean.getTitle() && null != user.getTenant())
						{
							if(bean.getTitle().equals(user.getTenant().getName()))
							{
								Register r = new Register();
								if(null != bean.getEmail())
								{
									Email email = new Email();
									email.setEmail(bean.getEmail());
									r.setEmail(email);
								}
								String[] s = bean.getName().split(" ");
								if(s.length > 0)
								{
									r.setFirstName(s[0]);
									r.setLastName(s[1]);
								}
								r.setId(bean.getId());
								if(i == 0)
								{
									r.setRoleId(enduserRoleId);
									r.setGroupId(enduserRoleName);
								}
								else
								{
									r.setRoleId(tenantadminRoleId);
									r.setGroupId(tenantadminRoleName);
								}
								r.setTitle(bean.getTitle());
								userList.add(r);
							}
						}
					}
				}
			}
			logger.info("exiting from getAllUser");
		}
		catch (Exception e) {
			logger.error("", e);
			if (e instanceof NFVException)
				throw (NFVException)e;
		}
		return new ResponseEntity<>(userList, HttpStatus.OK);
	}

	@RequestMapping(value = "/updaterole", method = RequestMethod.PATCH)
	public ResponseEntity<Void> updaterole(@RequestBody Register register) throws NFVException {
		User user = userRepository.findByOpeniamid(register.getId());
		logger.info("updaterole started : " );
		try
		{
			Map<String, String> map = utility.openimLogin();
			if(null != map)
			{
				//register.setLogin(registration.getUsername());
				RemoveUser removeUser = new RemoveUser();
				removeUser.setUserId(register.getId());
				removeUser.setDescription(SecurityContextHolder.getContext().getAuthentication().getName());
				
				restClient.post(openiamRemoveUserURI, map, utility.getObjectMapper().writeValueAsString(removeUser));

				String result = restClient.post(openiamRegisterURI, map, utility.getObjectMapper().writeValueAsString(register));
				RestRegister object = utility.getObjectMapper().readValue(result, RestRegister.class);
				if (object.getStatus() != 200) {
					throw new NFVException(messageSource.getMessage("already.exists", new String[]{USER}, utility.getLocale()), HttpStatus.BAD_REQUEST);
				}

				String userId = object.getContextValues().getUserId();
				restClient.post(openiamActivateUserURI, map, "{\"id\":\"" + userId + "\"}");

				if (null != user) 
				{
					if(register.getRoleId().equals(enduserRoleId))
						user.setRole(enduserRoleName);
					else if(register.getRoleId().equals(tenantadminRoleId))
						user.setRole(tenantadminRoleName);
					userRepository.save(user);
				}
				logger.info("updaterole end : " );
				return new ResponseEntity<>(HttpStatus.OK);
			}
		}
		catch (Exception e) {
			logger.error("", e);
			if (e instanceof NFVException)
				throw (NFVException)e;
		}
		throw new NFVException(messageSource.getMessage("not.found", new String[]{USER}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	private void userMagic(String roleId, String role) throws NFVException {
		if (Boolean.parseBoolean(disableOpeniam))
			return;
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByUsername(username);
		if (user == null) {
			logger.warn("User not found: " + username);
			throw new NFVException(messageSource.getMessage("user.unauthorized", null, utility.getLocale()), HttpStatus.UNAUTHORIZED);
		}
		String password = (String)SecurityContextHolder.getContext().getAuthentication().getCredentials();
		if (password == null) {
			logger.warn("User/Password not found: " + username);
			throw new NFVException(messageSource.getMessage("user.unauthorized", null, utility.getLocale()), HttpStatus.UNAUTHORIZED);
		}

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

			String uri = openiamLoginURI + username + "&password=" + password;
			String result = restClient.post(uri, null, null);
			Login objectLogin = mapper.readValue(result, Login.class);
			Long status = objectLogin.getStatus();
			if (null != status && status == 200) {
				String authToken = "OPENIAM_AUTH_TOKEN=\"" + objectLogin.getTokenInfo().getAuthToken() + "\"";
				Map<String, String> map = new HashMap<>();
				map.put("Cookie", authToken);
				map.put(contentTypeKey, contentTypeValue);
				SearchUser su = new SearchUser();
				List<String> roleIds = new ArrayList<>();
				roleIds.add(roleId);
				su.setRoleIds(roleIds);

				result = restClient.post(openiamSearchURI, map, mapper.writeValueAsString(su));
				RestUser object = mapper.readValue(result, RestUser.class);
				List<Beans> beans = object.getBeans();
				Iterable<User> users = userRepository.findByRole(role);

				for (Iterator<Beans> i = beans.iterator(); i.hasNext();) {
					Beans bean = i.next();
					for (Iterator<User> j = users.iterator(); j.hasNext();) {
						User u = j.next();
						if (bean.getId().equals(u.getOpeniamid())) {
							u.setName(bean.getName());
							userRepository.save(u);
							i.remove();
							j.remove();
						}
					}
				}

				for (Beans bean: beans) {
					User u = new User();
					u.setName(bean.getName());
					u.setOpeniamid(bean.getId());
					if(bean.getEmail() != null)
						u.setEmail(bean.getEmail());
					Tenant tenant = tenantRepository.findByName(bean.getTitle());
					if(tenant == null)
						continue;
					else
						u.setTenant(tenant);
					u.setRole(role);
					create(u);
				}

				for (User u: users) {
					if (u.getRole() != null && u.getRole().equals(enduserRoleName)) {
						Iterable<VnfInstance> instances = vnfInstanceRepository.findAll();
						for (VnfInstance instance: instances) {
							Set<User> userSet = instance.getUser();
							for (Iterator<User> i = userSet.iterator(); i.hasNext();) {
								User v = i.next();
								if (v.getId() == u.getId()) {
									i.remove();
								}
							}
							instance.setUser(userSet);
							vnfInstanceRepository.save(instance);
						}

						Iterable<Catalog> catalogs = catalogRepository.findAll();
						for (Catalog catalog: catalogs) {
							Set<User> userSet = catalog.getUser();
							for (Iterator<User> i = userSet.iterator(); i.hasNext();) {
								User v = i.next();
								if (v.getId() == u.getId()) {
									i.remove();
								}
							}
							catalog.setUser(userSet);
							catalogRepository.save(catalog);
						}
					}
					userRepository.delete(u);
				}
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
