package com.nfv.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
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
import com.nfv.entity.ForgotPassword;
import com.nfv.entity.Registration;
import com.nfv.entity.Tenant;
import com.nfv.entity.User;
import com.nfv.entity.Vim;
import com.nfv.entity.VnfInstance;
import com.nfv.error.NFVException;
import com.nfv.mail.EmailRegistrationContent;
import com.nfv.mail.SendMail;
import com.nfv.model.AddUser;
import com.nfv.model.Beans;
import com.nfv.model.ChangePassword;
import com.nfv.model.Email;
import com.nfv.model.FloatingIP;
import com.nfv.model.Login;
import com.nfv.model.Register;
import com.nfv.model.ResetPassword;
import com.nfv.model.RestFloatingIP;
import com.nfv.model.RestRegister;
import com.nfv.model.SearchUser;
import com.nfv.model.SetPassword;
import com.nfv.repository.ForgotPasswordRepository;
import com.nfv.repository.RegistrationRepository;
import com.nfv.repository.TenantRepository;
import com.nfv.repository.UserRepository;
import com.nfv.repository.VimRepository;
import com.nfv.repository.VnfInstanceRepository;
import com.nfv.utils.NFVConstants;
import com.nfv.utils.NFVUtility;
import com.nfv.utils.RestClient;
//import com.nfv.utils.TenantCreationUtil;

@RestController
public class MiscController {
	private static final Logger logger = LoggerFactory.getLogger(MiscController.class);

	private static final String USER = "user";

	private static final String VNFINSTANCE = "VNF Instance";
	
	private static final String TENANT = "tenant";

	@Value("${application.version}")
	String version;

	@Value("${post.openiam.login.uri}")
	String openiamLoginURI;

	@Value("${post.openiam.register.uri}")
	String openiamRegisterURI;

	@Value("${post.openiam.resetPassword.uri}")
	String openiamResetPasswordURI;

	@Value("${post.openiam.activateUser.uri}")
	String openiamActivateUserURI;

	@Value("${get.floatingips.uri}")
	String getFloatingIPsURI;

	@Value("${content.type.key}")
	String contentTypeKey;

	@Value("${content.type.value}")
	String contentTypeValue;

	@Value("${auth.token.key}")
	String authTokenKey;

	@Value("${openiam.admin}")
	String adminUsername;

	@Value("${openiam.password}")
	String adminPassword;

	@Value("${vim}")
	String vim;

	@Value("${email.username.register}")
	String emailUsernameRegister;

	@Value("${email.password.register}")
	String emailPasswordRegister;

	@Value("${project.name}")
	String projectName;

	@Value("${setpassword.page.name}")
	String setpasswordPageName;

	@Value("${enduser.roleid}")
	String enduserRoleId;

	@Value("${tenantadmin.roleid}")
	String tenantadminRoleId;

	@Value("${enduser.role.name}")
	String enduserRoleName;

	@Value("${tenant.admin.role}")
	String tenantadminRoleName;

	@Value("${tenantadmin.db.name}")
	String tenantadminDbName;
	
	@Value("${email.host.link}")
	String emailHostLink;

	@Value("${tenant.register.page.name}")
	String tenantRegisterPageName;

	@Value("${forgot.password.page}")
	String forgotPasswordPage;

	@Value("${adduser.setpassword.page}")
	String adduserSetpasswordPage;

	@Value("${post.openstack.project.uri}")
	String postOSProjectURI;

	@Value("${post.openstack.users.uri}")
	String postOSUsersURI;

	@Value("${get.openstack.roles.uri}")
	String getOSRolesURI;

	@Value("${redirect.to.adduser}")
	String redirectToAdduser;

	@Resource
	UserRepository userRepository;

	@Resource
	VnfInstanceRepository vnfInstanceRepository;

	@Resource
	VimRepository vimRepository;
	
	@Autowired
	RestClient restClient;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	NFVUtility utility;

	@Autowired
	private SendMail mail;

	@Autowired
	private EmailRegistrationContent emailContent;

	@Resource
	private RegistrationRepository registrationRepository;

	@Resource
	private TenantRepository tenantRepository;

	@Resource
	private ForgotPasswordRepository forgotPasswordRepository;

	@Autowired
	UserController userController;

