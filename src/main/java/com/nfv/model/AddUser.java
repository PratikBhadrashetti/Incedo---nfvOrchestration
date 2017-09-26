package com.nfv.model;

import java.util.List;

public class AddUser {
	
	private String roleName;
	
	private List<String> emailIds;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<String> getEmailIds() {
		return emailIds;
	}

	public void setEmailIds(List<String> emailIds) {
		this.emailIds = emailIds;
	}

	@Override
	public String toString() {
		return "AddUser [roleName=" + roleName + ", emailIds=" + emailIds + "]";
	}


	

}
