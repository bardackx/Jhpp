package com.bardackx.jhpp.tests.tutorial;

public class Environment {

	private String url;
	private Account email, db;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Account getEmail() {
		return email;
	}

	public void setEmail(Account email) {
		this.email = email;
	}

	public Account getDb() {
		return db;
	}

	public void setDb(Account db) {
		this.db = db;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((db == null) ? 0 : db.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Environment other = (Environment) obj;
		if (db == null) {
			if (other.db != null) return false;
		} else if (!db.equals(other.db)) return false;
		if (email == null) {
			if (other.email != null) return false;
		} else if (!email.equals(other.email)) return false;
		if (url == null) {
			if (other.url != null) return false;
		} else if (!url.equals(other.url)) return false;
		return true;
	}

}
