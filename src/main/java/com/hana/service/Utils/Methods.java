package com.hana.service.Utils;

import com.hana.service.Common.Const;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;

import java.sql.Statement;
import java.util.Base64;

public class Methods {
	static Statement statement;
	private static LogUtils logger = new LogUtils();

	public static String getTransactionInvoiceString() {
		String result = "";

		while (result.length() < 8) {
			int randomnumber = (int) (Math.random() * 1000 % (Const.aTOz.length));
			result += Const.aTOz[randomnumber];
		}
		return result;
	}

	public static String generateHS512Key() {
		byte[] encoded = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
		return Base64.getEncoder().encodeToString(encoded);
	}

	public static String getUserAccountBySecurityContextHolder(Authentication auth) {
		return auth.getName();
	}
}
