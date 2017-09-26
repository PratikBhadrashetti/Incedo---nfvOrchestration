package com.nfv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class FloatingIP {

	@JsonProperty(access = Access.WRITE_ONLY)
	private String status;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String floating_ip_address;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String tenant_id;
	
	private String floating_network_id;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String id;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFloating_ip_address() {
		return floating_ip_address;
	}

	public void setFloating_ip_address(String floating_ip_address) {
		this.floating_ip_address = floating_ip_address;
	}

	public String getTenant_id() {
		return tenant_id;
	}

	public void setTenant_id(String tenant_id) {
		this.tenant_id = tenant_id;
	}

	public String getFloating_network_id() {
		return floating_network_id;
	}

	public void setFloating_network_id(String floating_network_id) {
		this.floating_network_id = floating_network_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
