/**
 * 
 */
package com.nfv.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "forgotpassword")
public class ForgotPassword {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "email", length = 512, nullable = false)
	private String email;
	
	@Column(name = "username", length = 512, nullable = false)
	private String username;
	
	@Column(name = "token",length = 512,nullable = false)
    private String token;
    
	@Column(name = "tokenCreationTime",length = 512,nullable = true)
    private String tokenCreationTime;
	
	@Column(name = "openiamid", length = 512, nullable = false)
	private String openiamid;
	
	public ForgotPassword() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTokenCreationTime() {
		return tokenCreationTime;
	}

	public void setTokenCreationTime(String tokenCreationTime) {
		this.tokenCreationTime = tokenCreationTime;
	}

	public String getOpeniamid() {
		return openiamid;
	}

	public void setOpeniamid(String openiamid) {
		this.openiamid = openiamid;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "ForgotPassword [id=" + id + ", email=" + email + ", username=" + username + ", token=" + token
				+ ", tokenCreationTime=" + tokenCreationTime + ", openiamid=" + openiamid + "]";
	}
	
}
