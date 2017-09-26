package com.nfv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class Router {

	private String name;
	
	private ExternalGatewayInfo external_gateway_info;
	
	private Boolean admin_state_up = true;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String id;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String tenant_id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ExternalGatewayInfo getExternal_gateway_info() {
		return external_gateway_info;
	}

	public void setExternal_gateway_info(ExternalGatewayInfo external_gateway_info) {
		this.external_gateway_info = external_gateway_info;
	}

	public Boolean getAdmin_state_up() {
		return admin_state_up;
	}

	public void setAdmin_state_up(Boolean admin_state_up) {
		this.admin_state_up = admin_state_up;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTenant_id() {
		return tenant_id;
	}

	public void setTenant_id(String tenant_id) {
		this.tenant_id = tenant_id;
	}
}
