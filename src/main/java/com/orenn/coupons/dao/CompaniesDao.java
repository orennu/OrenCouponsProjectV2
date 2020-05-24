package com.orenn.coupons.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.orenn.coupons.beans.Company;
import com.orenn.coupons.dao.interfaces.ICompaniesDao;
import com.orenn.coupons.enums.ErrorType;
import com.orenn.coupons.enums.IndustryType;
import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.utils.JdbcUtils;

@Repository
public class CompaniesDao implements ICompaniesDao {
	
	public long addCompany(Company company) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "INSERT INTO companies (name, email, phone_number, address, industry_type) VALUES (?, ?, ?, ?, ?)";
			
			preparedStatement = connection.prepareStatement(sqlStatement, PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, company.getName());
			preparedStatement.setString(2, company.getEmail());
			preparedStatement.setString(3, company.getPhoneNumber());
			preparedStatement.setString(4, company.getAddress());
			preparedStatement.setString(5, company.getIndustry().name());
			
			preparedStatement.executeUpdate();
			
			resultSet = preparedStatement.getGeneratedKeys();
			
			if (!resultSet.next()) {
				throw new ApplicationException(ErrorType.CREATE_ERROR, 
											String.format("%s, company name %s", ErrorType.CREATE_ERROR.getErrorDescription(), company.getName()));
			}
			
			return resultSet.getLong(1);
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.CREATE_ERROR, 
										String.format("%s, company name %s", ErrorType.CREATE_ERROR.getErrorDescription(), company.getName()));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public boolean isCompanyExists(Long companyId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT id FROM companies WHERE id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, companyId);
			
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				return true;
			}
			
			return false;
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, String.format("%s, company id %s", ErrorType.QUERY_ERROR.getErrorDescription(), companyId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	public boolean isCompanyExists(String companyName) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT id FROM companies WHERE name = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setString(1, companyName);
			
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				return true;
			}
			
			return false;
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, String.format("%s, company name %s", ErrorType.QUERY_ERROR.getErrorDescription(), companyName));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public boolean isCompanyEmailExists(String companyEmail) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT email FROM companies WHERE email = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setString(1, companyEmail);
			
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				return true;
			}
			
			return false;
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, String.format("%s, company email %s", ErrorType.QUERY_ERROR.getErrorDescription(), companyEmail));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public boolean isCompanyPhoneNumberExists(String companyPhoneNumber) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT phone_number FROM companies WHERE phone_number = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setString(1, companyPhoneNumber);
			
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				return true;
			}
			
			return false;
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, 
										String.format("%s, company phone number %s", ErrorType.QUERY_ERROR.getErrorDescription(), companyPhoneNumber));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public Company getCompanyById(long companyId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT * FROM companies WHERE id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, companyId);
			
			resultSet = preparedStatement.executeQuery();
			
			if (!resultSet.next()) {
				return null;
			}
			
			return extractCompanyFromResultSet(resultSet);
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, String.format("%s, company id %s", ErrorType.QUERY_ERROR.getErrorDescription(), companyId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public List<Company> getAllCompanies() throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Company company = null;
		List<Company> companiesList = new ArrayList<Company>();
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT id, name, phone_number FROM companies";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				company = extractCompanyFromResultSetSummarized(resultSet);
				companiesList.add(company);
			}
			
			return companiesList;
			
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public List<Company> getCompaniesByType(IndustryType industry) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Company company = null;
		List<Company> companiesList = new ArrayList<Company>();
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT id, name, phone_number FROM companies WHERE industry_type = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setString(1, industry.name());
			
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				company = extractCompanyFromResultSetSummarized(resultSet);
				companiesList.add(company);
			}
			
			return companiesList;
			
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public void updateCompany(Company company) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "UPDATE companies SET name = ?, email = ?, phone_number = ?, address = ?, industry_type = ? WHERE id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setString(1, company.getName());
			preparedStatement.setString(2, company.getEmail());
			preparedStatement.setString(3, company.getPhoneNumber());
			preparedStatement.setString(4, company.getAddress());
			preparedStatement.setString(5, company.getIndustry().name());
			preparedStatement.setLong(6, company.getId());
			
			preparedStatement.executeUpdate();
			
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.UPDATE_ERROR, 
										String.format("%s, company id %s", ErrorType.UPDATE_ERROR.getErrorDescription(), company.getId()));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	public void removeCompany(long companyId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "DELETE companies, coupons FROM companies JOIN coupons ON companies.id = coupons.company_id WHERE companies.id = ?; DELETE FROM companies WHERE id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, companyId);
			preparedStatement.setLong(2, companyId);
			
			preparedStatement.executeUpdate();
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.DELETE_ERROR, String.format("%s, company id %s", ErrorType.DELETE_ERROR.getErrorDescription(), companyId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	private Company extractCompanyFromResultSet(ResultSet resultSet) throws SQLException {
		Company company = new Company();
		company.setId(resultSet.getLong("id"));
		company.setName(resultSet.getString("name"));
		company.setEmail(resultSet.getString("email"));
		company.setAddress(resultSet.getString("address"));
		company.setPhoneNumber(resultSet.getString("phone_number"));
		String industryStr = resultSet.getString("industry_type"); 
		IndustryType industry = IndustryType.valueOf(industryStr);
		company.setIndustry(industry);
		
		return company;
	}
	
	private Company extractCompanyFromResultSetSummarized(ResultSet resultSet) throws SQLException {
		Company company = new Company();
		company.setId(resultSet.getLong("id"));
		company.setName(resultSet.getString("name"));
		company.setPhoneNumber(resultSet.getString("phone_number"));
		
		return company;
	}

}
