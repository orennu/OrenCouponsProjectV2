package com.orenn.coupons.enums;

public enum CouponCategory {
	
	FOOD("Food"),
	TEXSTILE("Texstile"),
	TOURISM("Tourism"),
	ELECTRONICS("Electronics"),
	ENTERTAINMENT("Entertainment"),
	JEWLERY("Jewlery");
	
	private String name;
	
	private CouponCategory(String type) {
		this.name = type;
	}

	public String getName() {
		return name;
	}
	
}
