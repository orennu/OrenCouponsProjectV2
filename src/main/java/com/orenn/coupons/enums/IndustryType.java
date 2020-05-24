package com.orenn.coupons.enums;

public enum IndustryType {
	
	FOOD("Food"),
	TEXSTILE("Texstile"),
	TOURISM("Tourism"),
	ELECTRONICS("Electronics"),
	JEWLERY("Jewlery");
	
	private String name;

	private IndustryType(String type) {
		this.name = type;
	}

	public String getName() {
		return name;
	}
	
}
