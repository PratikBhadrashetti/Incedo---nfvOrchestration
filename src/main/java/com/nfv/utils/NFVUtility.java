package com.nfv.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nfv.entity.Catalog;
import com.nfv.entity.ForgotPassword;
import com.nfv.entity.Registration;
import com.nfv.entity.Tenant;
import com.nfv.entity.User;
import com.nfv.entity.Vnf;
import com.nfv.entity.VnfInstance;
import com.nfv.error.NFVException;
import com.nfv.model.Beans;
import com.nfv.model.Login;
import com.nfv.model.RestUser;
import com.nfv.model.SearchUser;
import com.nfv.repository.TenantRepository;
import com.nfv.repository.UserRepository;

@Component
public class NFVUtility {

	private static final Logger logger = LoggerFactory.getLogger(NFVUtility.class);

	@Value("${admin.role}")
	String adminRole;

	@Value("${tenant.admin.role}")
	String tenantAdminRole;
	
	@Value("${post.openiam.login.uri}")
	String openiamLoginURI;
	
	@Value("${openiam.admin}")
	String adminUsername;

	@Value("${openiam.password}")
	String adminPassword;
	
	@Value("${content.type.key}")
	String contentTypeKey;

	@Value("${content.type.value}")
	String contentTypeValue;
	
	@Value("${post.openiam.search.uri}")
	String openiamSearchURI;
	
	final String NETWORK_STR="management network dynamic parameter\r\n      default: ";
	
	final String network_regex = "(?m)management network dynamic parameter\r\n      default:.*";

	@Resource
	UserRepository userRepository;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	RestClient restClient;

	@Resource
	private TenantRepository tenantRepository;

	public User getUser() throws NFVException {
		return authorize();
	}

	public Locale getLocale() {
		return Locale.US;
	}

	public boolean isAdminUser(User user) {
		if (user.getRole().equals(adminRole)) {
			return true;
		}
		return false;
	}

	public boolean isTenantAdminUser(User user) {
		if (user.getRole().equals(tenantAdminRole)) {
			return true;
		}
		return false;
	}

	public void checkPermissions(Catalog catalog) throws NFVException {
		boolean hasPermissions = false;
		Tenant tenant = catalog.getTenant();
		User user = getUser();
		if (isAdminUser(user))
			hasPermissions = true;
		else if (tenant == null || user.getTenant() == tenant) {
			if (isTenantAdminUser(user)) 
				hasPermissions = true;
			else if (catalog.getUser().contains(user)) {
				hasPermissions = true;
			}
		}
		if (!hasPermissions)
			throw new NFVException(messageSource.getMessage("user.unauthorized", null, getLocale()), HttpStatus.UNAUTHORIZED);
	}

	public void checkPermissions(VnfInstance vnfInstance) throws NFVException {
		boolean hasPermissions = false;
		Tenant tenant = vnfInstance.getTenant();
		User user = getUser();
		if (isAdminUser(user))
			hasPermissions = true;
		else if (user.getTenant() == tenant) {
			if (isTenantAdminUser(user)) 
				hasPermissions = true;
			else if (vnfInstance.getUser().contains(user)) {
				hasPermissions = true;
			}
		}
		if (!hasPermissions)
			throw new NFVException(messageSource.getMessage("user.unauthorized", null, getLocale()), HttpStatus.UNAUTHORIZED);
	}

	public void checkPermissions(Vnf vnf) throws NFVException {
		boolean hasPermissions = false;
		Tenant tenant = vnf.getTenant();
		User user = getUser();
		if (isAdminUser(user))
			hasPermissions = true;
		else if (tenant == null) {
			hasPermissions = true;
		} else if (user.getTenant() == tenant) {
			hasPermissions = true;
		} else {
			Set<Catalog> catalogs = vnf.getCatalog();
			for (Catalog catalog: catalogs) {
				if (catalog.getTenant() == null) {
					hasPermissions = true;
					break;
				}
			}
		}
		if (!hasPermissions)
			throw new NFVException(messageSource.getMessage("user.unauthorized", null, getLocale()), HttpStatus.UNAUTHORIZED);
	}

	public void checkPermissions(User user) throws NFVException {
		boolean hasPermissions = false;
		User loggedInUser = getUser();
		if (isAdminUser(loggedInUser))
			hasPermissions = true;
		else if (isTenantAdminUser(loggedInUser)) {
			if (user.getTenant() == loggedInUser.getTenant())
				hasPermissions = true;
		} else {
			if (user == loggedInUser)
				hasPermissions = true;
		}
		if (!hasPermissions)
			throw new NFVException(messageSource.getMessage("user.unauthorized", null, getLocale()), HttpStatus.UNAUTHORIZED);
	}

	public void checkAdminPermissions() throws NFVException {
		User user = getUser();
		if (!isAdminUser(user) && !isTenantAdminUser(user))
			throw new NFVException(messageSource.getMessage("user.unauthorized", null, getLocale()), HttpStatus.UNAUTHORIZED);
	}

	public User authorize() throws NFVException {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByUsername(username);
		if (user == null)
			throw new NFVException(messageSource.getMessage("user.unauthorized", null, getLocale()), HttpStatus.UNAUTHORIZED);
		return user;
	}

	public Registration generateToken(Registration registration) {
		registration.setToken(UUID.randomUUID().toString());
		registration.setTokenCreationTime(String.valueOf(System.currentTimeMillis()));
		return registration;
	}

	public ForgotPassword generateToken(ForgotPassword forgotPassword) {
		forgotPassword.setToken(UUID.randomUUID().toString());
		forgotPassword.setTokenCreationTime(String.valueOf(System.currentTimeMillis()));
		return forgotPassword;
	}


	public List<Beans> openiamSearch(SearchUser su) throws NFVException 
	{
		try 
		{
			Map<String, String> map = openimLogin();
			if(null != map)
			{
				String searchresult = restClient.post(openiamSearchURI, map, getObjectMapper().writeValueAsString(su));
				RestUser object = getObjectMapper().readValue(searchresult, RestUser.class);
				List<Beans> beans = object.getBeans();
				return beans;
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new NFVException(messageSource.getMessage("unexpected.error", null, getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	public Map<String, String> openimLogin() throws NFVException
	{		
		String uri = openiamLoginURI + adminUsername + "&password=" + adminPassword;
		String result;
		try {
			result = restClient.post(uri, null, null);
			Login objectLogin = getObjectMapper().readValue(result, Login.class);
			Long status = objectLogin.getStatus();
			if (null != status && status == 200) 
			{
				String authToken = "OPENIAM_AUTH_TOKEN=\"" + objectLogin.getTokenInfo().getAuthToken() + "\"";
				Map<String, String> map = new HashMap<>();
				map.put("Cookie", authToken);
				map.put(contentTypeKey, contentTypeValue);
				return map;
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new NFVException(messageSource.getMessage("unexpected.error", null, getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	public ObjectMapper getObjectMapper()
	{
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		return mapper;
	}
	
	public String replaceBy(String input, String replaceStr)
	{
		return input.replaceAll(network_regex, NETWORK_STR+replaceStr);
	}
}
