package com.orenn.coupons.beans;

import com.orenn.coupons.enums.UserType;

public class SuccessfulLoginData {
	
	private String token;
	private UserType type;
	
	public SuccessfulLoginData(String token, UserType type) {
		this.token = token;
		this.type = type;
	}

	public String getToken() {
		return token;
	}

	public UserType getType() {
		return type;
	}
	
}
