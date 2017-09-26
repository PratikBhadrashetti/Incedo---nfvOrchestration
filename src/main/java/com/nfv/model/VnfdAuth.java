package com.nfv.model;

public class VnfdAuth {
	private String tenantName;
	
	private PasswordCredentials passwordCredentials;

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public PasswordCredentials getPasswordCredentials() {
		return passwordCredentials;
	}

	public void setPasswordCredentials(PasswordCredentials passwordCredentials) {
		this.passwordCredentials = passwordCredentials;
	}
}
