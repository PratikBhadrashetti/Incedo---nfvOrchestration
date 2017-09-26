package com.nfv.model;

public class VnfdVnf {
	private VnfdAttributes attributes;
	
//	private String vim_id;
	
	private String description = "demo";
	
	private String vnfd_id;
	
	private String name;
	
	private String tenant_id;

	public VnfdAttributes getAttributes() {
		return attributes;
	}

	public void setAttributes(VnfdAttributes attributes) {
		this.attributes = attributes;
	}

//	public String getVim_id() {
//		return vim_id;
//	}
//
//	public void setVim_id(String vim_id) {
//		this.vim_id = vim_id;
//	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVnfd_id() {
		return vnfd_id;
	}

	public void setVnfd_id(String vnfd_id) {
		this.vnfd_id = vnfd_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTenant_id() {
		return tenant_id;
	}

	public void setTenant_id(String tenant_id) {
		this.tenant_id = tenant_id;
	}
}
