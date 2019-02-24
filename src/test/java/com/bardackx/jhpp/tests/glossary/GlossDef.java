package com.bardackx.jhpp.tests.glossary;

import java.util.Arrays;

public class GlossDef {

	private String para;
	private String[] glossSeeAlso;

	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}

	public String[] getGlossSeeAlso() {
		return glossSeeAlso;
	}

	public void setGlossSeeAlso(String[] glossSeeAlso) {
		this.glossSeeAlso = glossSeeAlso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(glossSeeAlso);
		result = prime * result + ((para == null) ? 0 : para.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		GlossDef other = (GlossDef) obj;
		if (!Arrays.equals(glossSeeAlso, other.glossSeeAlso)) return false;
		if (para == null) {
			if (other.para != null) return false;
		} else if (!para.equals(other.para)) return false;
		return true;
	}

}
