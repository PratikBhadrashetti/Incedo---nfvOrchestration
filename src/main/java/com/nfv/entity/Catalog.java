package com.nfv.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "catalog")
public class Catalog {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name", length = 512, nullable = false)
	private String name;

	@Column(name = "description", length = 4096, nullable = true)
	private String description;

	@JsonIgnore
	@OneToOne
	private CatalogLogo catalogLogo;
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(name="CATALOG_USER")
	private Set<User> user;

	@ManyToOne(optional=true)
	@JoinColumn(name="tenant_id", nullable=true)
	private Tenant tenant;

	@JsonIgnore
	@ManyToMany
	@JoinTable(name="CATALOG_VNF")
	private Set<Vnf> vnf;
	
	@JsonIgnore
	@OneToMany(mappedBy="catalog")
	private Set<VnfInstance> vnfInstance;

	@ManyToOne(optional=true)
	@JoinColumn(name="vim_id", nullable=false)
	private Vim vim;

	public Catalog() {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CatalogLogo getCatalogLogo() {
		return catalogLogo;
	}

	public void setCatalogLogo(CatalogLogo catalogLogo) {
		this.catalogLogo = catalogLogo;
	}

	public Set<User> getUser() {
		return user;
	}

	public void setUser(Set<User> user) {
		this.user = user;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
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

	public Vim getVim() {
		return vim;
	}

	public void setVim(Vim vim) {
		this.vim = vim;
	}

}
