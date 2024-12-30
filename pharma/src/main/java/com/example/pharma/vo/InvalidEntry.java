package com.example.pharma.vo;

public class InvalidEntry {

	private String[] fields;
	private String errorMessage;

	public InvalidEntry(String[] fields, String errorMessage) {
		this.fields = fields;
		this.errorMessage = errorMessage;
	}

	public String[] getFields() {
		return fields;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}