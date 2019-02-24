package com.bardackx.jhpp.tests.menu;

import java.util.Arrays;

public class Popup {

	private MenuItem[] menuitem;

	public MenuItem[] getMenuitem() {
		return menuitem;
	}

	public void setMenuitem(MenuItem[] menuitem) {
		this.menuitem = menuitem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(menuitem);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Popup other = (Popup) obj;
		if (!Arrays.equals(menuitem, other.menuitem)) return false;
		return true;
	}

}
