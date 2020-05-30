package com.orenn.coupons.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.orenn.coupons.beans.PostLoginData;
import com.orenn.coupons.beans.SuccessfulLoginData;
import com.orenn.coupons.beans.User;
import com.orenn.coupons.beans.UserLoginData;
import com.orenn.coupons.dao.UsersDao;
import com.orenn.coupons.enums.ErrorType;
import com.orenn.coupons.enums.UserType;
import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.utils.StringUtils;
import com.orenn.coupons.utils.ValidationsUtils;

@Controller
public class UsersController {
	
	@Autowired
	private UsersDao usersDao;
	@Autowired
	private CompaniesController companiesController;
	@Autowired
	private CacheController cacheController;
	
	public UsersController() {
	}
	
	public SuccessfulLoginData login(UserLoginData userLoginData) throws ApplicationException {
		String hashedPassword = StringUtils.generateHashedPassword(userLoginData.getPassword());
		PostLoginData postLoginData = usersDao.login(userLoginData.getUserName(), hashedPassword);
		
		if (postLoginData == null) {
			throw new ApplicationException(ErrorType.LOGIN_FAILED, ErrorType.LOGIN_FAILED.getErrorDescription());
		}
		
		String token = StringUtils.generateToken();
		cacheController.put(token, postLoginData);
		
		return new SuccessfulLoginData(token, postLoginData.getType());
	}

	public long addUser(User user) throws ApplicationException {
		if (!isUserAttributesValid(user)) {
			throw new ApplicationException();
		}
		if (isUserExists(user.getUserName())) {
			throw new ApplicationException(ErrorType.ALREADY_EXISTS_ERROR, String.format("User %s", ErrorType.ALREADY_EXISTS_ERROR.getErrorDescription()));
		}
		String hashedPassword = StringUtils.generateHashedPassword(user.getPassword());
		user.setPassword(hashedPassword);
		
		return this.usersDao.addUser(user);
	}

	public boolean isUserExists(long userId) throws ApplicationException {
		return this.usersDao.isUserExists(userId);
	}
	
	public boolean isUserExists(String userName) throws ApplicationException {
		if (!ValidationsUtils.isValidUserName(userName)) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, String.format("%s, %s", ErrorType.INVALID_FORMAT_ERROR.getErrorDescription(), userName));
		}
		
		return this.usersDao.isUserExists(userName);
	}

	public User getUserById(long userId) throws ApplicationException {
		if (!isUserExists(userId)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, String.format("user id %s %s" ,userId ,ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		return this.usersDao.getUserById(userId);
	}
	
	public User getUserByUserName(String userName) throws ApplicationException {
		if (!isUserExists(userName)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, String.format("User %s %s", userName, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		return this.usersDao.getUserByUserName(userName);
	}
	
	public int getUsersCount() throws ApplicationException {
		return this.usersDao.getUsersCount();
	}
	
	public List<User> getAllUsers() throws ApplicationException {
		return this.usersDao.getAllUsers();
	}
	
	public List<User> getUsersByCompany(long companyId) throws ApplicationException {
		if (!this.companiesController.isCompanyExists(companyId)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR,
										String.format("company id %s %s", companyId, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		return this.usersDao.getUsersByCompany(companyId);
	}
	
	public List<User> getUsersByType(String userTypeStr) throws ApplicationException {
		if (userTypeStr == null) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s UserType", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		
		UserType userType = UserType.valueOf(userTypeStr);
		
		return this.usersDao.getUsersByType(userType);
	}
	
	public void updateUser(User user) throws ApplicationException {
		//TODO restrict what can be updated
		if (!isUserExists(user.getId())) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, String.format("user id %s %s" ,user.getId() ,ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		if (!isUserAttributesValid(user)) {
			throw new ApplicationException();
		}
		
		this.usersDao.updateUser(user);
	}
	
	public void lockUser(long userId, boolean lockUser) throws ApplicationException {
		if (!isUserExists(userId)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, String.format("user id %s %s" ,userId ,ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		this.usersDao.lockUser(userId, lockUser);
	}
	
	public void removeUser(long userId) throws ApplicationException {
		if (!isUserExists(userId)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, String.format("user id %s %s" ,userId ,ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		this.usersDao.removeUser(userId);
	}
	
	public void removeUsersByCompany(Long companyId) throws ApplicationException {
		if (companyId == null) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s companyId", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		if (!this.companiesController.isCompanyExists(companyId)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR,
										String.format("company id %s %s", companyId, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		this.usersDao.removeUsersByCompany(companyId);
	}
	
	private boolean isUserAttributesValid(User user) throws ApplicationException {
		if (user == null) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s User", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		if (!ValidationsUtils.isValidUserName(user.getUserName())) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, 
										String.format("%s, %s", ErrorType.INVALID_FORMAT_ERROR.getErrorDescription(), user.getUserName()));
		}
		if (!ValidationsUtils.isValidPassword(user.getPassword())) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, 
										String.format("%s, %s", ErrorType.INVALID_FORMAT_ERROR.getErrorDescription(), user.getPassword()));
		}
		if (!ValidationsUtils.isValidEmail(user.getEmail())) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, 
										String.format("%s, %s", ErrorType.INVALID_FORMAT_ERROR.getErrorDescription(), user.getEmail()));
		}
		if (user.getCompanyId() != null && !this.companiesController.isCompanyExists(user.getCompanyId())) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
										String.format("company id %s %s", user.getCompanyId(), ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		return true;
	}
	
}
