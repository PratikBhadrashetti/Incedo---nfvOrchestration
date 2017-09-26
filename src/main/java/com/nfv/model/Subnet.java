package com.nfv.model;

import java.util.List;

public class Subnet {

	private String network_id;
	
	private Integer ip_version = 4;
	
	private String cidr;
	
	private List<AllocationPools> allocation_pools;
	
	private List<String> dns_nameservers;
	
	private String gateway_ip;
	
	private Boolean enable_dhcp = true;
	
	private String name;

	public String getNetwork_id() {
		return network_id;
	}

	public void setNetwork_id(String network_id) {
		this.network_id = network_id;
	}

	public Integer getIp_version() {
		return ip_version;
	}

	public void setIp_version(Integer ip_version) {
		this.ip_version = ip_version;
	}

	public String getCidr() {
		return cidr;
	}

	public void setCidr(String cidr) {
		this.cidr = cidr;
	}

	public List<AllocationPools> getAllocation_pools() {
		return allocation_pools;
	}

	public void setAllocation_pools(List<AllocationPools> allocation_pools) {
		this.allocation_pools = allocation_pools;
	}

	public List<String> getDns_nameservers() {
		return dns_nameservers;
	}

	public void setDns_nameservers(List<String> dns_nameservers) {
		this.dns_nameservers = dns_nameservers;
	}

	public String getGateway_ip() {
		return gateway_ip;
	}

	public void setGateway_ip(String gateway_ip) {
		this.gateway_ip = gateway_ip;
	}

	public Boolean getEnable_dhcp() {
		return enable_dhcp;
	}

	public void setEnable_dhcp(Boolean enable_dhcp) {
		this.enable_dhcp = enable_dhcp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
