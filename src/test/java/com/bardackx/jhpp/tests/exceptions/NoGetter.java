package com.bardackx.jhpp.tests.exceptions;

public class NoGetter {

	private String text;

	public NoGetter() {
		text = "default";
	}

	public NoGetter(String text) {
		super();
		this.text = text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}
