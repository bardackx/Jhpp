package com.bardackx.jhpp.tests.abc;

public class ABC {

	private A a1;
	private A a2;
	private A a3;
	private A a4;

	public A getA1() {
		return a1;
	}

	public void setA1(A a1) {
		this.a1 = a1;
	}

	public A getA2() {
		return a2;
	}

	public void setA2(A a2) {
		this.a2 = a2;
	}

	public A getA3() {
		return a3;
	}

	public void setA3(A a3) {
		this.a3 = a3;
	}

	public A getA4() {
		return a4;
	}

	public void setA4(A a4) {
		this.a4 = a4;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a1 == null) ? 0 : a1.hashCode());
		result = prime * result + ((a2 == null) ? 0 : a2.hashCode());
		result = prime * result + ((a3 == null) ? 0 : a3.hashCode());
		result = prime * result + ((a4 == null) ? 0 : a4.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ABC other = (ABC) obj;
		if (a1 == null) {
			if (other.a1 != null) return false;
		} else if (!a1.equals(other.a1)) return false;
		if (a2 == null) {
			if (other.a2 != null) return false;
		} else if (!a2.equals(other.a2)) return false;
		if (a3 == null) {
			if (other.a3 != null) return false;
		} else if (!a3.equals(other.a3)) return false;
		if (a4 == null) {
			if (other.a4 != null) return false;
		} else if (!a4.equals(other.a4)) return false;
		return true;
	}

}
