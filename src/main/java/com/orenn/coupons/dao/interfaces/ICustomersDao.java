package com.orenn.coupons.dao.interfaces;

import java.util.Date;
import java.util.List;
import com.orenn.coupons.beans.Customer;
import com.orenn.coupons.exceptions.ApplicationException;

public interface ICustomersDao {
	
	public void addCustomer(Customer customer, long customerId) throws ApplicationException;
	
	public boolean isCustomerExists(long customerId) throws ApplicationException;
	
	public Customer getCustomerById(long customerId) throws ApplicationException;
	
	public List<Customer> getAllCustomers() throws ApplicationException;
	
	public List<Customer> getBirthDayCustomers(Date dateOfBirth) throws ApplicationException;
	
	public void updateCustomer(Customer customer) throws ApplicationException;
	
	public void removeCustomer(long customerId) throws ApplicationException;

}
