package com.orenn.coupons.enums;

public enum UserType {
	
	ADMIN("Administrator"),
	CUSTOMER("Customer"),
	COMPANY("Company");
	
	private String name;

	private UserType(String type) {
		this.name = type;
	}

	public String getName() {
		return name;
	}
	
}
