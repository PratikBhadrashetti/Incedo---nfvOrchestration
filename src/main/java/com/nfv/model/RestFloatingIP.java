package com.nfv.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class RestFloatingIP {

	@JsonProperty(access = Access.WRITE_ONLY)
	private List<FloatingIP> floatingips;
	
	@JsonProperty(access = Access.READ_ONLY)
	private FloatingIP floatingip;

	public List<FloatingIP> getFloatingips() {
		return floatingips;
	}

	public void setFloatingips(List<FloatingIP> floatingips) {
		this.floatingips = floatingips;
	}

	public FloatingIP getFloatingip() {
		return floatingip;
	}

	public void setFloatingip(FloatingIP floatingip) {
		this.floatingip = floatingip;
	}
}
