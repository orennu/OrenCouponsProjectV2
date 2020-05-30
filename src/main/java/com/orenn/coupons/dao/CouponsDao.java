package com.orenn.coupons.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.orenn.coupons.beans.Coupon;
import com.orenn.coupons.dao.interfaces.ICouponsDao;
import com.orenn.coupons.enums.CouponCategory;
import com.orenn.coupons.enums.ErrorType;
import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.utils.DateUtils;
import com.orenn.coupons.utils.JdbcUtils;
import com.orenn.coupons.utils.NumberUtils;

@Repository
public class CouponsDao implements ICouponsDao {
	
	public long addCoupon(Coupon coupon) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "INSERT INTO coupons (title, description, company_id, price, category, image, quantity, start_date, expiration_date) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(sqlStatement, PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1,  coupon.getTitle());
			preparedStatement.setString(2,  coupon.getDescription());
			preparedStatement.setLong(3,  coupon.getCompanyId());
			preparedStatement.setFloat(4, coupon.getPrice());
			preparedStatement.setString(5,  coupon.getCategory().name());
			preparedStatement.setString(6, coupon.getImage());
			preparedStatement.setInt(7,  coupon.getQuantity());
			preparedStatement.setTimestamp(8, DateUtils.getSqlDateTime(coupon.getStartDate()));
			preparedStatement.setTimestamp(9, DateUtils.getSqlDateTime(coupon.getExpirationDate()));
			
			preparedStatement.executeUpdate();
			
			resultSet = preparedStatement.getGeneratedKeys();
			
			if (!resultSet.next()) {
				throw new ApplicationException(ErrorType.CREATE_ERROR, 
						String.format("%s, coupon with title %s", ErrorType.CREATE_ERROR.getErrorDescription(), coupon.getTitle()));
			}
			
