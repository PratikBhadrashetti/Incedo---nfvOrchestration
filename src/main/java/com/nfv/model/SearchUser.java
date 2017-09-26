package com.nfv.model;

import java.util.ArrayList;
import java.util.List;

public class SearchUser {

	private String jobCode = "";
	
	private String requesterId = "";
	
	private String lastName = "";
	
	private String firstName = "";
	
	private String principal = "";
	
	private String email = "";
	
	private List<String> roleIds;
	
	private List<String> groupIds = new ArrayList<>();
	
	private String userStatus = "";
	
	private List<String> organizationIds = new ArrayList<>();
	
	private String phoneCode = "";
	
	private String phoneNumber = "";
	
	private String accountStatus = "";
	
	private String attributeName = "";
	
	private String attributeElementId = "";
	
	private String attributeValue = "";
	
	private String employeeId = "";
	
	private String resourceId = "";
	
	private String employeeType = "";
	
	private String from = "1";
	
	private String size = "-1";
	
	private String targetUserId = "";
	
	private String maidenName = "";
	
	private Boolean fromDirectoryLookup = true;
	
	private String sortBy = "";
	
	private String orderBy = null;

	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	public String getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(String requesterId) {
		this.requesterId = requesterId;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<String> roleIds) {
		this.roleIds = roleIds;
	}

	public List<String> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(List<String> groupIds) {
		this.groupIds = groupIds;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public List<String> getOrganizationIds() {
		return organizationIds;
	}

	public void setOrganizationIds(List<String> organizationIds) {
		this.organizationIds = organizationIds;
	}

	public String getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeElementId() {
		return attributeElementId;
	}

	public void setAttributeElementId(String attributeElementId) {
		this.attributeElementId = attributeElementId;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getTargetUserId() {
		return targetUserId;
	}

	public void setTargetUserId(String targetUserId) {
		this.targetUserId = targetUserId;
	}

	public String getMaidenName() {
		return maidenName;
	}

	public void setMaidenName(String maidenName) {
		this.maidenName = maidenName;
	}

	public Boolean getFromDirectoryLookup() {
		return fromDirectoryLookup;
	}

	public void setFromDirectoryLookup(Boolean fromDirectoryLookup) {
		this.fromDirectoryLookup = fromDirectoryLookup;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
}
