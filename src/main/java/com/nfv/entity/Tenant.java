package com.nfv.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "tenant")
public class Tenant {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name", length = 512, nullable = false)
	private String name;

	@Column(name = "project", length = 512, nullable = true)
	private String project;

	@Column(name = "projectid", length = 512, nullable = true)
	private String projectid;
	
	@Column(name = "projectusername", length = 512, nullable = true)
	private String projectusername;
	
	@Column(name = "projectpassword", length = 512, nullable = true)
	private String projectpassword;

//	@Column(name = "allocatedip", nullable = true)
//	private Integer allocatedip;
//
//	@Column(name = "usedip", nullable = true)
//	private Integer usedip;

	@JsonIgnore
	@OneToMany(mappedBy="tenant")
	private Set<User> user;

	@JsonIgnore
	@OneToMany(mappedBy="tenant")
	private Set<Catalog> catalog;

	@JsonIgnore
	@OneToMany(mappedBy="tenant")
	private Set<Vnf> vnf;

	@JsonIgnore
	@OneToMany(mappedBy="tenant")
	private Set<VnfInstance> vnfInstance;

	@JsonIgnore
	@OneToMany(mappedBy="tenant")
	private Set<TenantNetwork> tenantNetwork;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="TENANT_VIM")
	private Set<Vim> vim;

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

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getProjectid() {
		return projectid;
	}

	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}

	public Set<User> getUser() {
		return user;
	}

	public void setUser(Set<User> user) {
		this.user = user;
	}

	public Set<Catalog> getCatalog() {
		return catalog;
	}

	public void setCatalog(Set<Catalog> catalog) {
		this.catalog = catalog;
	}

	public Set<Vnf> getVnf() {
		return vnf;
	}

	public void setVnf(Set<Vnf> vnf) {
		this.vnf = vnf;
	}

	public Set<VnfInstance> getVnfInstance() {
		return vnfInstance;
	}

	public void setVnfInstance(Set<VnfInstance> vnfInstance) {
		this.vnfInstance = vnfInstance;
	}

	public String getProjectusername() {
		return projectusername;
	}

	public void setProjectusername(String projectusername) {
		this.projectusername = projectusername;
	}

	public String getProjectpassword() {
		return projectpassword;
	}

	public void setProjectpassword(String projectpassword) {
		this.projectpassword = projectpassword;
	}

//	public Integer getAllocatedip() {
//		return allocatedip;
//	}
//
//	public void setAllocatedip(Integer allocatedip) {
//		this.allocatedip = allocatedip;
//	}
//
//	public Integer getUsedip() {
//		return usedip;
//	}
//
//	public void setUsedip(Integer usedip) {
//		this.usedip = usedip;
//	}

	public Set<TenantNetwork> getTenantNetwork() {
		return tenantNetwork;
	}

	public void setTenantNetwork(Set<TenantNetwork> tenantNetwork) {
		this.tenantNetwork = tenantNetwork;
	}

	public Set<Vim> getVim() {
		return vim;
	}

	public void setVim(Set<Vim> vim) {
		this.vim = vim;
	}

	@Override
	public String toString() {
		return "Tenant [id=" + id + ", name=" + name + ", project=" + project + ", projectid=" + projectid
				+ ", projectusername=" + projectusername + ", projectpassword=" + projectpassword + ", user=" + user
				+ ", catalog=" + catalog + ", vnf=" + vnf + ", vnfInstance=" + vnfInstance + "]";
	}

	
	
}
