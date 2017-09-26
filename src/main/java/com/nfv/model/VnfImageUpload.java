package com.nfv.model;

public class VnfImageUpload {

	private String name;
	private String disk_format = "qcow2";
	private String container_format = "bare";
	private String visibility;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisk_format() {
		return disk_format;
	}
	public void setDisk_format(String disk_format) {
		this.disk_format = disk_format;
	}
	public String getContainer_format() {
		return container_format;
	}
	public void setContainer_format(String container_format) {
		this.container_format = container_format;
	}
	public String getVisibility() {
		return visibility;
	}
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
}
