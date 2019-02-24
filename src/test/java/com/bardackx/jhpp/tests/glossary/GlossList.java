package com.bardackx.jhpp.tests.glossary;

public class GlossList {

	private GlossEntry glossEntry;

	public GlossEntry getGlossEntry() {
		return glossEntry;
	}

	public void setGlossEntry(GlossEntry glossEntry) {
		this.glossEntry = glossEntry;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((glossEntry == null) ? 0 : glossEntry.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		GlossList other = (GlossList) obj;
		if (glossEntry == null) {
			if (other.glossEntry != null) return false;
		} else if (!glossEntry.equals(other.glossEntry)) return false;
		return true;
	}

}
