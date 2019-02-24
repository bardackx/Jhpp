package com.bardackx.jhpp.tests.collections;

public class Measure {

	private double ammount; // I know, it should be BigDecimal, but is not about this yet
	private String source;

	public Measure() {
	}

	public Measure(double ammount, String source) {
		super();
		this.ammount = ammount;
		this.source = source;
	}

	public double getAmmount() {
		return ammount;
	}

	public void setAmmount(double ammount) {
		this.ammount = ammount;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(ammount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Measure other = (Measure) obj;
		if (Double.doubleToLongBits(ammount) != Double.doubleToLongBits(other.ammount)) return false;
		if (source == null) {
			if (other.source != null) return false;
		} else if (!source.equals(other.source)) return false;
		return true;
	}

}
