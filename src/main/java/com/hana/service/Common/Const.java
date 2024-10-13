package com.hana.service.Common;

public class Const {

	public static final String BACKEND_VERSION_CODE = "v0.0.0";

	public static final String REQUEST_ID = "requestId";
	public static final String USER_STATUS_ACTIVE = "Active";
	public static final String LOG_TYPE_OPERATION = "Operation";
	public static final String LOG_TYPE_SYSTEM = "System";

	public static final String STRING_LOGIN_TOKEN_KEY = "LoginTokenKey";

	public static String secretKeyForJWT;
	public static final String STRING_TRANSACTION_INVOICE = "transactionInvoice";

	public static final String STRING_DATE_TIME_UTC = "dateTimeUTC";
	public static final String[] aTOz = { "a", "b", "c", "d", "e", "f", "g",
			"h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
			"u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G",
			"H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z" };
	public static final String[] aTOz1TO0 = { "a", "b", "c", "d", "e", "f",
			"g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
			"t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F",
			"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9" };
	public static final String[] hexString = { "a", "b", "c", "d", "e", "f",
			"0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
	public static final String[] METHODS_NOT_NEED_AUTHORIZATION = {
			"/api/auth/login",

	};
	public static final String[] METHODS_NOT_NEED_AUTHORIZATION_SWAAGER = {
			"/swagger-ui",
			"/v3/api-docs",
			"/swagger-resources",
			"/webjars"
	};

}
