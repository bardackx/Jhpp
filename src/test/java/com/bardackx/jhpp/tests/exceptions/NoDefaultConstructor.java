package com.bardackx.jhpp.tests.exceptions;

public class NoDefaultConstructor {

	private String text;

	public NoDefaultConstructor(String text) {
		this.setText(text);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
