package com.nfv.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "registration")
public class Registration {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "firstname", length = 512, nullable = true)
	private String firstname;

	@Column(name = "lastname", length = 512, nullable = true)
	private String lastname;
	
	@Column(name = "email", length = 512, nullable = false)
	private String email;
	
	@Column(name = "username", length = 512, nullable = true)
	private String username;
	
	@Column(name = "token",length = 512,nullable = false)
    private String token;
    
	@Column(name = "tokenCreationTime",length = 512,nullable = false)
    private String tokenCreationTime;
	
	@Column(name = "roleId",length = 512,nullable = false)
	private String roleId;
	
	@Column(name = "tenantid", length = 10, nullable = true)
	private Long tenantid;

	@Column(name = "tenantname", length = 512, nullable = true)
	private String tenantname;
	
	public Registration() {
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

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
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

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public Long getTenantid() {
		return tenantid;
	}

	public void setTenantid(Long tenantid) {
		this.tenantid = tenantid;
	}

	public String getTenantname() {
		return tenantname;
	}

	public void setTenantname(String tenantname) {
		this.tenantname = tenantname;
	}

	@Override
	public String toString() {
		return "Registration [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", email=" + email
				+ ", username=" + username + ", token=" + token + ", tokenCreationTime=" + tokenCreationTime
				+ ", roleId=" + roleId + ", tenantid=" + tenantid + ", tenantname=" + tenantname + "]";
	}


	
}
