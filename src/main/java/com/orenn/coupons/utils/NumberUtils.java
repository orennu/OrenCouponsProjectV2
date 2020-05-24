package com.orenn.coupons.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NumberUtils {
	
	public static int extractIntFromResultSet(ResultSet resultSet, String field) throws SQLException {
		return resultSet.getInt(field);
	}

}
