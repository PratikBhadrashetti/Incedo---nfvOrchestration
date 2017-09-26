package com.nfv.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "vim")
public class Vim {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name", length = 512, nullable = false)
	private String name;

	@Column(name = "ipaddress", length = 512, nullable = false)
	private String ipaddress;

	@Column(name = "adminprojectid", length = 512, nullable = false)
	private String adminprojectid;

	@Column(name = "elkipaddress", length = 512, nullable = false)
	private String elkipaddress;

	@Column(name = "elkid", length = 512, nullable = false)
	private String elkid;

	@JsonIgnore
	@ManyToMany(mappedBy="vim")
	private Set<Tenant> tenant;

	@JsonIgnore
	@OneToMany(mappedBy="vim")
	private Set<TenantNetwork> tenantNetwork;
	
	@JsonIgnore
	@OneToMany(mappedBy="vim")
	private Set<Catalog> catalog;
	
	@JsonIgnore
	@OneToMany(mappedBy="vim")
	private Set<Vnf> vnf;
	
	@JsonIgnore
	@OneToMany(mappedBy="vim")
	private Set<VnfInstance> vnfInstance;
	
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

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getAdminprojectid() {
		return adminprojectid;
	}

	public void setAdminprojectid(String adminprojectid) {
		this.adminprojectid = adminprojectid;
	}

	public String getElkipaddress() {
		return elkipaddress;
	}

	public void setElkipaddress(String elkipaddress) {
		this.elkipaddress = elkipaddress;
	}

	public String getElkid() {
		return elkid;
	}

	public void setElkid(String elkid) {
		this.elkid = elkid;
	}

	public Set<Tenant> getTenant() {
		return tenant;
	}

	public void setTenant(Set<Tenant> tenant) {
		this.tenant = tenant;
	}

	public Set<TenantNetwork> getTenantNetwork() {
		return tenantNetwork;
	}

	public void setTenantNetwork(Set<TenantNetwork> tenantNetwork) {
		this.tenantNetwork = tenantNetwork;
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

}
