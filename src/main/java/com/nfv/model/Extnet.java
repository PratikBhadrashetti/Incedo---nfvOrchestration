package com.nfv.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Extnet {
	
	private String addr;
	
	@JsonProperty("OS-EXT-IPS:type")
	private String type;

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
