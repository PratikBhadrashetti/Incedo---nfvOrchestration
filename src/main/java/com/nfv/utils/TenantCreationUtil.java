package com.nfv.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nfv.entity.Tenant;
import com.nfv.entity.Vim;
import com.nfv.model.FloatingIP;
import com.nfv.model.RestFloatingIP;
import com.nfv.model.RestProject;
import com.nfv.model.RestRoles;
import com.nfv.model.RestTenantUser;
import com.nfv.model.TenantProject;
import com.nfv.model.TenantRoles;
import com.nfv.model.TenantUser;
import com.nfv.repository.TenantRepository;

@Component
@Scope("prototype")
public class TenantCreationUtil extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(TenantCreationUtil.class);
	
	private String postOSProjectURI;
	
	private String postOSUsersURI;
	
	private String getOSRolesURI;
	
	private String getFloatingIPsURI;
	
	private String contentTypeKey;

	private String contentTypeValue;

	private String authTokenKey;

	private RestClient restClient;
	
	private TenantRepository tenantRepository;
	
	private Tenant tenant;
	
	private Vim vim;
	
	@Override
	public void run() {
		try {
			logger.debug("TenantCreationUtil start for " + tenant.getName());
			Tenant t = tenantRepository.findByName("Admin");
			String domainId = restClient.getDomainId(t, vim);
			String authTokenValue = restClient.getToken(t, vim);
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			Map<String, String> map = new HashMap<>();
			map.put(authTokenKey, authTokenValue);
			map.put(contentTypeKey, contentTypeValue);
			TenantProject tenantProject = new TenantProject();
			tenantProject.setName(tenant.getProject());
			tenantProject.setDescription(tenant.getProject());
			tenantProject.setDomain_id(domainId);
			RestProject projectObject = new RestProject();
			projectObject.setProject(tenantProject);
			
			String result = restClient.post(NFVConstants.HTTP + vim.getIpaddress() + postOSProjectURI, map, mapper.writeValueAsString(projectObject));
			projectObject = mapper.readValue(result, RestProject.class);
			String projectId = projectObject.getProject().getId();
			
			TenantUser tenantUser = new TenantUser();
			tenantUser.setDefault_project_id(projectId);
			tenantUser.setDomain_id(domainId);
			tenantUser.setName(tenant.getProjectusername());
			tenantUser.setPassword(tenant.getProjectpassword());
			RestTenantUser userObject = new RestTenantUser();
			userObject.setUser(tenantUser);
			
			result = restClient.post(NFVConstants.HTTP + vim.getIpaddress() + postOSUsersURI, map, mapper.writeValueAsString(userObject));
			userObject = mapper.readValue(result, RestTenantUser.class);
			String userId = userObject.getUser().getId();
			
			map.clear();
			map.put(authTokenKey, authTokenValue);
			result = restClient.get(NFVConstants.HTTP + vim.getIpaddress() + getOSRolesURI, map);
			RestRoles rolesObject = mapper.readValue(result, RestRoles.class);
			List<TenantRoles> roles = rolesObject.getRoles();
			String roleId = null;
			for (TenantRoles role: roles) {
				if (role.getName().equalsIgnoreCase("user")) {
					roleId = role.getId();
					break;
				}
			}
			if (roleId == null) {
				throw new Exception("*** Role not found. This is serious ***");
			}
			
			restClient.put(NFVConstants.HTTP + vim.getIpaddress() + postOSProjectURI + "/" + projectId + "/users/" + userId + "/roles/" + roleId, map);
			
			tenant.setProjectid(projectId);
			Set<Vim> vims = tenant.getVim();
			vims.add(vim);
			tenant.setVim(vims);
			tenantRepository.save(tenant);
			
			String floating_network_id = getFloatingNetworkId();
			if (null == floating_network_id) {
				throw new Exception("Not able to allocate floating IP!");
			}
			allocateFloatingIP(floating_network_id);
			logger.debug("TenantCreationUtil end for " + tenant.getName());
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public void set(RestClient restClient, String contentTypeKey, String contentTypeValue, String authTokenKey,
			String postOSProjectURI, String postOSUsersURI, String getOSRolesURI,
			TenantRepository tenantRepository, Tenant tenant, String getFloatingIPsURI, Vim vim) {
		this.restClient = restClient;
		this.contentTypeKey = contentTypeKey;
		this.contentTypeValue = contentTypeValue;
		this.authTokenKey = authTokenKey;
		this.postOSProjectURI = postOSProjectURI;
		this.postOSUsersURI = postOSUsersURI;
		this.getOSRolesURI = getOSRolesURI;
		this.tenantRepository = tenantRepository;
		this.tenant = tenant;
		this.getFloatingIPsURI = getFloatingIPsURI;
		this.vim = vim;
	}

	private String getFloatingNetworkId() throws Exception {
		Tenant adminTenant = tenantRepository.findByName("Admin");
		String authTokenValue = restClient.getToken(adminTenant, vim);
		ObjectMapper mapper = getObjectMapper();
		Map<String, String> map = new HashMap<>();
		map.put(authTokenKey, authTokenValue);
		String result = restClient.get(NFVConstants.HTTP + vim.getIpaddress() + getFloatingIPsURI, map);
		RestFloatingIP object = mapper.readValue(result, RestFloatingIP.class);
		List<FloatingIP> list = object.getFloatingips();
		if (!list.isEmpty())
			return list.get(0).getFloating_network_id();
		else
			return null;
	}
	
	private void allocateFloatingIP(String floating_network_id) throws Exception {
		String authTokenValue = restClient.getToken(tenant, vim);
		ObjectMapper mapper = getObjectMapper();
		Map<String, String> map = new HashMap<>();
		map.put(authTokenKey, authTokenValue);
		map.put(contentTypeKey, contentTypeValue);
		FloatingIP floatingIP = new FloatingIP();
		floatingIP.setFloating_network_id(floating_network_id);
		RestFloatingIP restFloatingIP = new RestFloatingIP();
		restFloatingIP.setFloatingip(floatingIP);
		restClient.post(NFVConstants.HTTP + vim.getIpaddress() + getFloatingIPsURI, map, mapper.writeValueAsString(restFloatingIP));
	}

	private ObjectMapper getObjectMapper()
	{
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		return mapper;
	}
}
