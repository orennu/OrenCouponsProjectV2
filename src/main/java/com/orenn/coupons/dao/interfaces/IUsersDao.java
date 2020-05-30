package com.orenn.coupons.dao.interfaces;

import java.util.List;

import com.orenn.coupons.beans.PostLoginData;
import com.orenn.coupons.beans.User;
import com.orenn.coupons.enums.UserType;
import com.orenn.coupons.exceptions.ApplicationException;

public interface IUsersDao {
	
	public PostLoginData login(String useruserName, String password) throws ApplicationException;
	
	public long addUser(User user) throws ApplicationException;
	
	public boolean isUserExists(long userId) throws ApplicationException;
	
	public boolean isUserExists(String userName) throws ApplicationException;
	
	public User getUserById(long userId) throws ApplicationException;
	
	public User getUserByUserName(String userName) throws ApplicationException;
	
	public int getUsersCount() throws ApplicationException;
	
	public List<User> getAllUsers() throws ApplicationException;
	
	public List<User> getUsersByCompany(long companyId) throws ApplicationException;
	
	public List<User> getUsersByType(UserType userType) throws ApplicationException;
	
	public void updateUser(User user) throws ApplicationException;
	
	public void lockUser(long userId, boolean lockUser) throws ApplicationException;
	
	public void removeUser(long userId) throws ApplicationException;
	
	public void removeUsersByCompany(Long companyId) throws ApplicationException;

}
