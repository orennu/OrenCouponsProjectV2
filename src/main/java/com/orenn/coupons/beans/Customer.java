package com.orenn.coupons.beans;

import java.util.Date;

public class Customer {
	
	private User user;
	private String firstName;
	private String lastName;
	private String address;
	private String phoneNumber;
	private Date dateOfBirth;
	
	public Customer(User user, String firstName, String lastName, String address,
			String phoneNumber, Date dateOfBirth) {
		this.user = user;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.dateOfBirth = dateOfBirth;
	}

	public Customer() {
	}

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}
	
	@Override
	public String toString() {
		return "Customer [id=" + user.getId() + ", user=" + user + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", address=" + address + ", phoneNumber=" + phoneNumber + ", dateOfBirth=" + dateOfBirth + "]";
	}
	
}
