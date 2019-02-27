package com.bardackx.jhpp;

public class JhppException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public static enum Code {
		GETTER_IS_MISSING, SETTER_IS_MISSING, ELEMENT_KEY_IS_MISSING, DEFAULT_CONSTRUCTOR_IS_MISSING, UNEXPECTED
	}

	private final Code type;

	public JhppException(Code type, String message) {
		super(message);
		this.type = type;
	}

	public JhppException(Exception ex) {
		super(ex);
		this.type = Code.UNEXPECTED;
	}

	public Code getType() {
		return type;
	}

}
