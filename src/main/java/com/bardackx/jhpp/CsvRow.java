package com.bardackx.jhpp;

class CsvRow {

	private StringBuilder builder = new StringBuilder();
	private boolean empty = true;

	void add(Object value) {
		if (!empty) builder.append(", ");
		else empty = false;
		String valueAsString = "" + value;
		if (valueAsString.contains(",")) valueAsString = '"' + valueAsString + '"';
		builder.append(valueAsString);
	}

	@Override
	public String toString() {
		return builder.toString();
	}
}
