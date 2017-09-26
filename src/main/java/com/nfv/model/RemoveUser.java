/**
 * 
 */
package com.nfv.model;

import java.util.Date;

/**
 * @author rohit.patel
 *
 */
public class RemoveUser {
	
	private String userId;
	
	private String performDataString = new Date(System.currentTimeMillis()).toString();
	
	private boolean performNow = true;
	
	private String description = "";

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPerformDataString() {
		return performDataString;
	}

	public void setPerformDataString(String performDataString) {
		this.performDataString = performDataString;
	}

	public boolean isPerformNow() {
		return performNow;
	}

	public void setPerformNow(boolean performNow) {
		this.performNow = performNow;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "RemoveUser [userId=" + userId + ", performDataString=" + performDataString + ", performNow="
				+ performNow + ", description=" + description + "]";
	}
	
	

}
