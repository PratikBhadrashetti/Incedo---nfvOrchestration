package com.nfv.model;

public class ResetPassword {

	private String userId;
	
	private String password;
	
	private String confPassword;
	
	private String principal = "admin";
	
	private String[] managedSystem = new String[]{"0", "101"};
	
	private boolean notifyUserViaEmail = false;
	
	private boolean autoGeneratePassword = false;
	
	private boolean userActivateFlag = false;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfPassword() {
		return confPassword;
	}

	public void setConfPassword(String confPassword) {
		this.confPassword = confPassword;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String[] getManagedSystem() {
		return managedSystem;
	}

	public void setManagedSystem(String[] managedSystem) {
		this.managedSystem = managedSystem;
	}

	public boolean isNotifyUserViaEmail() {
		return notifyUserViaEmail;
	}

	public void setNotifyUserViaEmail(boolean notifyUserViaEmail) {
		this.notifyUserViaEmail = notifyUserViaEmail;
	}

	public boolean isAutoGeneratePassword() {
		return autoGeneratePassword;
	}

	public void setAutoGeneratePassword(boolean autoGeneratePassword) {
		this.autoGeneratePassword = autoGeneratePassword;
	}

	public boolean isUserActivateFlag() {
		return userActivateFlag;
	}

	public void setUserActivateFlag(boolean userActivateFlag) {
		this.userActivateFlag = userActivateFlag;
	}
}
