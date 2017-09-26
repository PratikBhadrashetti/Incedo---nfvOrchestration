package com.nfv.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class Vnfd {
	@JsonProperty(access = Access.WRITE_ONLY)
	private String id;
	
	private String name;
	
	private String description;
	
	private String tenant_id;
	
	private String mgmt_driver;
	
	private String infra_driver;
	
	private Attributes attributes;
	
	private List<ServiceTypes> service_types;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTenant_id() {
		return tenant_id;
	}

	public void setTenant_id(String tenant_id) {
		this.tenant_id = tenant_id;
	}

	public String getMgmt_driver() {
		return mgmt_driver;
	}

	public void setMgmt_driver(String mgmt_driver) {
		this.mgmt_driver = mgmt_driver;
	}

	public String getInfra_driver() {
		return infra_driver;
	}

	public void setInfra_driver(String infra_driver) {
		this.infra_driver = infra_driver;
	}

	public Attributes getAttributes() {
		return attributes;
	}

	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	public List<ServiceTypes> getService_types() {
		return service_types;
	}

	public void setService_types(List<ServiceTypes> service_types) {
		this.service_types = service_types;
	}
}
