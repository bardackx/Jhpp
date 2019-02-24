package com.bardackx.jhpp.tests.glossary;

public class Glossary {

	private String title;
	private GlossDiv glossDiv;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public GlossDiv getGlossDiv() {
		return glossDiv;
	}

	public void setGlossDiv(GlossDiv glossDiv) {
		this.glossDiv = glossDiv;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((glossDiv == null) ? 0 : glossDiv.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Glossary other = (Glossary) obj;
		if (glossDiv == null) {
			if (other.glossDiv != null) return false;
		} else if (!glossDiv.equals(other.glossDiv)) return false;
		if (title == null) {
			if (other.title != null) return false;
		} else if (!title.equals(other.title)) return false;
		return true;
	}

}
