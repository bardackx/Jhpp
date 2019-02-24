package com.bardackx.jhpp.tests.exceptions;

public class NoSetter {

	private final String text;

	public NoSetter() {
		text = "default";
	}

	public NoSetter(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

}
