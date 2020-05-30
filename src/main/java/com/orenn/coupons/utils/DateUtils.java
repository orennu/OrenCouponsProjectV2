package com.orenn.coupons.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	public static java.sql.Timestamp getSqlDateTime(Date dateTime) {
		return new java.sql.Timestamp(dateTime.getTime());
	}
	
	public static java.sql.Timestamp getSqlDateTimeStartOfDay(Date dateTime) {
		java.sql.Timestamp timeStamp = new java.sql.Timestamp(dateTime.getTime());
		timeStamp.setTime(timeStamp.getTime() + (120 * 60 * 1000));
		return timeStamp;
	}
	
	public static java.sql.Timestamp getSqlDateTimeEndOfDay(Date dateTime) {
		java.sql.Timestamp timeStamp = new java.sql.Timestamp(dateTime.getTime());
		timeStamp.setTime(timeStamp.getTime() + (((((23 * 60) + 59) * 60) + 59)  * 1000));
		return timeStamp;
	}
	
	public static Date extractDateFromResultSet(ResultSet resultSet, String dateField) throws SQLException {
		return new Date(resultSet.getTimestamp(dateField).getTime());
	}
	
	public static int calculateAge(Date birthDate) {
		Date currentDate = new Date();
		DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		int birthDateInt = Integer.parseInt(formatter.format(birthDate));
		int currentDateInt = Integer.parseInt(formatter.format(currentDate));
		int age = (currentDateInt - birthDateInt) / 10000;

		return age;
	}
	
	public static Date convertDateStringToDate(String dateStr) throws ParseException {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = formatter.parse(dateStr);

		return date;
	}

}
