package com.nfv.model;

public class TenantProject {

	private String description;
	
	private String domain_id;
	
	private Boolean enabled = true;
	
	private Boolean is_domain = false;
	
	private String name;
	
	private String id;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Boolean getIs_domain() {
		return is_domain;
	}

	public void setIs_domain(Boolean is_domain) {
		this.is_domain = is_domain;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
