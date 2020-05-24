package com.orenn.coupons.beans;
import com.orenn.coupons.enums.UserType;

public class User {
	
	private long id;
	private String userName;
	private String password;
	private String email;
	private Long companyId;
	private UserType type;
	private boolean lockUser;
	
	public User(long id, String userName, String password, String email, Long companyId, UserType type) {
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.companyId = companyId;
		this.type = type;
		this.lockUser = false;
	}

	public User(String userName, String password, String email, Long companyId, UserType type) {
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.companyId = companyId;
		this.type = type;
		this.lockUser = false;
	}
	
	public User() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public boolean isLockUser() {
		return lockUser;
	}

	public void setLockUser(boolean lockUser) {
		this.lockUser = lockUser;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", password=" + password + ", email=" + email
				+ ", companyId=" + companyId + ", type=" + type + ", lockUser=" + lockUser + "]";
	}

}