			return resultSet.getLong(1);
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.CREATE_ERROR, 
					String.format("%s, coupon with title %s", ErrorType.CREATE_ERROR.getErrorDescription(), coupon.getTitle()));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public boolean isCouponExists(long couponId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT id FROM coupons WHERE id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, couponId);
			
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				return true;
			}
			
			return false;
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, 
					String.format("%s, coupon id %", ErrorType.QUERY_ERROR.getErrorDescription(), couponId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public Date getCouponStartDate(long couponId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT start_date from coupons WHERE id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, couponId);
			
			resultSet = preparedStatement.executeQuery();
			
			if (!resultSet.next()) {
				return null;
			}
			
			return DateUtils.extractDateFromResultSet(resultSet, "start_date");
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, 
					String.format("%s, coupon id %", ErrorType.QUERY_ERROR.getErrorDescription(), couponId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public Date getCouponExpirationDate(long couponId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT expiration_date from coupons WHERE id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, couponId);
			
			resultSet = preparedStatement.executeQuery();
			
			if (!resultSet.next()) {
				return null;
			}
			
			return DateUtils.extractDateFromResultSet(resultSet, "expiration_date");
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, 
					String.format("%s, coupon id %", ErrorType.QUERY_ERROR.getErrorDescription(), couponId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public int getCouponQuantity(long couponId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT quantity from coupons WHERE id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, couponId);
			
			resultSet = preparedStatement.executeQuery();
			
			if (!resultSet.next()) {
				return 0;
			}
			
			return NumberUtils.extractIntFromResultSet(resultSet, "quantity");
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, 
					String.format("%s, coupon id %", ErrorType.QUERY_ERROR.getErrorDescription(), couponId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	public Coupon getCouponById(long couponId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT * from coupons WHERE id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, couponId);
			
			resultSet = preparedStatement.executeQuery();
			
			if (!resultSet.next()) {
				return null;
			}
			
			return extractCouponFromResultSet(resultSet);
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, 
					String.format("%s, coupon id %", ErrorType.QUERY_ERROR.getErrorDescription(), couponId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public List<Coupon> getAllCoupons() throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Coupon coupon = null;
		List<Coupon> couponsList = new ArrayList<Coupon>();
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT * FROM coupons";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				coupon = extractCouponFromResultSet(resultSet);
				couponsList.add(coupon);
			}
			
			return couponsList;
			
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
			
	}
	
	public List<Coupon> getCouponsByCompany(long companyId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Coupon coupon = null;
		List<Coupon> couponsList = new ArrayList<Coupon>();
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT * FROM coupons WHERE id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, companyId);
			
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				coupon = extractCouponFromResultSet(resultSet);
				couponsList.add(coupon);
			}
			
			return couponsList;
			
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public List<Coupon> getCouponsByCategory(CouponCategory category) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Coupon coupon = null;
		List<Coupon> couponsList = new ArrayList<Coupon>();
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT * FROM coupons WHERE category = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setString(1, category.name());
			
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				coupon = extractCouponFromResultSet(resultSet);
				couponsList.add(coupon);
			}
			
			return couponsList;
			
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public List<Coupon> getCouponsByPriceOrLower(float price) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Coupon coupon = null;
		List<Coupon> couponsList = new ArrayList<Coupon>();
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT * FROM coupons WHERE price >= ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setFloat(1, price);
			
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				coupon = extractCouponFromResultSet(resultSet);
				couponsList.add(coupon);
			}
			
			return couponsList;
			
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public void updateCoupon(Coupon coupon) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "UPDATE coupons SET title = ?, description = ?, company_id = ?, price = ?, category = ?, image = ?, "
					+ "quantity = ?, start_date = ?, expiration_date = ? WHERE id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setString(1,  coupon.getTitle());
			preparedStatement.setString(2,  coupon.getDescription());
			preparedStatement.setLong(3,  coupon.getCompanyId());
			preparedStatement.setFloat(4, coupon.getPrice());
			preparedStatement.setString(5,  coupon.getCategory().name());
			preparedStatement.setString(6, coupon.getImage());
			preparedStatement.setInt(7,  coupon.getQuantity());
			preparedStatement.setTimestamp(8, DateUtils.getSqlDateTime(coupon.getStartDate()));
			preparedStatement.setTimestamp(9, DateUtils.getSqlDateTime(coupon.getExpirationDate()));
			preparedStatement.setLong(10, coupon.getId());
			
			preparedStatement.executeUpdate();
			
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.UPDATE_ERROR, 
					String.format("%s, coupon id %s", ErrorType.UPDATE_ERROR.getErrorDescription(), coupon.getId()));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
			
	}
	
	public void updateCouponQuantityById(long couponId, int quantity, boolean cancel) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = JdbcUtils.getConnection();
			if (cancel) {
				String sqlStatement = "UPDATE coupons SET quantity = quantity + ? WHERE id = ?";

				preparedStatement = connection.prepareStatement(sqlStatement);
				preparedStatement.setInt(3,  quantity);
				preparedStatement.setLong(4,  couponId);
			}
			else {
				String sqlStatement = "DELETE FROM coupons WHERE id = ? AND quantity - ? = 0; UPDATE coupons SET quantity = quantity - ? WHERE id = ?";

				preparedStatement = connection.prepareStatement(sqlStatement);
				preparedStatement.setLong(1, couponId);
				preparedStatement.setInt(2,  quantity);
				preparedStatement.setInt(3,  quantity);
				preparedStatement.setLong(4,  couponId);
			}

			preparedStatement.executeUpdate();

		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.UPDATE_ERROR, 
					String.format("%s, coupon id %s", ErrorType.UPDATE_ERROR.getErrorDescription(), couponId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}

	}
	
	public void removeCoupon(long couponId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "DELETE FROM coupons WHERE id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, couponId);
			
			preparedStatement.executeUpdate();
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.DELETE_ERROR, 
					String.format("%s, coupon id %s", ErrorType.DELETE_ERROR.getErrorDescription(), couponId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	public void removeCouponsByCompanyId(long companyId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "DELETE FROM coupons WHERE company_id = ?";
			
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
	
	public void removeExpiredCoupons() throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "DELETE FROM coupons WHERE expiration_date < ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setTimestamp(1, DateUtils.getSqlDateTime(new Date()));
			
			preparedStatement.executeUpdate();
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.DELETE_ERROR, ErrorType.DELETE_ERROR.getErrorDescription());
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	private Coupon extractCouponFromResultSet(ResultSet resultSet) throws SQLException {
		Coupon coupon = new Coupon();
		coupon.setId(resultSet.getLong("id"));
		coupon.setTitle(resultSet.getString("title"));
		coupon.setDescription(resultSet.getString("description"));
		coupon.setCompanyId(resultSet.getLong("company_id"));
		coupon.setPrice(resultSet.getFloat("price"));
		String categoryStr = resultSet.getString("category"); 
		CouponCategory category = CouponCategory.valueOf(categoryStr);
		coupon.setCategory(category);
		coupon.setImage(resultSet.getString("image"));
		coupon.setQuantity(resultSet.getInt("quantity"));
		coupon.setStartDate(new Date(resultSet.getTimestamp("start_date").getTime()));
		coupon.setExpirationDate(new Date(resultSet.getTimestamp("expiration_date").getTime()));
		
		return coupon;
	}
	
//	private Coupon extractCouponFromResultSetSummarized(ResultSet resultSet) throws SQLException {
//		Coupon coupon = new Coupon();
//		coupon.setId(resultSet.getLong("id"));
//		coupon.setTitle(resultSet.getString("title"));
//		coupon.setPrice(resultSet.getFloat("price"));
//		coupon.setExpirationDate(new Date(resultSet.getTimestamp("expiration_date").getTime()));
//		
//		return coupon;
//	}
	
}
