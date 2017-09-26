package com.nfv.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class Server {
	
	private String id;
	
	private String name;
	
	private String status;
	
	private String created;
	
	private Addresses addresses;
	
	private String imageRef;
	
	private String flavorRef = "3";
	
	private int max_count = 1;
	
	private int min_count = 1;
	
	private List<SecurityGroups> security_groups;
	
	private List<Networks> networks;
	
	private String user_data = "I2Nsb3VkLWNvbmZpZw0KcGFzc3dvcmQ6IGh1YXdlaQ0KY2hwYXNzd2Q6IHsgZXhwaXJlOiBGYWxzZSB9DQpzc2hfcHdhdXRoOiBUcnVl";
	
	private String config_drive = "true";
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Flavor flavor;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public Addresses getAddresses() {
		return addresses;
	}

	public void setAddresses(Addresses addresses) {
		this.addresses = addresses;
	}

	public String getImageRef() {
		return imageRef;
	}

	public void setImageRef(String imageRef) {
		this.imageRef = imageRef;
	}

	public String getFlavorRef() {
		return flavorRef;
	}

	public void setFlavorRef(String flavorRef) {
		this.flavorRef = flavorRef;
	}

	public int getMax_count() {
		return max_count;
	}

	public void setMax_count(int max_count) {
		this.max_count = max_count;
	}

	public int getMin_count() {
		return min_count;
	}

	public void setMin_count(int min_count) {
		this.min_count = min_count;
	}

	public List<SecurityGroups> getSecurity_groups() {
		return security_groups;
	}

	public void setSecurity_groups(List<SecurityGroups> security_groups) {
		this.security_groups = security_groups;
	}

	public List<Networks> getNetworks() {
		return networks;
	}

	public void setNetworks(List<Networks> networks) {
		this.networks = networks;
	}

	public String getUser_data() {
		return user_data;
	}

	public void setUser_data(String user_data) {
		this.user_data = user_data;
	}

	public String getConfig_drive() {
		return config_drive;
	}

	public void setConfig_drive(String config_drive) {
		this.config_drive = config_drive;
	}

	public Flavor getFlavor() {
		return flavor;
	}

	public void setFlavor(Flavor flavor) {
		this.flavor = flavor;
	}
}
