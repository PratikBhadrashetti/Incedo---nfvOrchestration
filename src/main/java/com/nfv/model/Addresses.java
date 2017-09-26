package com.nfv.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;

public class Addresses {
	private Map<String, List<Extnet>> map = new HashMap<>();
	
	@JsonAnySetter
	public void setDynamicProperty(String name, List<Extnet> net) {
        map.put(name, net);
    }

	public Map<String, List<Extnet>> getMap() {
		return map;
	}

	public void setMap(Map<String, List<Extnet>> map) {
		this.map = map;
	}
}
