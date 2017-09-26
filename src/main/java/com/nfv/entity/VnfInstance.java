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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "vnfinstance")
public class VnfInstance {

	@Id()
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name", length = 512, nullable = false)
	private String name;

	@Column(name = "tackername", length = 512, nullable = false)
	private String tackername;

	@Column(name = "publicip", length = 512, nullable = false)
	private String publicip;

	@Column(name = "privateip", length = 512, nullable = false)
	private String privateip;

	@Column(name = "status", length = 512, nullable = false)
	private String status;

	@Column(name = "description", length = 512, nullable = true)
	private String description;

	@Column(name = "instanceid", length = 512, nullable = false)
	private String instanceid;

	@Column(name = "created", length = 512, nullable = false)
	private String created;

	@Column(name = "servicestatus", nullable = false)
	private Boolean servicestatus;

	@Column(name = "initstatus", nullable = false)
	private Long initstatus;

	@Column(name = "flavorid", length = 512, nullable = false)
	private String flavorid;

	@ManyToOne(optional=false)
	@JoinColumn(name="catalog_id", nullable=false)
	private Catalog catalog;

	@ManyToOne(optional=false)
	@JoinColumn(name="vnf_id", nullable=false)
	private Vnf vnf;

	@Column(name="elk_uri", length = 1024, nullable=true)
	private String elk_uri;

	@JsonIgnore
	@ManyToMany
	@JoinTable(name="VNFINSTANCE_USER")
	private Set<User> user;

	@ManyToOne(optional=false)
	@JoinColumn(name="tenant_id", nullable=false)
	private Tenant tenant;

	@ManyToOne(optional=true)
	@JoinColumn(name="vim_id", nullable=false)
	private Vim vim;

	public VnfInstance() {
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

	public String getTackername() {
		return tackername;
	}

	public void setTackername(String tackername) {
		this.tackername = tackername;
	}

	public String getPublicip() {
		return publicip;
	}

	public void setPublicip(String publicip) {
		this.publicip = publicip;
	}

	public String getPrivateip() {
		return privateip;
	}

	public void setPrivateip(String privateip) {
		this.privateip = privateip;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInstanceid() {
		return instanceid;
	}

	public void setInstanceid(String instanceid) {
		this.instanceid = instanceid;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public Boolean getServicestatus() {
		return servicestatus;
	}

	public void setServicestatus(Boolean servicestatus) {
		this.servicestatus = servicestatus;
	}

	public Long getInitstatus() {
		return initstatus;
	}

	public void setInitstatus(Long initstatus) {
		this.initstatus = initstatus;
	}

	public String getFlavorid() {
		return flavorid;
	}

	public void setFlavorid(String flavorid) {
		this.flavorid = flavorid;
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	public Vnf getVnf() {
		return vnf;
	}

	public void setVnf(Vnf vnf) {
		this.vnf = vnf;
	}

	public Set<User> getUser() {
		return user;
	}

	public void setUser(Set<User> user) {
		this.user = user;
	}

	public String getElk_uri() {
		return elk_uri;
	}

	public void setElk_uri(String elk_uri) {
		this.elk_uri = elk_uri;
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
