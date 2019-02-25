package com.bardackx.jhpp.tests.collections;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListSetMapArray {

	private List<Measure> list;
	private Set<Measure> set;
	private Map<String, Measure> map;
	private Measure[] array;

	public ListSetMapArray() {
		// TODO Auto-generated constructor stub
	}

	public ListSetMapArray(List<Measure> list, Set<Measure> set, Map<String, Measure> map, Measure[] array) {
		super();
		this.list = list;
		this.set = set;
		this.map = map;
		this.array = array;
	}

	public List<Measure> getList() {
		return list;
	}

	public void setList(List<Measure> list) {
		this.list = list;
	}

	public Set<Measure> getSet() {
		return set;
	}

	public void setSet(Set<Measure> set) {
		this.set = set;
	}

	public Map<String, Measure> getMap() {
		return map;
	}

	public void setMap(Map<String, Measure> map) {
		this.map = map;
	}

	public Measure[] getArray() {
		return array;
	}

	public void setArray(Measure[] array) {
		this.array = array;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(array);
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		result = prime * result + ((set == null) ? 0 : set.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ListSetMapArray other = (ListSetMapArray) obj;
		if (!Arrays.equals(array, other.array)) return false;
		if (list == null) {
			if (other.list != null) return false;
		} else if (!list.equals(other.list)) return false;
		if (map == null) {
			if (other.map != null) return false;
		} else if (!map.equals(other.map)) return false;
		if (set == null) {
			if (other.set != null) return false;
		} else if (!set.equals(other.set)) return false;
		return true;
	}

}
