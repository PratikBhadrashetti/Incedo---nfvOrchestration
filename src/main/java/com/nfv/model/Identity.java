package com.nfv.model;

public class Identity {

	private String[] methods = new String[]{"password"};
	
	private Password password;

	public String[] getMethods() {
		return methods;
	}

	public void setMethods(String[] methods) {
		this.methods = methods;
	}

	public Password getPassword() {
		return password;
	}

	public void setPassword(Password password) {
		this.password = password;
	}
}
