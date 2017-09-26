package com.nfv.model;

public class VnfImage {

	private String name;
	private String disk_format;
	private String id;
	private Long size;
	
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
}
