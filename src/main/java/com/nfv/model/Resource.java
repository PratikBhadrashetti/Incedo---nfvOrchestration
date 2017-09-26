package com.nfv.model;

import java.util.List;

public class Resource {

	private String resource_name;
	
	private String resource_status;
	
	private List<String> required_by;
	
	private String physical_resource_id;

	public String getResource_name() {
		return resource_name;
	}

	public void setResource_name(String resource_name) {
		this.resource_name = resource_name;
	}

	public String getResource_status() {
		return resource_status;
	}

	public void setResource_status(String resource_status) {
		this.resource_status = resource_status;
	}

	public List<String> getRequired_by() {
		return required_by;
	}

	public void setRequired_by(List<String> required_by) {
		this.required_by = required_by;
	}

	public String getPhysical_resource_id() {
		return physical_resource_id;
	}

	public void setPhysical_resource_id(String physical_resource_id) {
		this.physical_resource_id = physical_resource_id;
	}
}
