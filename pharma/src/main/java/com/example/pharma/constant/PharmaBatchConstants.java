package com.example.pharma.constant;

public class PharmaBatchConstants {
	// Error messages
	public static final String MANDATORY_FIELDS_MISSING = "Mandatory fields are missing.";
	public static final String MEDICINE_TYPE_CODE_NOT_EXIST = "Medicine type code does not exist.";
	public static final String MEDICINE_CODE_NOT_EXIST = "Medicine code does not exist.";
	public static final String BATCH_CODE_ALREADY_EXISTS = "Batch code already exists :";
	public static final String BATCH_WEIGHT_TOO_LOW = "Batch weight is less than the minimum required (100).";
	public static final String BATCH_CODE_INVALID_FORMAT = "Batch code is not in the correct format (BTC-)followed by 4 digits.";
	public static final String NO_SHIPPING_CHARGE_FOUND = "No shipping charge found for the given medicine type and weight range";
	public static final String BATCH_CODE_NOT_FOUND = "Batch code does not found ";

	// Error codes
	public static final int ERROR_CODE_MANDATORY_FIELDS = 500;
	public static final int ERROR_CODE_MEDICINE_TYPE_NOT_EXIST = 404;
	public static final int ERROR_CODE_MEDICINE_CODE_NOT_EXIST = 510;
	public static final int ERROR_CODE_BATCH_ALREADY_EXISTS = 511;
	public static final int ERROR_CODE_BATCH_WEIGHT_TOO_LOW = 512;
	public static final int ERROR_CODE_BATCH_CODE_INVALID_FORMAT = 513;
	public static final int ERROR_CODE_BATCH_CODE_NOT_FOUND = 404;
	public static final int ERROR_SHIPPING_CHARGE_NOT_FOUND = 404;
	// Weight Categories
	public static final String WEIGHT_CATEGORY_W1 = "W1";
	public static final String WEIGHT_CATEGORY_W2 = "W2";
	public static final String WEIGHT_CATEGORY_W3 = "W3";
	public static final String WEIGHT_CATEGORY_W4 = "W4";
	public static final String WEIGHT_CATEGORY_W5 = "W5";

	// Care levels
	public static final String CARE_LEVEL_NORMAL = "Normal";
	public static final String CARE_LEVEL_EXTREMELY_HIGH = "Extremely High";

}
