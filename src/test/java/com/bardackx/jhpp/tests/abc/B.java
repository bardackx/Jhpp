package com.bardackx.jhpp.tests.abc;

public class B {

	private C c;

	public C getC() {
		return c;
	}

	public void setC(C c) {
		this.c = c;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((c == null) ? 0 : c.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		B other = (B) obj;
		if (c == null) {
			if (other.c != null) return false;
		} else if (!c.equals(other.c)) return false;
		return true;
	}

}
