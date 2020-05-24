package com.orenn.coupons.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.orenn.coupons.beans.User;
import com.orenn.coupons.dao.interfaces.IUsersDao;
import com.orenn.coupons.enums.ErrorType;
import com.orenn.coupons.enums.UserType;
import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.utils.JdbcUtils;

@Repository
public class UsersDao implements IUsersDao {
	
	public long addUser(User user) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "INSERT INTO users (username, password, email, company_id, type, lock_user) VALUES (?, ?, ?, ?, ?, ?)";
			
			preparedStatement = connection.prepareStatement(sqlStatement, PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1,  user.getUserName());
			preparedStatement.setString(2,  user.getPassword());
			preparedStatement.setString(3,  user.getEmail());
			if (user.getCompanyId() == null) {
				preparedStatement.setNull(4, Types.LONGVARCHAR);
			}
			else {
				preparedStatement.setLong(4, user.getCompanyId() * 1L);
			}
			preparedStatement.setString(5, user.getType().name());
			preparedStatement.setBoolean(6, user.isLockUser());
			
			preparedStatement.executeUpdate();
			
			resultSet = preparedStatement.getGeneratedKeys();
			
			if (!resultSet.next()) {
				throw new ApplicationException(ErrorType.CREATE_ERROR, 
						String.format("%s, user %s", ErrorType.CREATE_ERROR.getErrorDescription(), user.getUserName()));
			}
			
			return resultSet.getLong(1);
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.CREATE_ERROR, 
					String.format("%s, user %s", ErrorType.CREATE_ERROR.getErrorDescription(), user.getUserName()));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public boolean isUserExists(long userId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT id FROM users WHERE id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, userId);
			
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				return true;
			}
			
			return false;
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, 
					String.format("%s, user id %s", ErrorType.QUERY_ERROR.getErrorDescription(), userId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public boolean isUserExists(String userName) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT id FROM users WHERE username = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setString(1, userName);
			
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				return true;
			}
			
			return false;
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, 
					String.format("%s, user %s", ErrorType.QUERY_ERROR.getErrorDescription(), userName));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public User getUserById(long userId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT * FROM users WHERE id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, userId);
			
			resultSet = preparedStatement.executeQuery();
			
			return extractUserFromResultSet(resultSet);
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, 
					String.format("%s, user id %s", ErrorType.QUERY_ERROR.getErrorDescription(), userId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public User getUserByUserName(String userName) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT * from users WHERE username = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setString(1, userName);
			
			resultSet = preparedStatement.executeQuery();
			
			return extractUserFromResultSet(resultSet);
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, 
					String.format("%s, user %s", ErrorType.QUERY_ERROR.getErrorDescription(), userName));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public int getUsersCount() throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT COUNT(id) from users";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
			return 0;
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public List<User> getAllUsers() throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		User user = null;
		List<User> usersList = new ArrayList<User>();
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT username, email, type FROM users";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				user = extractUserFromResultSetSummarized(resultSet);
				usersList.add(user);
			}
			
			return usersList;
			
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public List<User> getUsersByCompany(long companyId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		User user = null;
		List<User> usersList = new ArrayList<User>();
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT username, email, type FROM users WHERE company_id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, companyId);
			
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				user = extractUserFromResultSetSummarized(resultSet);
				usersList.add(user);
			}
			
			return usersList;
			
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public List<User> getUsersByType(UserType userType) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		User user = null;
		List<User> usersList = new ArrayList<User>();
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT username, email, type FROM users WHERE type = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setString(1, userType.name());
			
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				user = extractUserFromResultSetSummarized(resultSet);
				usersList.add(user);
			}
			
			return usersList;
			
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public void updateUser(User user) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "UPDATE users SET password = ?, email = ? WHERE id = ?";

			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setString(1,  user.getPassword());
			preparedStatement.setString(2,  user.getEmail());
			preparedStatement.setLong(3, user.getId());

			preparedStatement.executeUpdate();

		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.UPDATE_ERROR, 
					String.format("%s, user id %s", ErrorType.UPDATE_ERROR.getErrorDescription(), user.getId()));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	public void lockUser(long userId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "UPDATE users SET lock_user = ? WHERE id = ?";

			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setBoolean(1, true);
			preparedStatement.setLong(2, userId);

			preparedStatement.executeUpdate();

		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.UPDATE_ERROR, 
					String.format("%s, user id %s", ErrorType.UPDATE_ERROR.getErrorDescription(), userId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	public void removeUser(long userId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "DELETE FROM users WHERE id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, userId);
			
			preparedStatement.executeUpdate();
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.DELETE_ERROR, 
					String.format("%s, user id %s", ErrorType.DELETE_ERROR.getErrorDescription(), userId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	public void removeUsersByCompany(long companyId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "DELETE FROM users WHERE company_id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, companyId);
			
			preparedStatement.executeUpdate();
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.DELETE_ERROR, 
					String.format("%s, company id %s", ErrorType.DELETE_ERROR.getErrorDescription(), companyId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
		User user = new User();
		user.setId(resultSet.getLong("id"));
		user.setUserName(resultSet.getString("username"));
		user.setPassword(resultSet.getString("password"));
		user.setEmail(resultSet.getString("email"));
		user.setCompanyId(resultSet.getLong("company_id"));
		String userTypeStr = resultSet.getString("type"); 
		UserType userType = UserType.valueOf(userTypeStr);
		user.setType(userType);
		user.setLockUser(resultSet.getBoolean("lock_user"));
		
		return user;
	}
	
	private User extractUserFromResultSetSummarized(ResultSet resultSet) throws SQLException {
		User user = new User();
		user.setUserName(resultSet.getString("username"));
		user.setEmail(resultSet.getString("email"));
		String userTypeStr = resultSet.getString("type"); 
		UserType userType = UserType.valueOf(userTypeStr);
		user.setType(userType);
		
		return user;
	}

}
