package com.bardackx.jhpp.tests.glossary;

public class GlossDiv {

	private String title;
	private GlossList glossList;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public GlossList getGlossList() {
		return glossList;
	}

	public void setGlossList(GlossList glossList) {
		this.glossList = glossList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((glossList == null) ? 0 : glossList.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		GlossDiv other = (GlossDiv) obj;
		if (glossList == null) {
			if (other.glossList != null) return false;
		} else if (!glossList.equals(other.glossList)) return false;
		if (title == null) {
			if (other.title != null) return false;
		} else if (!title.equals(other.title)) return false;
		return true;
	}

}
