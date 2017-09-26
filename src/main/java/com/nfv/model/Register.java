package com.nfv.model;

import java.util.Arrays;

public class Register {
	
	private String id = null;

	private String firstName;
	
	private String middleInit = "";
	
	private String lastName;
	
	private String nickname = "";
	
	private String maidenName = "";
	
	private String suffix = "";
	
	private String sex = "";
	
	private String prefixLastName = "";
	
	private String partnerName = "";
	
	private String prefixPartnerName = "";
	
	private String prefix = "";
	
	private String title;
	
	private String jobCodeId = "";
	
	private String classification = "";
	
	private String employeeId = "";
	
	private String userTypeInd = "";
	
	private String userSubTypeId = "FULL_TIME";
	
	private String employeeTypeId = "";
	
	private String supervisorId = "";
	
	private String supervisorName = "";
	
	private String alternateContactId = "";
	
	private String alternateContactName = "";
	
	private String login;
	
	private String[] organizationIds = new String[]{};
	
	private String roleId;
	
	private String groupId = null;
	
	private String metadataTypeId = "DEFAULT_USER";
	
	private boolean notifyUserViaEmail = false;

	private boolean notifySupervisorViaEmail = false;
	
	private boolean provisionOnStartDate = false;
	
	private String password;
	
	private Email email;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleInit() {
		return middleInit;
	}

	public void setMiddleInit(String middleInit) {
		this.middleInit = middleInit;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getMaidenName() {
		return maidenName;
	}

	public void setMaidenName(String maidenName) {
		this.maidenName = maidenName;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPrefixLastName() {
		return prefixLastName;
	}

	public void setPrefixLastName(String prefixLastName) {
		this.prefixLastName = prefixLastName;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public String getPrefixPartnerName() {
		return prefixPartnerName;
	}

	public void setPrefixPartnerName(String prefixPartnerName) {
		this.prefixPartnerName = prefixPartnerName;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getJobCodeId() {
		return jobCodeId;
	}

	public void setJobCodeId(String jobCodeId) {
		this.jobCodeId = jobCodeId;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getUserTypeInd() {
		return userTypeInd;
	}

	public void setUserTypeInd(String userTypeInd) {
		this.userTypeInd = userTypeInd;
	}

	public String getUserSubTypeId() {
		return userSubTypeId;
	}

	public void setUserSubTypeId(String userSubTypeId) {
		this.userSubTypeId = userSubTypeId;
	}

	public String getEmployeeTypeId() {
		return employeeTypeId;
	}

	public void setEmployeeTypeId(String employeeTypeId) {
		this.employeeTypeId = employeeTypeId;
	}

	public String getSupervisorId() {
		return supervisorId;
	}

	public void setSupervisorId(String supervisorId) {
		this.supervisorId = supervisorId;
	}

	public String getSupervisorName() {
		return supervisorName;
	}

	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}

	public String getAlternateContactId() {
		return alternateContactId;
	}

	public void setAlternateContactId(String alternateContactId) {
		this.alternateContactId = alternateContactId;
	}

	public String getAlternateContactName() {
		return alternateContactName;
	}

	public void setAlternateContactName(String alternateContactName) {
		this.alternateContactName = alternateContactName;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String[] getOrganizationIds() {
		return organizationIds;
	}

	public void setOrganizationIds(String[] organizationIds) {
		this.organizationIds = organizationIds;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getMetadataTypeId() {
		return metadataTypeId;
	}

	public void setMetadataTypeId(String metadataTypeId) {
		this.metadataTypeId = metadataTypeId;
	}

	public boolean isNotifyUserViaEmail() {
		return notifyUserViaEmail;
	}

	public void setNotifyUserViaEmail(boolean notifyUserViaEmail) {
		this.notifyUserViaEmail = notifyUserViaEmail;
	}

	public boolean isNotifySupervisorViaEmail() {
		return notifySupervisorViaEmail;
	}

	public void setNotifySupervisorViaEmail(boolean notifySupervisorViaEmail) {
		this.notifySupervisorViaEmail = notifySupervisorViaEmail;
	}

	public boolean isProvisionOnStartDate() {
		return provisionOnStartDate;
	}

	public void setProvisionOnStartDate(boolean provisionOnStartDate) {
		this.provisionOnStartDate = provisionOnStartDate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Register [id=" + id + ", firstName=" + firstName + ", middleInit=" + middleInit + ", lastName="
				+ lastName + ", nickname=" + nickname + ", maidenName=" + maidenName + ", suffix=" + suffix + ", sex="
				+ sex + ", prefixLastName=" + prefixLastName + ", partnerName=" + partnerName + ", prefixPartnerName="
				+ prefixPartnerName + ", prefix=" + prefix + ", title=" + title + ", jobCodeId=" + jobCodeId
				+ ", classification=" + classification + ", employeeId=" + employeeId + ", userTypeInd=" + userTypeInd
				+ ", userSubTypeId=" + userSubTypeId + ", employeeTypeId=" + employeeTypeId + ", supervisorId="
				+ supervisorId + ", supervisorName=" + supervisorName + ", alternateContactId=" + alternateContactId
				+ ", alternateContactName=" + alternateContactName + ", login=" + login + ", organizationIds="
				+ Arrays.toString(organizationIds) + ", roleId=" + roleId + ", groupId=" + groupId + ", metadataTypeId="
				+ metadataTypeId + ", notifyUserViaEmail=" + notifyUserViaEmail + ", notifySupervisorViaEmail="
				+ notifySupervisorViaEmail + ", provisionOnStartDate=" + provisionOnStartDate + ", password=" + password
				+ ", email=" + email + "]";
	}

	
}
