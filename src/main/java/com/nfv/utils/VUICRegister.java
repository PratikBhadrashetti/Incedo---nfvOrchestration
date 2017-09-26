package com.nfv.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
import com.nfv.entity.User;
import com.nfv.error.NFVException;
import com.nfv.model.RestRegisterVUIC;
import com.nfv.repository.UserRepository;

@Component
public class VUICRegister {
	private static final Logger logger = LoggerFactory.getLogger(VUICRegister.class);

	@Value("${content.type.key}")
	String contentTypeKey;
	
	@Value("${content.type.value}")
	String contentTypeValue;

	@Resource
	UserRepository userRepository;
	
	@Autowired
	RestClient restClient;

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	NFVUtility utility;
	
	public void manageUsers(String ip, Set<User> oldUsers, Set<User> newUsers) throws NFVException {
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
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			Map<String, String> map = new HashMap<>();
			map.put(contentTypeKey, contentTypeValue);
			map.put("apitype", "nfvUM");
			RestRegisterVUIC register = new RestRegisterVUIC();
			register.setPassword(password);
			register.setUsername(username);
			Set<String> users = new HashSet<>();
			if (!oldUsers.isEmpty()) {
				for (User u: oldUsers) {
					users.add(u.getOpeniamid());
				}
				register.setUsers(users);
				restClient.postVUIC("https://" + ip + ":9998/unregisterUsers", map, mapper.writeValueAsString(register));
				users.clear();
			}
			
			if (!newUsers.isEmpty()) {
				for (User u: newUsers) {
					User v = userRepository.findOne(u.getId());
					if (v != null)
						users.add(v.getOpeniamid());
				}
				register.setUsers(users);
				restClient.postVUIC("https://" + ip + ":9998/registerUsers", map, mapper.writeValueAsString(register));
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
