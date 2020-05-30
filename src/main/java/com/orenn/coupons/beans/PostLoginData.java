package com.orenn.coupons.beans;

import com.orenn.coupons.enums.UserType;

public class PostLoginData {
	
	private long id;
	private Long companyId;
	private UserType type;
	
	public PostLoginData(long id, Long companyId, UserType type) {
		this(id, type);
		this.companyId = companyId;
	}
	
	public PostLoginData(long id, UserType type) {
		this.id = id;
		this.companyId = null;
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public UserType getType() {
		return type;
	}
	
}
