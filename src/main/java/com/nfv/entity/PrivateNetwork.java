package com.nfv.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "privatenetwork")
public class PrivateNetwork {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "cidr", length = 512, nullable = false)
	private String cidr;

	@Column(name = "start", length = 512, nullable = false)
	private String start;

	@Column(name = "end", length = 512, nullable = false)
	private String end;

	@Column(name = "dns", length = 512, nullable = false)
	private String dns;

	@Column(name = "gateway", length = 512, nullable = false)
	private String gateway;
	
	@JsonIgnore
	@OneToMany(mappedBy="privateNetwork")
	private Set<TenantNetwork> tenantNetwork;
	
	public PrivateNetwork() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCidr() {
		return cidr;
	}

	public void setCidr(String cidr) {
		this.cidr = cidr;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getDns() {
		return dns;
	}

	public void setDns(String dns) {
		this.dns = dns;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public Set<TenantNetwork> getTenantNetwork() {
		return tenantNetwork;
	}

	public void setTenantNetwork(Set<TenantNetwork> tenantNetwork) {
		this.tenantNetwork = tenantNetwork;
	}

}
