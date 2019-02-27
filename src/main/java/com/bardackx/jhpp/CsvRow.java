package com.bardackx.jhpp;

class CsvRow {

	private StringBuilder builder = new StringBuilder();
	private boolean empty = true;

	void add(Object value) {
		if (!empty) builder.append(", ");
		else empty = false;
		builder.append(value);
	}

	@Override
	public String toString() {
		return builder.toString();
	}
}
