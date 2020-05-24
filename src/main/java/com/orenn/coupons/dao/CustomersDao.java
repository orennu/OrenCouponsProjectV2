package com.orenn.coupons.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.orenn.coupons.beans.Customer;
import com.orenn.coupons.beans.User;
import com.orenn.coupons.dao.interfaces.ICustomersDao;
import com.orenn.coupons.enums.ErrorType;
import com.orenn.coupons.enums.UserType;
import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.utils.DateUtils;
import com.orenn.coupons.utils.JdbcUtils;

@Repository
public class CustomersDao implements ICustomersDao {

	public void addCustomer(Customer customer, long customerId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "INSERT INTO customers (id, first_name, last_name, address, phone_number, date_of_birth) VALUES (?, ?, ?, ?, ?, ?)";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1,  customerId);
			preparedStatement.setString(2, customer.getFirstName());
			preparedStatement.setString(3, customer.getLastName());
			preparedStatement.setString(4, customer.getAddress());
			preparedStatement.setString(5, customer.getPhoneNumber());
			preparedStatement.setTimestamp(6, DateUtils.getSqlDateTime(customer.getDateOfBirth()));
			
			preparedStatement.executeUpdate();
			
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.CREATE_ERROR, 
					String.format("%s, customer %s", ErrorType.CREATE_ERROR.getErrorDescription(), customerId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	public boolean isCustomerExists(long customerId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT id FROM customers WHERE id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, customerId);
			
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				return true;
			}
			
			return false;
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, 
					String.format("%s, customer id %s", ErrorType.QUERY_ERROR.getErrorDescription(), customerId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public Customer getCustomerById(long customerId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT * FROM customers JOIN users ON customers.id = users.id WHERE customers.id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, customerId);
			
			resultSet = preparedStatement.executeQuery();
			
			return extractCustomersFromResultSet(resultSet);
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, 
					String.format("%s, user id %s", ErrorType.QUERY_ERROR.getErrorDescription(), customerId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public List<Customer> getAllCustomers() throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Customer customer = null;
		List<Customer> customersList = new ArrayList<Customer>();
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT email, first_name, last_name, phone_number FROM customers JOIN users ON customers.id = users.id";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				customer = extractCustomersFromResultSetSummarized(resultSet);
				customersList.add(customer);
			}
			
			return customersList;
			
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public List<Customer> getBirthDayCustomers(Date dateOfBirth) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Customer customer = null;
		List<Customer> customersList = new ArrayList<Customer>();
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT email, first_name, last_name, phone_number FROM customers JOIN users ON customers.id = users.id WHERE date_of_birth >= ? AND date_of_birth <= ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setTimestamp(1, DateUtils.getSqlDateTimeStartOfDay(dateOfBirth));
			preparedStatement.setTimestamp(2, DateUtils.getSqlDateTimeEndOfDay(dateOfBirth));
			
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				customer = extractCustomersFromResultSetSummarized(resultSet);
				customersList.add(customer);
			}
			
			return customersList;
			
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public void updateCustomer(Customer customer) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "UPDATE customers JOIN users ON users.id = customers.id SET password = ?, email = ?, first_name = ?, last_name = ?,"
					+ " address = ?, phone_number = ?, date_of_birth = ? WHERE customers.id = ?";

			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setString(1,  customer.getUser().getPassword());
			preparedStatement.setString(2,  customer.getUser().getEmail());
			preparedStatement.setString(3, customer.getFirstName());
			preparedStatement.setString(4, customer.getLastName());
			preparedStatement.setString(5, customer.getAddress());
			preparedStatement.setString(6, customer.getPhoneNumber());
			preparedStatement.setTimestamp(7, DateUtils.getSqlDateTime(customer.getDateOfBirth()));
			preparedStatement.setLong(8, customer.getUser().getId());

			preparedStatement.executeUpdate();

		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.UPDATE_ERROR, 
					String.format("%s, user id %s", ErrorType.UPDATE_ERROR.getErrorDescription(), customer.getUser().getId()));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	public void removeCustomer(long customerId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "DELETE customers, users FROM customers JOIN users ON customers.id = users.id WHERE customers.id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, customerId);
			
			preparedStatement.executeUpdate();
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.DELETE_ERROR, 
					String.format("%s, customer id %s", ErrorType.DELETE_ERROR.getErrorDescription(), customerId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	private Customer extractCustomersFromResultSet(ResultSet resultSet) throws SQLException {
		Customer customer = new Customer();
		User user = new User();
		user.setId(resultSet.getLong("users.id"));
		user.setUserName(resultSet.getString("username"));
		user.setPassword(resultSet.getString("password"));
		user.setEmail(resultSet.getString("email"));
		user.setCompanyId(resultSet.getLong("company_id"));
		String userTypeStr = resultSet.getString("type"); 
		UserType userType = UserType.valueOf(userTypeStr);
		user.setType(userType);
		user.setLockUser(resultSet.getBoolean("lock_user"));
		customer.setUser(user);
		customer.setFirstName(resultSet.getString("first_name"));
		customer.setLastName(resultSet.getString("last_name"));
		customer.setAddress(resultSet.getString("address"));
		customer.setPhoneNumber(resultSet.getString("phone_number"));
		customer.setDateOfBirth(new Date(resultSet.getTimestamp("date_of_birth").getTime()));
		
		return customer;
	}
	
	private Customer extractCustomersFromResultSetSummarized(ResultSet resultSet) throws SQLException {
		Customer customer = new Customer();
		User user = new User();
		user.setEmail(resultSet.getString("email"));
		customer.setUser(user);
		customer.setFirstName(resultSet.getString("first_name"));
		customer.setLastName(resultSet.getString("last_name"));
		customer.setPhoneNumber(resultSet.getString("phone_number"));
		
		return customer;
	}

}
