package com.nfv.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name", length = 512, nullable = true)
	private String name;

	@Column(name = "openiamid", length = 512, nullable = false)
	private String openiamid;
	
	@Column(name = "role", length = 512, nullable = true)
	private String role;
	
	@Column(name = "username", length = 512, nullable = true)
	private String username;
	
	@Column(name = "email", length = 512, nullable = true)
	private String email;
	
	@JsonIgnore
	@ManyToMany(mappedBy="user")
	private Set<Catalog> catalog;

	@JsonIgnore
	@ManyToMany(mappedBy="user")
	private Set<VnfInstance> vnfInstance;

	@ManyToOne(optional=false)
	@JoinColumn(name="tenant_id", nullable=false)
	private Tenant tenant;

	public User() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOpeniamid() {
		return openiamid;
	}

	public void setOpeniamid(String openiamid) {
		this.openiamid = openiamid;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<Catalog> getCatalog() {
		return catalog;
	}

	public void setCatalog(Set<Catalog> catalog) {
		this.catalog = catalog;
	}

	public Set<VnfInstance> getVnfInstance() {
		return vnfInstance;
	}

	public void setVnfInstance(Set<VnfInstance> vnfInstance) {
		this.vnfInstance = vnfInstance;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", openiamid=" + openiamid + ", role=" + role + ", username="
				+ username + ", email=" + email + ", catalog=" + catalog + ", vnfInstance=" + vnfInstance + ", tenant="
				+ tenant + "]";
	}

	
}
