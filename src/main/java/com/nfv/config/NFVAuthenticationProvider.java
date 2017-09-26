package com.nfv.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nfv.entity.Tenant;
import com.nfv.entity.User;
import com.nfv.error.NFVException;
import com.nfv.model.Beans;
import com.nfv.model.Login;
import com.nfv.model.RestUser;
import com.nfv.model.SearchUser;
import com.nfv.repository.RegistrationRepository;
import com.nfv.repository.TenantRepository;
import com.nfv.repository.UserRepository;
import com.nfv.utils.NFVConstants;
import com.nfv.utils.NFVUtility;
import com.nfv.utils.RestClient;

@Component
public class NFVAuthenticationProvider implements AuthenticationProvider {
	private static final Logger logger = LoggerFactory.getLogger(NFVAuthenticationProvider.class);

	@Value("${disable.openiam}")
	String disableOpeniam;

	@Value("${post.openiam.login.uri}")
	String openiamLoginURI;

	@Value("${get.openiam.role.uri}")
	String openiamRoleURI;
	
	@Value("${content.type.key}")
	String contentTypeKey;

	@Value("${content.type.value}")
	String contentTypeValue;
	
	@Value("${enduser.roleid}")
	String enduserRoleId;
	
	@Value("${tenantadmin.roleid}")
	String tenantadminRoleId;
	
	@Value("${enduser.role.name}")
	String enduserRoleName;
	
	@Value("${tenant.admin.role}")
	String tenantadminRoleName;
	
	@Value("${admin.role}")
	String superadminRoleName;
	
	@Value("${tenantadmin.db.name}")
	String tenantadminDbName;

	@Value("${email.username.register}")
	String emailUsernameRegister;
	
	@Value("${post.openiam.search.uri}")
	String openiamSearchURI;
	
	@Resource
	UserRepository userRepository;

	@Resource
	RegistrationRepository registrationRepository;

	@Autowired
	RestClient restClient;

	@Autowired
	NFVUtility utility;

	@Resource
	private TenantRepository tenantRepository;

	@Autowired
	private MessageSource messageSource;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (authentication.getName() == null || authentication.getName().isEmpty()) {
			throw new BadCredentialsException("Invalid Credentials");
		}

		if (authentication.getCredentials().toString().isEmpty()) {
			throw new BadCredentialsException("Invalid Credentials");
		}

		String password = (String) authentication.getCredentials();
		String role = validate(authentication.getName(), password);
		if (null != role) {
			return new UsernamePasswordAuthenticationToken(authentication.getName(), authentication.getCredentials(), grantRole(role));
		}

		throw new BadCredentialsException("Invalid Credentials");
	}

	private String validate(String username, String password) {
		if (Boolean.parseBoolean(disableOpeniam)) {
			return "Super Security Admin";
		} else {
			String uri = openiamLoginURI + username + "&password=" + password;
			logger.info(username + "||" + password);
			try {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				String result = restClient.post(uri, null, null);
				Login object = mapper.readValue(result, Login.class);
				Long status = object.getStatus();
				if (null != status && status == 200) {
					String openiamid = object.getUserId();
					String authToken = "OPENIAM_AUTH_TOKEN=\"" + object.getTokenInfo().getAuthToken() + "\"";
					uri = openiamRoleURI + openiamid + "&from=-1&size=-1";
					Map<String, String> map = new HashMap<>();
					map.put("Cookie", authToken);
					map.put(contentTypeKey, contentTypeValue);
					result = restClient.get(uri, map);
					RestUser roleObject = mapper.readValue(result, RestUser.class);
					List<Beans> beans = roleObject.getBeans();
					if (beans != null && !beans.isEmpty()) {
						String role = beans.iterator().next().getName();
						if (null != role) {
							User user = userRepository.findByOpeniamid(openiamid);
							if (null == user) 
							{
								user = new User();
								User existingUsername = userRepository.findByUsername(username);
								if(null != existingUsername)
									userRepository.delete(existingUsername.getId());
								
								user.setOpeniamid(openiamid);
								SearchUser su = new SearchUser();
								List<String> roleIds = new ArrayList<>();
								if(role.equals(superadminRoleName))
								{
									Tenant admintenant = tenantRepository.findByName(tenantadminDbName);
									if(admintenant != null)
										user.setTenant(admintenant);
									user.setEmail(emailUsernameRegister);
								}
								else if(role.equals(tenantadminRoleName))
								{
									roleIds.add(tenantadminRoleId);
									su.setRoleIds(roleIds);
									user = openiamSearchApi(map, mapper, su, openiamid, user);	
								}
								else if(role.equals(enduserRoleName))
								{
									roleIds.add(enduserRoleId);
									su.setRoleIds(roleIds);
									user = openiamSearchApi(map, mapper, su, openiamid, user);
								}
							}
							user.setUsername(username);
							user.setRole(role);
							userRepository.save(user);
							return role;
						}
					}
				}
			} catch (Exception e) {
				logger.error("", e);
			}
			return null;
		}
	}

	private List<GrantedAuthority> grantRole(String role) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(role));
		return authorities;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		if (authentication == null)
			return false;
		return Authentication.class.isAssignableFrom(authentication);
	}

	private User openiamSearchApi(Map<String, String> map, ObjectMapper mapper, SearchUser su, String openiamid, User user) throws NFVException
	{
		try 
		{
			String result = restClient.post(openiamSearchURI, map, mapper.writeValueAsString(su));
			RestUser object = mapper.readValue(result, RestUser.class);
			List<Beans> beans = object.getBeans();
			for (Beans bean: beans) {
				if(bean.getId().equals(openiamid))
				{
					if(null != bean.getEmail())
						user.setEmail(bean.getEmail());
					Tenant tenant = tenantRepository.findByName(bean.getTitle());
					if(null != tenant)
					{
						user.setTenant(tenant);
						break;
					}
					else
						throw new NFVException(messageSource.getMessage("not.found", new String[]{NFVConstants.TENANT_NAME}, utility.getLocale()), HttpStatus.BAD_REQUEST);
				}
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return user;
	}
}