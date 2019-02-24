package com.bardackx.jhpp.tests.glossary;

public class GlossEntry {

	private String ID;
	private String sortAs;
	private String glossTerm;
	private String acronym;
	private String abbrev;
	private GlossDef glossDef;
	private String glossSee;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getSortAs() {
		return sortAs;
	}

	public void setSortAs(String sortAs) {
		this.sortAs = sortAs;
	}

	public String getGlossTerm() {
		return glossTerm;
	}

	public void setGlossTerm(String glossTerm) {
		this.glossTerm = glossTerm;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getAbbrev() {
		return abbrev;
	}

	public void setAbbrev(String abbrev) {
		this.abbrev = abbrev;
	}

	public GlossDef getGlossDef() {
		return glossDef;
	}

	public void setGlossDef(GlossDef glossDef) {
		this.glossDef = glossDef;
	}

	public String getGlossSee() {
		return glossSee;
	}

	public void setGlossSee(String glossSee) {
		this.glossSee = glossSee;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		result = prime * result + ((abbrev == null) ? 0 : abbrev.hashCode());
		result = prime * result + ((acronym == null) ? 0 : acronym.hashCode());
		result = prime * result + ((glossDef == null) ? 0 : glossDef.hashCode());
		result = prime * result + ((glossSee == null) ? 0 : glossSee.hashCode());
		result = prime * result + ((glossTerm == null) ? 0 : glossTerm.hashCode());
		result = prime * result + ((sortAs == null) ? 0 : sortAs.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		GlossEntry other = (GlossEntry) obj;
		if (ID == null) {
			if (other.ID != null) return false;
		} else if (!ID.equals(other.ID)) return false;
		if (abbrev == null) {
			if (other.abbrev != null) return false;
		} else if (!abbrev.equals(other.abbrev)) return false;
		if (acronym == null) {
			if (other.acronym != null) return false;
		} else if (!acronym.equals(other.acronym)) return false;
		if (glossDef == null) {
			if (other.glossDef != null) return false;
		} else if (!glossDef.equals(other.glossDef)) return false;
		if (glossSee == null) {
			if (other.glossSee != null) return false;
		} else if (!glossSee.equals(other.glossSee)) return false;
		if (glossTerm == null) {
			if (other.glossTerm != null) return false;
		} else if (!glossTerm.equals(other.glossTerm)) return false;
		if (sortAs == null) {
			if (other.sortAs != null) return false;
		} else if (!sortAs.equals(other.sortAs)) return false;
		return true;
	}
}
