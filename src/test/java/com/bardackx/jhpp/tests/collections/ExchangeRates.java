package com.bardackx.jhpp.tests.collections;

import java.util.Map;

public class ExchangeRates {

	private String currency;
	private Map<String, Measure> measures;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Map<String, Measure> getMeasures() {
		return measures;
	}

	public void setMeasures(Map<String, Measure> measures) {
		this.measures = measures;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((measures == null) ? 0 : measures.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ExchangeRates other = (ExchangeRates) obj;
		if (currency == null) {
			if (other.currency != null) return false;
		} else if (!currency.equals(other.currency)) return false;
		if (measures == null) {
			if (other.measures != null) return false;
		} else if (!measures.equals(other.measures)) return false;
		return true;
	}

}