	@RequestMapping(value = "/misc/version", method = RequestMethod.GET)
	public ResponseEntity<String> getVersion() {
		return new ResponseEntity<>(version, HttpStatus.OK);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<Void> register(@RequestBody Register register) throws NFVException {
		try {
			String password = register.getPassword();
			register.setPassword(null);

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

			String uri = openiamLoginURI + adminUsername + "&password=" + adminPassword;
			String result = restClient.post(uri, null, null);
			Login objectLogin = mapper.readValue(result, Login.class);
			Long status = objectLogin.getStatus();
			if (null != status && status == 200) {
				String authToken = "OPENIAM_AUTH_TOKEN=\"" + objectLogin.getTokenInfo().getAuthToken() + "\"";
				Map<String, String> map = new HashMap<>();
				map.put("Cookie", authToken);
				map.put(contentTypeKey, contentTypeValue);

				result = restClient.post(openiamRegisterURI, map, mapper.writeValueAsString(register));
				RestRegister object = mapper.readValue(result, RestRegister.class);
				if (object.getStatus() != 200) {
					throw new NFVException(messageSource.getMessage("already.exists", new String[]{USER}, utility.getLocale()), HttpStatus.BAD_REQUEST);
				}

				String userId = object.getContextValues().getUserId();
				restClient.post(openiamActivateUserURI, map, "{\"id\":\"" + userId + "\"}");

				ResetPassword resetPassword = new ResetPassword();
				resetPassword.setUserId(userId);
				resetPassword.setPassword(password);
				resetPassword.setConfPassword(password);
				restClient.post(openiamResetPasswordURI, map, mapper.writeValueAsString(resetPassword));
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("", e);
			if (e instanceof NFVException)
				throw (NFVException)e;
		}
		throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/changepassword", method = RequestMethod.POST)
	public ResponseEntity<Void> changePassword(@RequestBody ChangePassword changePassword) throws NFVException {
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

		if (!password.equals(changePassword.getOldPassword()))
			throw new NFVException(messageSource.getMessage("old.password.mismatch", null, utility.getLocale()), HttpStatus.BAD_REQUEST);

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

				ResetPassword resetPassword = new ResetPassword();
				resetPassword.setUserId(user.getOpeniamid());
				resetPassword.setPassword(changePassword.getNewPassword());
				resetPassword.setConfPassword(changePassword.getNewPassword());
				restClient.post(openiamResetPasswordURI, map, mapper.writeValueAsString(resetPassword));
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/resetuser/{id}", method = RequestMethod.POST)
	public ResponseEntity<Void> resetUser(@PathVariable("id") Long id, @RequestBody ChangePassword changePassword) throws NFVException {
		String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		User adminUser = userRepository.findByUsername(adminUsername);
		if (adminUser == null) {
			logger.warn("User not found: " + adminUsername);
			throw new NFVException(messageSource.getMessage("user.unauthorized", null, utility.getLocale()), HttpStatus.UNAUTHORIZED);
		}
		String adminPassword = (String)SecurityContextHolder.getContext().getAuthentication().getCredentials();
		if (adminPassword == null) {
			logger.warn("User/Password not found: " + adminUsername);
			throw new NFVException(messageSource.getMessage("user.unauthorized", null, utility.getLocale()), HttpStatus.UNAUTHORIZED);
		}
		User user = userRepository.findOne(id);
		if (user == null) {
			logger.warn("User not found: " + id);
			throw new NFVException(messageSource.getMessage("not.found", new String[]{USER}, utility.getLocale()), HttpStatus.NOT_FOUND);
		}

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

			String uri = openiamLoginURI + adminUsername + "&password=" + adminPassword;
			String result = restClient.post(uri, null, null);
			Login objectLogin = mapper.readValue(result, Login.class);
			Long status = objectLogin.getStatus();
			if (null != status && status == 200) {
				String authToken = "OPENIAM_AUTH_TOKEN=\"" + objectLogin.getTokenInfo().getAuthToken() + "\"";
				Map<String, String> map = new HashMap<>();
				map.put("Cookie", authToken);
				map.put(contentTypeKey, contentTypeValue);

				ResetPassword resetPassword = new ResetPassword();
				resetPassword.setUserId(user.getOpeniamid());
				resetPassword.setPassword(changePassword.getNewPassword());
				resetPassword.setConfPassword(changePassword.getNewPassword());
				restClient.post(openiamResetPasswordURI, map, mapper.writeValueAsString(resetPassword));
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/viewvuic/{id}", method = RequestMethod.GET)
	public ResponseEntity<String> viewvuic(@PathVariable("id") Long id) throws NFVException {
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

		VnfInstance vnfInstance = vnfInstanceRepository.findOne(id);
		if (vnfInstance != null && vnfInstance.getPublicip() != null) {
			String url = "http://" + vnfInstance.getPublicip() + ":9090?key=" + encode(username, password);
			return new ResponseEntity<>(url, HttpStatus.OK);
		} else {
			throw new NFVException(messageSource.getMessage("not.found", new String[]{VNFINSTANCE}, utility.getLocale()), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/viminfo", method = RequestMethod.GET)
	public ResponseEntity<String> getVim() throws NFVException {
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

		return new ResponseEntity<>(vim, HttpStatus.OK);
	}

	private String encode(String username, String password) {
		String str = "username=" + username + ",password=" + password;
		byte[] encodedBytes = Base64.getEncoder().encode(str.getBytes());
		return new String(encodedBytes);
	}

	@RequestMapping(value = "/getalltenants", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Tenant>> getAllTenants() throws NFVException {
//		Iterable<Tenant> tenants = tenantRepository.findAll();
//		for (Tenant tenant: tenants) {
//			int[] arr = getPublicIPInfo(tenant);
//			if (arr != null) {
//				tenant.setAllocatedip(arr[0]);
//				tenant.setUsedip(arr[1]);
//				tenantRepository.save(tenant);
//			}
//		}
		return new ResponseEntity<>(tenantRepository.findAll(), HttpStatus.OK);
	}

	@RequestMapping(value = "/registerenduser", method = RequestMethod.POST)
	public ResponseEntity<Void> registerenduser(@RequestBody Registration registration) throws NFVException {
		try {
			logger.info("registerenduser started || registration : " + registration);
			Registration registrationExist = registrationRepository.findByEmail(registration.getEmail());
			if(registrationExist != null)
			{
				logger.warn("Email already exists: " + registrationExist.getEmail());
				throw new NFVException(messageSource.getMessage("already.exists", new String[]{NFVConstants.EMAIL}, utility.getLocale()), HttpStatus.BAD_REQUEST);
			}
			registration = utility.generateToken(registration);
			mail.sendEmail(
					registration.getEmail(),
					emailUsernameRegister, emailPasswordRegister,
					NFVConstants.SETPASSWORD_EMAIL_SUBJECT.replace("$projectName", projectName),
					emailContent.emailNotificationDetails(registration, "password_recovery_template.vm", setpasswordPageName, registration.getFirstname()));
			logger.info("registerenduser end : ");
			registration.setRoleId(enduserRoleId);
			registrationRepository.save(registration);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error("", e);
			if (e instanceof NFVException)
				throw (NFVException)e;
		}
		throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/redirecttosetpassword/{token}", method = RequestMethod.GET)
	public ResponseEntity<Void> redirecttosetpassword(@PathVariable("token") String token) throws NFVException, URISyntaxException 
	{
		logger.info("inside redirecttosetpassword || token : " + token);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/setnewpassword/{token}", method = RequestMethod.POST)
	public ResponseEntity<Void> setnewpassword(@PathVariable("token") String token, @RequestBody SetPassword setPassword) throws NFVException 
	{
		logger.info("setnewpassword started || token : " + token);
		Registration registration = registrationRepository.findByToken(token);
		if (null == registration)
		{
			logger.warn("User not found: " + token);
			throw new NFVException(messageSource.getMessage("not.found", new String[]{token}, null, utility.getLocale()), HttpStatus.BAD_REQUEST);
		}
		if(null != registration && (System.currentTimeMillis() - Long.parseLong(registration.getTokenCreationTime())) < 1 * 24 * 60 * 60 * 1000)
		{
			try
			{
				URI opennfv = new URI("http://"+emailHostLink);
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setLocation(opennfv);

				logger.info(" setnewpassword registration : " + registration);
				String password = setPassword.getNewPassword();
				Register register = new Register();
				register.setFirstName(registration.getFirstname());
				register.setLastName(registration.getLastname());
				register.setLogin(registration.getUsername());
				register.setPassword(password);
				register.setRoleId(registration.getRoleId());
				Email email = new Email();
				email.setEmail(registration.getEmail());
				register.setEmail(email);
				if(registration.getRoleId().equals(tenantadminRoleId))
					register.setTitle(registration.getTenantname());
				else if(registration.getRoleId().equals(enduserRoleId))
				{
					Tenant tenant = tenantRepository.findOne(registration.getTenantid());
					if(null != tenant)
						register.setTitle(tenant.getName());
				}

				Map<String, String> map = utility.openimLogin();
				if(null != map)
				{
					String result = restClient.post(openiamRegisterURI, map, utility.getObjectMapper().writeValueAsString(register));
					RestRegister object = utility.getObjectMapper().readValue(result, RestRegister.class);
					if (object.getStatus() != 200) {
						throw new NFVException(messageSource.getMessage("already.exists", new String[]{USER}, utility.getLocale()), HttpStatus.BAD_REQUEST);
					}

					String userId = object.getContextValues().getUserId();
					restClient.post(openiamActivateUserURI, map, "{\"id\":\"" + userId + "\"}");

					ResetPassword resetPassword = new ResetPassword();
					resetPassword.setUserId(userId);
					resetPassword.setPassword(password);
					resetPassword.setConfPassword(password);
					restClient.post(openiamResetPasswordURI, map, utility.getObjectMapper().writeValueAsString(resetPassword));
					Registration existingToken = registrationRepository.findByToken(registration.getToken());
					if(null != existingToken)
						registrationRepository.delete(existingToken.getId());
					if(registration.getRoleId().equals(tenantadminRoleId))
					{
						if(null == tenantRepository.findByName(registration.getTenantname()))
						{
							Tenant newtenant = new Tenant();
							newtenant.setName(registration.getTenantname());
							newtenant.setProject(newtenant.getName().toLowerCase());
							newtenant.setProjectusername(newtenant.getProject() + "u");
							newtenant.setProjectpassword(newtenant.getProject() + "p");
							tenantRepository.save(newtenant);
//							tenantprojectinfo(t);
						}
					}
					logger.info("setnewpassword end : " + token);
					return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
				}
			}
			catch (Exception e) {
				logger.error("", e);
				if (e instanceof NFVException)
					throw (NFVException)e;
			}
		}
		else
		{
			logger.warn("Token has been expired : " + token);
			throw new NFVException(messageSource.getMessage("email.link.expired", null, utility.getLocale()), HttpStatus.BAD_REQUEST);
		}
		throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/tenantmanagement", method = RequestMethod.POST)
	public ResponseEntity<Void> tenantmanagement(@RequestBody Registration registration) throws NFVException {
		try {
			logger.info("tenantmanagement started : ");
			Tenant tenantExist = tenantRepository.findByName(registration.getTenantname());
			if(null != tenantExist)
			{
				logger.warn("Tenant already exists: " + registration.getTenantname());
				throw new NFVException(messageSource.getMessage("already.exists", new String[]{NFVConstants.TENANT_NAME}, utility.getLocale()), HttpStatus.BAD_REQUEST);
			}
			Registration registration1 = registrationRepository.findByEmail(registration.getEmail());
			if(null !=  registration1)
			{
				logger.warn("Email already exists: " + registration1.getEmail());
				throw new NFVException(messageSource.getMessage("already.exists", new String[]{NFVConstants.EMAIL}, utility.getLocale()), HttpStatus.BAD_REQUEST);
			}
			registration.setRoleId(tenantadminRoleId);
			registration = utility.generateToken(registration);
			logger.info("registration : "+ registration);
			mail.sendEmail(	
					registration.getEmail(), emailUsernameRegister, emailPasswordRegister,
					NFVConstants.REGISTER_EMAIL_SUBJECT.replace("$projectName", projectName),
					emailContent.emailNotificationDetailsForTenant(registration, "registration_template.vm", tenantRegisterPageName, ""));
			registrationRepository.save(registration);
			logger.info("tenantmanagement end : ");
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error("", e);
			if (e instanceof NFVException)
				throw (NFVException)e;
		}
		throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/redirecttoregister/{token}/{email}/{tenantname}", method = RequestMethod.GET)
	public ResponseEntity<Void> redirecttoregister(@PathVariable("token") String token, @PathVariable("email") String email, @PathVariable("tenantname") String tenantname) throws NFVException, URISyntaxException 
	{
		Registration registration = registrationRepository.findByToken(token);
		logger.info("inside redirecttosetpassword started || registration : " + registration.toString());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/registertenantadmin", method = RequestMethod.POST)
	public ResponseEntity<Void> registertenantadmin(@RequestBody Registration registration) throws NFVException {
		try {
			logger.info("registertenantadmin started || registration : " + registration);
			Registration registration1 = registrationRepository.findByToken(registration.getToken());; 
			if (null == registration.getToken() || null == registration1)
			{
				logger.warn("User not found: " + registration.getToken());
				throw new NFVException(messageSource.getMessage("not.found", new String[]{registration.getToken()}, null, utility.getLocale()), HttpStatus.BAD_REQUEST);
			}
			if(null != registration.getToken() && (System.currentTimeMillis() - Long.parseLong(registration1.getTokenCreationTime())) < 1 * 24 * 60 * 60 * 1000)
			{
				logger.info("existingToken : "+ registration1);
				registration.setRoleId(registration1.getRoleId());
				if(registration.getRoleId().equals(enduserRoleId))
					registration.setTenantid(registration1.getTenantid());
				registrationRepository.delete(registration1.getId());

				registration = utility.generateToken(registration);
				mail.sendEmail(
						registration.getEmail(), emailUsernameRegister, emailPasswordRegister,
						NFVConstants.SETPASSWORD_EMAIL_SUBJECT.replace("$projectName", projectName),
						emailContent.emailNotificationDetails(registration, "password_recovery_template.vm", setpasswordPageName, registration.getFirstname()));
				registrationRepository.save(registration);
				logger.info("registertenantadmin end : ");
				return new ResponseEntity<>(HttpStatus.OK);
			}
			else
			{
				logger.warn("Token has been expired : " + registration.getToken());
				throw new NFVException(messageSource.getMessage("email.link.expired", null, utility.getLocale()), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.error("", e);
			if (e instanceof NFVException)
				throw (NFVException)e;
		}
		throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
	}


	@RequestMapping(value = "/forgotpassword", method = RequestMethod.POST)
	public ResponseEntity<Void> forgotpassword(@RequestBody ForgotPassword forgotPassword) throws NFVException 
	{
		int statusCode = 0;
		try 
		{
			logger.info("forgotpassword end : ");
			User user = userRepository.findByUsername(forgotPassword.getUsername());
			if(null != user && null !=  user.getEmail())
			{
				forgotPassword.setEmail(user.getEmail());
				statusCode = 200;
			}
			else
			{
				SearchUser su = new SearchUser();
				su.setPrincipal(forgotPassword.getUsername());
				user = new User();
				List<Beans> beans = utility.openiamSearch(su);
				if(null != beans)
				{
					for (Beans bean: beans) 
					{
						if(null !=  bean.getEmail())
							user.setEmail(bean.getEmail());
						user.setName(bean.getName());
						user.setOpeniamid(bean.getId());
						Tenant tenant = tenantRepository.findByName(bean.getTitle());
						if(null != tenant)
							user.setTenant(tenant);
					}
					userRepository.save(user);
					statusCode = 200;
				}
				else
					statusCode = 500;
			}
			if(statusCode == 200)
			{
				forgotPassword.setEmail(user.getEmail());
				forgotPassword.setOpeniamid(user.getOpeniamid());
				forgotPassword = utility.generateToken(forgotPassword);
				mail.sendEmail(
						forgotPassword.getEmail(), emailUsernameRegister, emailPasswordRegister,
						NFVConstants.SETPASSWORD_EMAIL_SUBJECT.replace("$projectName", projectName),
						emailContent.emailNotificationDetails(forgotPassword, "password_recovery_template.vm", forgotPasswordPage, ""));
				forgotPasswordRepository.save(forgotPassword);
				logger.info("forgotpassword end : ");
				return new ResponseEntity<>(HttpStatus.OK);
			}
			else
			{
				logger.info(" error happened while creating user into DB ");
				throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		catch (Exception e) {
			logger.error("", e);
			if (e instanceof NFVException)
				throw (NFVException)e;
		}
		throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/redirecttoforgotpassword/{token}", method = RequestMethod.GET)
	public ResponseEntity<Void> redirecttoforgotpassword(@PathVariable("token") String token) throws NFVException, URISyntaxException 
	{
		Registration registration = registrationRepository.findByToken(token);
		logger.info("inside redirecttosetpassword started || registration : " + registration.toString());
		return new ResponseEntity<>(HttpStatus.OK);
	}


	@RequestMapping(value = "/setforgotpassword/{token}", method = RequestMethod.POST)
	public ResponseEntity<Void> setforgotpassword(@PathVariable("token") String token, @RequestBody ChangePassword changePassword) throws NFVException
	{
		logger.info("setforgotpassword started || token : " + token);
		ForgotPassword forgotPassword = forgotPasswordRepository.findByToken(token);
		if (null == forgotPassword)
		{
			logger.warn("User not found: " + token);
			throw new NFVException(messageSource.getMessage("not.found", new String[]{token}, null, utility.getLocale()), HttpStatus.BAD_REQUEST);
		}
		if(null != forgotPassword && (System.currentTimeMillis() - Long.parseLong(forgotPassword.getTokenCreationTime())) < 1 * 24 * 60 * 60 * 1000)
		{
			try 
			{
				Map<String, String> map = utility.openimLogin();
				if(null != map)
				{
					ResetPassword resetPassword = new ResetPassword();
					resetPassword.setUserId(forgotPassword.getOpeniamid());
					resetPassword.setPassword(changePassword.getNewPassword());
					resetPassword.setConfPassword(changePassword.getNewPassword());
					restClient.post(openiamResetPasswordURI, map, utility.getObjectMapper().writeValueAsString(resetPassword));
					return new ResponseEntity<>(HttpStatus.OK);
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		else
		{
			logger.warn("Token has been expired : " + token);
			throw new NFVException(messageSource.getMessage("email.link.expired", null, utility.getLocale()), HttpStatus.BAD_REQUEST);
		}
		throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
	}


	/*@RequestMapping(value = "/adduser", method = RequestMethod.POST)
	public ResponseEntity<Void> adduser(@RequestBody Registration registration) throws NFVException {
		try {
			logger.info("adduser started || registration : " + registration);
			User user = utility.authorize();
			Registration registrationExist = registrationRepository.findByEmail(registration.getEmail());
			if(null != registrationExist)
			{
				logger.warn("Email already exists: " + registrationExist.getEmail());
				throw new NFVException(messageSource.getMessage("already.exists", new String[]{NFVConstants.EMAIL}, utility.getLocale()), HttpStatus.BAD_REQUEST);
			}
			registration = utility.generateToken(registration);
			mail.sendEmail(
					registration.getEmail(), emailUsernameRegister, emailPasswordRegister,
					"Activate your "+ projectName + " Account",
					emailContent.emailNotificationDetails(registration, "registration_template.vm", adduserSetpasswordPage, null));
			logger.info("adduser end : ");
			if(null != user)
			{
				registration.setTenantid(user.getTenant().getId());
				registration.setTenantname(user.getTenant().getName());
			}
			registrationRepository.save(registration);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error("", e);
			if (e instanceof NFVException)
				throw (NFVException)e;
		}
		throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	 */
	@RequestMapping(value = "/adduser", method = RequestMethod.POST)
	public ResponseEntity<Void> adduser(@RequestBody AddUser addUser) throws NFVException {
		try {
			logger.info("adduser started || addUser : " + addUser);
			User user = utility.authorize();
			if(null != addUser.getEmailIds() && addUser.getEmailIds().size() > 0)
			{
				for(String email : addUser.getEmailIds())
				{
					Registration registration = new Registration();
					if(addUser.getRoleName().equals(tenantadminRoleName))
						registration.setRoleId(tenantadminRoleId);
					else if(addUser.getRoleName().equals(enduserRoleName))
						registration.setRoleId(enduserRoleId);
					registration.setTenantid(user.getTenant().getId());
					registration.setTenantname(user.getTenant().getName());
					registration.setEmail(email);
					registration = utility.generateToken(registration);
					mail.sendEmail(
							registration.getEmail(), emailUsernameRegister, emailPasswordRegister,
							NFVConstants.REGISTER_EMAIL_SUBJECT.replace("$projectName", projectName),
							emailContent.emailNotificationDetailsForTenant(registration, "registration_template.vm", redirectToAdduser, ""));
					registrationRepository.save(registration);
				}
				logger.info("adduser end : ");
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("", e);
			if (e instanceof NFVException)
				throw (NFVException)e;
		}
		throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/redirecttoaddusersetpassword/{token}", method = RequestMethod.GET)
	public ResponseEntity<Void> redirecttoaddusersetpassword(@PathVariable("token") String token) throws NFVException, URISyntaxException 
	{
		Registration registration = registrationRepository.findByToken(token);
		logger.info("inside redirecttoaddusersetpassword started || registration : " + registration.toString());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/setpasswordforadduser/{token}", method = RequestMethod.POST)
	public ResponseEntity<Void> setpasswordforuser(@PathVariable("token") String token, @RequestBody SetPassword setPassword) throws NFVException 
	{
		logger.info("setpasswordforadduser started || token : " + token);
		Registration registration = registrationRepository.findByToken(token);
		if (null == registration)
		{
			logger.warn("User not found: " + token);
			throw new NFVException(messageSource.getMessage("not.found", new String[]{token}, null, utility.getLocale()), HttpStatus.BAD_REQUEST);
		}
		if(null != registration && (System.currentTimeMillis() - Long.parseLong(registration.getTokenCreationTime())) < 1 * 24 * 60 * 60 * 1000)
		{
			try
			{
				URI opennfv = new URI("http://"+emailHostLink);
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setLocation(opennfv);

				logger.info(" setpasswordforadduser registration : " + registration);
				String password = setPassword.getNewPassword();
				Register register = new Register();
				register.setFirstName(registration.getFirstname());
				register.setLastName(registration.getLastname());
				register.setLogin(setPassword.getUsername());
				register.setPassword(password);
				register.setRoleId(registration.getRoleId());
				Email email = new Email();
				email.setEmail(registration.getEmail());
				register.setEmail(email);
				register.setTitle(registration.getTenantname());

				Map<String, String> map = utility.openimLogin();
				if(null != map)
				{
					String result = restClient.post(openiamRegisterURI, map, utility.getObjectMapper().writeValueAsString(register));
					RestRegister object = utility.getObjectMapper().readValue(result, RestRegister.class);
					if (object.getStatus() != 200) {
						throw new NFVException(messageSource.getMessage("already.exists", new String[]{USER}, utility.getLocale()), HttpStatus.BAD_REQUEST);
					}

					String userId = object.getContextValues().getUserId();
					restClient.post(openiamActivateUserURI, map, "{\"id\":\"" + userId + "\"}");

					ResetPassword resetPassword = new ResetPassword();
					resetPassword.setUserId(userId);
					resetPassword.setPassword(password);
					resetPassword.setConfPassword(password);
					restClient.post(openiamResetPasswordURI, map, utility.getObjectMapper().writeValueAsString(resetPassword));
					Registration existingToken = registrationRepository.findByToken(registration.getToken());
					if(null != existingToken)
						registrationRepository.delete(existingToken.getId());

					logger.info("setpasswordforadduser end : " + token);
					return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
				}
			}
			catch (Exception e) {
				logger.error("", e);
				if (e instanceof NFVException)
					throw (NFVException)e;
			}
		}
		else
		{
			logger.warn("Token has been expired : " + token);
			throw new NFVException(messageSource.getMessage("email.link.expired", null, utility.getLocale()), HttpStatus.BAD_REQUEST);
		}
		throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

//	private void tenantprojectinfo(@RequestBody Tenant tenant) {
//		Tenant t = tenantRepository.save(tenant);
//		TenantCreationUtil tenantCreationUtil = new TenantCreationUtil();
//		tenantCreationUtil.set(restClient, contentTypeKey, contentTypeValue, authTokenKey, postOSProjectURI, postOSUsersURI, getOSRolesURI, tenantRepository, t, getFloatingIPsURI);
//		tenantCreationUtil.start();
//	}

	@RequestMapping(value = "/vim/{vimId}/tenant/{id}/allocatedip", method = RequestMethod.GET)
	public ResponseEntity<int[]> saveTenant(@PathVariable("vimId") Long vimId, @PathVariable("id") Long id) throws NFVException {
		User user = utility.getUser();
		if (!utility.isAdminUser(user))
			throw new NFVException(messageSource.getMessage("user.unauthorized", null, utility.getLocale()), HttpStatus.UNAUTHORIZED);
		Tenant tenant = tenantRepository.findOne(id);
		if (tenant == null)
			throw new NFVException(messageSource.getMessage("not.found", new String[]{TENANT}, utility.getLocale()), HttpStatus.BAD_REQUEST);
		try {
			int[] arr = getPublicIPInfo(tenant, vimRepository.findOne(vimId));
			if (arr == null)
				throw new Exception("Not able to fetch existing allocation!");
			return new ResponseEntity<>(arr, HttpStatus.OK);
//			int allocated = arr[0];
//			int newAllocation = t.getAllocatedip();
//			if (allocated != newAllocation) {
//				if (newAllocation > allocated) {
//					String floatingNetworkId = getFloatingNetworkId(user.getTenant(), vimRepository.findOne(vimId));
//					int delta = newAllocation - allocated;
//					for (int i = 0; i < delta; i++) {
//						allocateFloatingIP(floatingNetworkId, tenant, vimRepository.findOne(vimId));
//					}
//				} else {
//					int used = arr[1];
//					if (newAllocation < used)
//						throw new Exception("New allocation cannot be less than what is currently used!");
//					int delta = allocated - newAllocation;
//					for (int i = 0; i < delta; i++) {
//						deallocateFloatingIP(tenant, vimRepository.findOne(vimId));
//					}
//				}
//			}
		} catch (Exception e) {
			logger.error("", e);
			throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
//		return new ResponseEntity<>(tenantRepository.save(tenant), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/vim/{vimId}/tenant/{id}/incallocatedip", method = RequestMethod.PATCH)
	public ResponseEntity<Void> incallocatedip(@PathVariable("vimId") Long vimId, @PathVariable("id") Long id) throws NFVException {
		User user = utility.getUser();
		if (!utility.isAdminUser(user))
			throw new NFVException(messageSource.getMessage("user.unauthorized", null, utility.getLocale()), HttpStatus.UNAUTHORIZED);
		Tenant tenant = tenantRepository.findOne(id);
		if (tenant == null)
			throw new NFVException(messageSource.getMessage("not.found", new String[]{TENANT}, utility.getLocale()), HttpStatus.BAD_REQUEST);
		try {
			String floatingNetworkId = getFloatingNetworkId(user.getTenant(), vimRepository.findOne(vimId));
			allocateFloatingIP(floatingNetworkId, tenant, vimRepository.findOne(vimId));
		} catch (Exception e) {
			logger.error("", e);
			throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/vim/{vimId}/tenant/{id}/decallocatedip", method = RequestMethod.PATCH)
	public ResponseEntity<Void> decallocatedip(@PathVariable("vimId") Long vimId, @PathVariable("id") Long id) throws NFVException {
		User user = utility.getUser();
		if (!utility.isAdminUser(user))
			throw new NFVException(messageSource.getMessage("user.unauthorized", null, utility.getLocale()), HttpStatus.UNAUTHORIZED);
		Tenant tenant = tenantRepository.findOne(id);
		if (tenant == null)
			throw new NFVException(messageSource.getMessage("not.found", new String[]{TENANT}, utility.getLocale()), HttpStatus.BAD_REQUEST);
		try {
			deallocateFloatingIP(tenant, vimRepository.findOne(vimId));
		} catch (Exception e) {
			logger.error("", e);
			throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	private int[] getPublicIPInfo(Tenant tenant, Vim v) {
		try {
			int[] arr = new int[2];
			String authTokenValue = restClient.getToken(tenant, v);
			ObjectMapper mapper = utility.getObjectMapper();
			Map<String, String> map = new HashMap<>();
			map.put(authTokenKey, authTokenValue);
			String result = restClient.get(NFVConstants.HTTP + v.getIpaddress() + getFloatingIPsURI, map);
			RestFloatingIP object = mapper.readValue(result, RestFloatingIP.class);
			List<FloatingIP> list = object.getFloatingips();
			int allocated = 0;
			int used = 0;
			String projectId = null;
			if (tenant.getName().equals(tenantadminDbName))
				projectId = v.getAdminprojectid();
			else
				projectId = tenant.getProjectid();
			for (FloatingIP ip: list) {
				if (ip.getTenant_id().equals(projectId)) {
					allocated++;
					if (!ip.getStatus().equals("DOWN"))
						used++;
				}
			}
			arr[0] = allocated;
			arr[1] = used;
			return arr;
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	private String getFloatingNetworkId(Tenant tenant, Vim v) throws Exception {
		String authTokenValue = restClient.getToken(tenant, v);
		ObjectMapper mapper = utility.getObjectMapper();
		Map<String, String> map = new HashMap<>();
		map.put(authTokenKey, authTokenValue);
		String result = restClient.get(NFVConstants.HTTP + v.getIpaddress() + getFloatingIPsURI, map);
		RestFloatingIP object = mapper.readValue(result, RestFloatingIP.class);
		List<FloatingIP> list = object.getFloatingips();
		if (!list.isEmpty())
			return list.get(0).getFloating_network_id();
		else
			return null;
	}
	
	private void allocateFloatingIP(String floating_network_id, Tenant tenant, Vim v) throws Exception {
		String authTokenValue = restClient.getToken(tenant, v);
		ObjectMapper mapper = utility.getObjectMapper();
		Map<String, String> map = new HashMap<>();
		map.put(authTokenKey, authTokenValue);
		map.put(contentTypeKey, contentTypeValue);
		FloatingIP floatingIP = new FloatingIP();
		floatingIP.setFloating_network_id(floating_network_id);
		RestFloatingIP restFloatingIP = new RestFloatingIP();
		restFloatingIP.setFloatingip(floatingIP);
		restClient.post(NFVConstants.HTTP + v.getIpaddress() + getFloatingIPsURI, map, mapper.writeValueAsString(restFloatingIP));
	}

	
	private void deallocateFloatingIP(Tenant tenant, Vim v) throws Exception {
		String authTokenValue = restClient.getToken(tenant, v);
		ObjectMapper mapper = utility.getObjectMapper();
		Map<String, String> map = new HashMap<>();
		map.put(authTokenKey, authTokenValue);
		String result = restClient.get(NFVConstants.HTTP + v.getIpaddress() + getFloatingIPsURI, map);
		RestFloatingIP object = mapper.readValue(result, RestFloatingIP.class);
		List<FloatingIP> list = object.getFloatingips();
		String projectId = null;
		if (tenant.getName().equals(tenantadminDbName))
			projectId = v.getAdminprojectid();
		else
			projectId = tenant.getProjectid();
		String id = null;
		for (FloatingIP ip: list) {
			if (ip.getTenant_id().equals(projectId) && ip.getStatus().equalsIgnoreCase("DOWN")) {
				id = ip.getId();
				break;
			}
		}
		restClient.delete(NFVConstants.HTTP + v.getIpaddress() + getFloatingIPsURI + "/" + id, map);
	}
}