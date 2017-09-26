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
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "vnf")
public class Vnf {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name", length = 512, nullable = false)
	private String name;

	@Column(name = "imageid", length = 512, nullable = false)
	private String imageid;

	@Column(name = "description", length = 512, nullable = true)
	private String description;

	@Column(name = "attributes", length = 32768, nullable = false)
	private String attributes;

	@JsonIgnore
	@ManyToMany(mappedBy="vnf")
	private Set<Catalog> catalog;

	@JsonIgnore
	@OneToMany(mappedBy="vnf")
	private Set<VnfInstance> vnfInstance;

	@ManyToOne(optional=false)
	@JoinColumn(name="tenant_id", nullable=false)
	private Tenant tenant;

	@ManyToOne(optional=true)
	@JoinColumn(name="vim_id", nullable=false)
	private Vim vim;

	public Vnf() {
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

	public String getImageid() {
		return imageid;
	}

	public void setImageid(String imageid) {
		this.imageid = imageid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
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

	public Vim getVim() {
		return vim;
	}

	public void setVim(Vim vim) {
		this.vim = vim;
	}

}
