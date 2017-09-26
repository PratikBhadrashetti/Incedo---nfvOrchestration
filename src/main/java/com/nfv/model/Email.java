/**
 * 
 */
package com.nfv.model;

/**
 * @author rohit.patel
 *
 */
public class Email {

	private String email;

	private String typeId = "PRIMARY_EMAIL";

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	@Override
	public String toString() {
		return "Email [email=" + email + ", typeId=" + typeId + "]";
	}
	
	
}
