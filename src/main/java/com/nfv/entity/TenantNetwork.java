package com.nfv.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "tenantnetwork")
public class TenantNetwork {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "network_name", length = 512, nullable = false)
	private String network_name;

	@Column(name = "subnet_name", length = 512, nullable = false)
	private String subnet_name;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="private_network_id", nullable=false)
	private PrivateNetwork privateNetwork;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="tenant_id", nullable=false)
	private Tenant tenant;

	@ManyToOne(optional=false)
	@JoinColumn(name="vim_id", nullable=false)
	private Vim vim;

	public TenantNetwork() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNetwork_name() {
		return network_name;
	}

	public void setNetwork_name(String network_name) {
		this.network_name = network_name;
	}

	public String getSubnet_name() {
		return subnet_name;
	}

	public void setSubnet_name(String subnet_name) {
		this.subnet_name = subnet_name;
	}

	public PrivateNetwork getPrivateNetwork() {
		return privateNetwork;
	}

	public void setPrivateNetwork(PrivateNetwork privateNetwork) {
		this.privateNetwork = privateNetwork;
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
