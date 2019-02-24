package com.bardackx.jhpp.tests.menu;

public class MenuItem {

	private String value, onclick;

	public MenuItem() {
	}

	public MenuItem(String value, String onclick) {
		super();
		this.value = value;
		this.onclick = onclick;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((onclick == null) ? 0 : onclick.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		MenuItem other = (MenuItem) obj;
		if (onclick == null) {
			if (other.onclick != null) return false;
		} else if (!onclick.equals(other.onclick)) return false;
		if (value == null) {
			if (other.value != null) return false;
		} else if (!value.equals(other.value)) return false;
		return true;
	}

}
