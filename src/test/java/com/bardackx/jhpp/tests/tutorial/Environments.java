package com.bardackx.jhpp.tests.tutorial;

public class Environments {

	private Environment pro, qas;

	public Environment getPro() {
		return pro;
	}

	public void setPro(Environment pro) {
		this.pro = pro;
	}

	public Environment getQas() {
		return qas;
	}

	public void setQas(Environment qas) {
		this.qas = qas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pro == null) ? 0 : pro.hashCode());
		result = prime * result + ((qas == null) ? 0 : qas.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Environments other = (Environments) obj;
		if (pro == null) {
			if (other.pro != null) return false;
		} else if (!pro.equals(other.pro)) return false;
		if (qas == null) {
			if (other.qas != null) return false;
		} else if (!qas.equals(other.qas)) return false;
		return true;
	}

}
