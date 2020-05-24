package com.orenn.coupons.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.orenn.coupons.beans.Purchase;
import com.orenn.coupons.dao.interfaces.IPurchasesDao;
import com.orenn.coupons.enums.ErrorType;
import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.utils.DateUtils;
import com.orenn.coupons.utils.JdbcUtils;

@Repository
public class PurchasesDao implements IPurchasesDao {
	
	public long addPurchase(Purchase purchase) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "INSERT INTO purchases (customer_id, coupon_id, quantity, purchase_date) VALUES (?, ?, ?, ?)";
			
			preparedStatement = connection.prepareStatement(sqlStatement, PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.setLong(1,  purchase.getCustomerId());
			preparedStatement.setLong(2,  purchase.getCouponId());
			preparedStatement.setInt(3,  purchase.getQuantity());
			preparedStatement.setTimestamp(4, DateUtils.getSqlDateTime(purchase.getPurchaseDate()));
			
			preparedStatement.executeUpdate();
			
			resultSet = preparedStatement.getGeneratedKeys();
			
			if (!resultSet.next()) {
				throw new ApplicationException(ErrorType.CREATE_ERROR, 
						String.format("%s, purchase with coupon id %s and customer id %s", 
								ErrorType.CREATE_ERROR.getErrorDescription(), purchase.getCouponId(), purchase.getCustomerId()));
			}
			
			return resultSet.getLong(1);
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.CREATE_ERROR, 
					String.format("%s, purchase with coupon id %s and customer id %s", 
							ErrorType.CREATE_ERROR.getErrorDescription(), purchase.getCouponId(), purchase.getCustomerId()));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	public boolean isPurchaseExists(long purchaseId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT id FROM purchases WHERE id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, purchaseId);
			
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				return true;
			}
			
			return false;
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, String.format("%s, purchase id %s", ErrorType.QUERY_ERROR.getErrorDescription(), purchaseId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public boolean isPurchaseExists(long couponId, long customerId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT id FROM purchases WHERE coupon_id = ? AND customer_id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, couponId);
			preparedStatement.setLong(1, customerId);
			
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				return true;
			}
			
			return false;
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, 
					String.format("%s, coupon id %s, customer id %s" , ErrorType.QUERY_ERROR.getErrorDescription(), couponId, customerId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public Purchase getPurchaseById(long purchaseId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT * from purchases WHERE id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, purchaseId);
			
			resultSet = preparedStatement.executeQuery();
			
			return extractPurchaseFromResultSet(resultSet);
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, 
					String.format("%s, purchase id %s", ErrorType.QUERY_ERROR.getErrorDescription(), purchaseId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	public int getPurchasesQuantityByDate(Date purchaseDate) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT SUM(quantity) from purchases WHERE purchase_date >= ? AND purchase_date <= ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setTimestamp(1, DateUtils.getSqlDateTimeStartOfDay(purchaseDate));
			preparedStatement.setTimestamp(2, DateUtils.getSqlDateTimeEndOfDay(purchaseDate));
			
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
			return 0;
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, 
					String.format("%s, purchase date %s", ErrorType.QUERY_ERROR.getErrorDescription(), purchaseDate));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	public int getPurchasesQuantityByCustomer(long customerId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT SUM(quantity) from purchases WHERE customer_id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, customerId);
			
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
			return 0;
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, 
					String.format("%s, customre id %s", ErrorType.QUERY_ERROR.getErrorDescription(), customerId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	public int getPurchasesQuantityByCoupon(long couponId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT SUM(quantity) from purchases WHERE coupon_id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, couponId);
			
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
			return 0;
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, 
					String.format("%s, coupon id %s", ErrorType.QUERY_ERROR.getErrorDescription(), couponId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	public List<Purchase> getAllPurchases() throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Purchase purchase = null;
		List<Purchase> purchasesList = new ArrayList<Purchase>();
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT id, coupon_id, quantity FROM purchases";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				purchase = extractPurchaseFromResultSetSummarized(resultSet);
				purchasesList.add(purchase);
			}
			
			return purchasesList;
			
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	public List<Purchase> getPurchasesByDate(Date purchaseDate) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Purchase purchase = null;
		List<Purchase> purchasesList = new ArrayList<Purchase>();
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT id, coupon_id, quantity FROM purchases WHERE purchase_date >= ? AND purchase_date <= ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setTimestamp(1, DateUtils.getSqlDateTimeStartOfDay(purchaseDate));
			preparedStatement.setTimestamp(2, DateUtils.getSqlDateTimeEndOfDay(purchaseDate));
			
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				purchase = extractPurchaseFromResultSetSummarized(resultSet);
				purchasesList.add(purchase);
			}
			
			return purchasesList;
			
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	public List<Purchase> getPurchasesByCustomer(long customerId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Purchase purchase = null;
		List<Purchase> purchasesList = new ArrayList<Purchase>();
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT id, coupon_id, quantity FROM purchases WHERE customer_id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, customerId);
			
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				purchase = extractPurchaseFromResultSetSummarized(resultSet);
				purchasesList.add(purchase);
			}
			
			return purchasesList;
			
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	public List<Purchase> getPurchasesByCoupon(long couponId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Purchase purchase = null;
		List<Purchase> purchasesList = new ArrayList<Purchase>();
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "SELECT id, coupon_id, quantity FROM purchases WHERE coupon_id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, couponId);
			
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				purchase = extractPurchaseFromResultSetSummarized(resultSet);
				purchasesList.add(purchase);
			}
			
			return purchasesList;
			
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.QUERY_ERROR, ErrorType.QUERY_ERROR.getErrorDescription());
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	public void removePurchase(long purchaseId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "DELETE FROM purchases WHERE id = ?";
			
			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setLong(1, purchaseId);
			
			preparedStatement.executeUpdate();
		}
		catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.DELETE_ERROR, 
					String.format("%s, purchase id %s", ErrorType.DELETE_ERROR.getErrorDescription(), purchaseId));
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	public void removePurchasesByCustomer(long customerId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String sqlStatement = "DELETE FROM purchases WHERE customer_id = ?";
			
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
	
	private Purchase extractPurchaseFromResultSet(ResultSet resultSet) throws SQLException {
		Purchase purchase = new Purchase();
		purchase.setId(resultSet.getLong("id"));
		purchase.setCustomerId(resultSet.getLong("customer_id"));
		purchase.setCouponId(resultSet.getLong("coupon_id"));
		purchase.setQuantity(resultSet.getInt("quantity"));
		purchase.setPurchaseDate(new Date(resultSet.getTimestamp("purchase_date").getTime()));
		
		return purchase;
	}
	
	private Purchase extractPurchaseFromResultSetSummarized(ResultSet resultSet) throws SQLException {
		Purchase purchase = new Purchase();
		purchase.setId(resultSet.getLong("id"));
		purchase.setCouponId(resultSet.getLong("coupon_id"));
		purchase.setQuantity(resultSet.getInt("quantity"));
		
		return purchase;
	}

}
