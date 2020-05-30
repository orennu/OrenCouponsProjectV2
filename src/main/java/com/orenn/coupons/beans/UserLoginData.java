package com.orenn.coupons.beans;

public class UserLoginData {
	
	private String userName;
	private String password;
	
	public UserLoginData(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	public UserLoginData() {
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

	@Override
	public String toString() {
		return "UserLoginData [userName=" + userName + ", password=" + password + "]";
	}
	
}
