package com.nfv.model;

public class SetPassword {

	private String newPassword;

	private String confirmPassword;
	
	private String username;
	
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "SetPassword [newPassword=" + newPassword + ", confirmPassword=" + confirmPassword + ", username="
				+ username + "]";
	}

	
	
}
