package com.nfv.model;

public class TenantUser {

	private String default_project_id;
	
	private String domain_id;
	
	private Boolean enabled = true;
	
	private String name;
	
	private String password;
	
	private String id;

	public String getDefault_project_id() {
		return default_project_id;
	}

	public void setDefault_project_id(String default_project_id) {
		this.default_project_id = default_project_id;
	}

	public String getDomain_id() {
		return domain_id;
	}

	public void setDomain_id(String domain_id) {
		this.domain_id = domain_id;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
