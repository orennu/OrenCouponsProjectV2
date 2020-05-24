package com.orenn.coupons.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.orenn.coupons.beans.Company;
import com.orenn.coupons.dao.CompaniesDao;
import com.orenn.coupons.enums.ErrorType;
import com.orenn.coupons.enums.IndustryType;
import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.logic.CompaniesController;
import com.orenn.coupons.utils.JdbcUtils;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class TestCompany {
	
	private static Company company;
	private static CompaniesDao companiesDao;
	private static CompaniesController companiesController;
	private static long companyId;
	private static Company testCompanyValid;
	private static Company testCompanyNullObject;
	private static Company testCompanyExistingEmail;
	private static Company testCompanyExistingPhone;
	private static Company testCompanyNullName;
	private static Company testCompanyInvalidShortName;
	private static Company testCompanyInvalidLongName;
	private static Company testCompanyInvalidSpacesOnlyName;
	private static Company testCompanyInvalidLeadingSpacesName;
	private static Company testCompanyInvalidTrailingSpacesName;
	private static Company testCompanyInvalidCharsInName;
	private static Company testCompanyInvalidPhoneShort;
	private static Company testCompanyInvalidPhoneLong;
	private static Company testCompanyInvalidCharsInPhone;
	private static Company testCompanyInvalidEmail;
	private static Company testCompanyAddressEmpty;
	private static Company testCompanyLongAddress;
	private static Company testCompanyInvalidCharsInAddress;
	
	
	@BeforeAll
	public static void setUpClass() throws ApplicationException {
		companyId = 0;
		companiesDao = new CompaniesDao();
		company = new Company("Test Company", "test@testcompany.com", "+100000000000", "0 Test st.", 
				IndustryType.ELECTRONICS);
		companyId = companiesDao.addCompany(company);
		
		companiesController = new CompaniesController();
		testCompanyValid = new Company("Test Company 1", "test1@testcompany.com", "+100000000001", "1st Test st.", 
				IndustryType.ELECTRONICS);
		testCompanyNullObject = null;
		testCompanyExistingEmail = new Company("Test Company 2", "test@testcompany.com", "+100000000002", "2nd Test st.", 
				IndustryType.ELECTRONICS);
		testCompanyExistingPhone = new Company("Test Company 3", "test2@testcompany.com", "+100000000000", "3rd Test st.", 
				IndustryType.ELECTRONICS);
		testCompanyNullName = new Company(null, "test4@testcompany.com", "+100000000004", "4th Test st.", 
				IndustryType.ELECTRONICS);
		testCompanyInvalidShortName = new Company("a", "test5@testcompany.com", "+100000000005", "5th Test st.", 
				IndustryType.ELECTRONICS);
		testCompanyInvalidLongName = new Company("aaaaaaaaaaaaaaaaaaaaa", "test6@testcompany.com", "+100000000006", "6th Test st.", 
				IndustryType.ELECTRONICS);
		testCompanyInvalidSpacesOnlyName = new Company("     ", "test7@testcompany.com", "+100000000007", "7th Test st.", 
				IndustryType.ELECTRONICS);
		testCompanyInvalidLeadingSpacesName = new Company("  Test Company 8", "test8@testcompany.com", "+100000000008", "8th Test st.", 
				IndustryType.ELECTRONICS);
		testCompanyInvalidTrailingSpacesName = new Company("Test Company 9  ", "test9@testcompany.com", "+100000000009", "9th Test st.", 
				IndustryType.ELECTRONICS);
		testCompanyInvalidCharsInName = new Company("Test Company %", "test10@testcompany.com", "+100000000010", "10th Test st.", 
				IndustryType.ELECTRONICS);
		testCompanyInvalidPhoneShort = new Company("Test Company 11", "test11@testcompany.com", "+1", "11th Test st.", 
				IndustryType.ELECTRONICS);
		testCompanyInvalidPhoneLong = new Company("Test Company 12", "test12@testcompany.com", "+10000000000000000", "12th Test st.", 
				IndustryType.ELECTRONICS);
		testCompanyInvalidCharsInPhone = new Company("Test Company 13", "test13@testcompany.com", "+10000000000A", "13th Test st.", 
				IndustryType.ELECTRONICS);
		testCompanyInvalidEmail = new Company("Test Company 14", "test14@testcompany@com", "+100000000014", "14th Test st.", 
				IndustryType.ELECTRONICS);
		testCompanyAddressEmpty = new Company("Test Company 15", "test15@testcompany.com", "+100000000015", "", IndustryType.ELECTRONICS);
		testCompanyLongAddress = new Company("Test Company 16", "test16@testcompany.com", "+100000000016", String.format("%0" + 256 + "d", 0).replace('0', 'f'), IndustryType.ELECTRONICS);
		testCompanyInvalidCharsInAddress = new Company("Test Company 17", "test17@testcompany.com", "+100000000017", "Main st. #33", IndustryType.ELECTRONICS);
	}
	
	public static void setUpMultipleCompanies(int count) throws ApplicationException {
		companiesDao = new CompaniesDao();
		 
		for (int i = 0; i < count; i++) {
			company = new Company(String.format("Test Company 10%d", i), String.format("testget%d@testcompany.com", i), 
					String.format("+20000000000%d", i), String.format("1000%d Test st.", i), IndustryType.ELECTRONICS);
			companiesDao.addCompany(company);
		}
	}
	
	public static void removeAllCompanies() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			connection = JdbcUtils.getConnection();
			String sqlStatement = "DELETE FROM companies";

			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();;
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	@Test
	@Order(1)
	public void testAddCompanyValid() throws ApplicationException {
		long id = companiesController.addCompany(testCompanyValid);
		assertNotEquals(0, id);
	}
	
	@Test
	@Order(2)
	public void testAddNullCompany() {
		ApplicationException applicationException = assertThrows(ApplicationException.class, () -> {
			companiesController.addCompany(testCompanyNullObject);
		});
		
		ErrorType expectedErrorType = ErrorType.NULL_ERROR;
		ErrorType actualErrorType = applicationException.getErrorType();
		
		assertEquals(expectedErrorType, actualErrorType);
			
	}
	
	@Test
	@Order(3)
	public void testAddCompanyWithExistingEmail() {
		ApplicationException applicationException = assertThrows(ApplicationException.class, () -> {
			companiesController.addCompany(testCompanyExistingEmail);
		});
		
		ErrorType expectedErrorType = ErrorType.ALREADY_EXISTS_ERROR;
		ErrorType actualErrorType = applicationException.getErrorType();
		
		assertEquals(expectedErrorType, actualErrorType);
	}
	
	@Test
	@Order(4)
	public void testAddCompanyWithExistingPhone() {
		ApplicationException applicationException = assertThrows(ApplicationException.class, () -> {
			companiesController.addCompany(testCompanyExistingPhone);
		});
		
		ErrorType expectedErrorType = ErrorType.ALREADY_EXISTS_ERROR;
		ErrorType actualErrorType = applicationException.getErrorType();
		
		assertEquals(expectedErrorType, actualErrorType);
	}
	
	@Test
	@Order(5)
	public void testAddCompanyWithNullName() {
		ApplicationException applicationException = assertThrows(ApplicationException.class, () -> {
			companiesController.addCompany(testCompanyNullName);
		});
		
		ErrorType expectedErrorType = ErrorType.NULL_ERROR;
		ErrorType actualErrorType = applicationException.getErrorType();
		
		assertEquals(expectedErrorType, actualErrorType);
	}
	
	@Test
	@Order(6)
	public void testAddCompanyWithInvalidShortName() {
		ApplicationException applicationException = assertThrows(ApplicationException.class, () -> {
			companiesController.addCompany(testCompanyInvalidShortName);
		});
		
		ErrorType expectedErrorType = ErrorType.INVALID_LENGTH_ERROR;
		ErrorType actualErrorType = applicationException.getErrorType();
		
		assertEquals(expectedErrorType, actualErrorType);
	}
	
	@Test
	@Order(7)
	public void testAddCompanyWithInvalidLongName() {
		ApplicationException applicationException = assertThrows(ApplicationException.class, () -> {
			companiesController.addCompany(testCompanyInvalidLongName);
		});
		
		ErrorType expectedErrorType = ErrorType.INVALID_LENGTH_ERROR;
		ErrorType actualErrorType = applicationException.getErrorType();
		
		assertEquals(expectedErrorType, actualErrorType);
	}
	
	@Test
	@Order(8)
	public void testAddCompanyWithInvalidNameSpacesOnly() {
		ApplicationException applicationException = assertThrows(ApplicationException.class, () -> {
			companiesController.addCompany(testCompanyInvalidSpacesOnlyName);
		});
		
		ErrorType expectedErrorType = ErrorType.INVALID_WHITESPACE_ERROR;
		ErrorType actualErrorType = applicationException.getErrorType();
		
		assertEquals(expectedErrorType, actualErrorType);
	}
	
	@Test
	@Order(9)
	public void testAddCompanyWithInvalidNameLeadingSpaces() {
		ApplicationException applicationException = assertThrows(ApplicationException.class, () -> {
			companiesController.addCompany(testCompanyInvalidLeadingSpacesName);
		});
		
		ErrorType expectedErrorType = ErrorType.INVALID_WHITESPACE_ERROR;
		ErrorType actualErrorType = applicationException.getErrorType();
		
		assertEquals(expectedErrorType, actualErrorType);
	}
	
	@Test
	@Order(10)
	public void testAddCompanyWithInvalidNameTrailingSpaces() {
		ApplicationException applicationException = assertThrows(ApplicationException.class, () -> {
			companiesController.addCompany(testCompanyInvalidTrailingSpacesName);
		});
		
		ErrorType expectedErrorType = ErrorType.INVALID_WHITESPACE_ERROR;
		ErrorType actualErrorType = applicationException.getErrorType();
		
		assertEquals(expectedErrorType, actualErrorType);
	}
	
	@Test
	@Order(11)
	public void testAddCompanyWithInvalidCharsInName() {
		ApplicationException applicationException = assertThrows(ApplicationException.class, () -> {
			companiesController.addCompany(testCompanyInvalidCharsInName);
		});
		
		ErrorType expectedErrorType = ErrorType.INVALID_CHARS_ERROR;
		ErrorType actualErrorType = applicationException.getErrorType();
		
		assertEquals(expectedErrorType, actualErrorType);
	}
	
	@Test
	@Order(12)
	public void testAddCompanyWithInvalidPhoneShort() {
		ApplicationException applicationException = assertThrows(ApplicationException.class, () -> {
			companiesController.addCompany(testCompanyInvalidPhoneShort);
		});
		
		ErrorType expectedErrorType = ErrorType.INVALID_FORMAT_ERROR;
		ErrorType actualErrorType = applicationException.getErrorType();
		
		assertEquals(expectedErrorType, actualErrorType);
	}
	
	@Test
	@Order(13)
	public void testAddCompanyWithInvalidPhoneLong() {
		ApplicationException applicationException = assertThrows(ApplicationException.class, () -> {
			companiesController.addCompany(testCompanyInvalidPhoneLong);
		});
		
		ErrorType expectedErrorType = ErrorType.INVALID_FORMAT_ERROR;
		ErrorType actualErrorType = applicationException.getErrorType();
		
		assertEquals(expectedErrorType, actualErrorType);
	}
	
	@Test
	@Order(14)
	public void testAddCompanyWithInvalidPhone() {
		ApplicationException applicationException = assertThrows(ApplicationException.class, () -> {
			companiesController.addCompany(testCompanyInvalidCharsInPhone);
		});
		
		ErrorType expectedErrorType = ErrorType.INVALID_FORMAT_ERROR;
		ErrorType actualErrorType = applicationException.getErrorType();
		
		assertEquals(expectedErrorType, actualErrorType);
	}
	
	@Test
	@Order(15)
	public void testAddCompanyWithInvalidEmail() {
		ApplicationException applicationException = assertThrows(ApplicationException.class, () -> {
			companiesController.addCompany(testCompanyInvalidEmail);
		});
		
		ErrorType expectedErrorType = ErrorType.INVALID_FORMAT_ERROR;
		ErrorType actualErrorType = applicationException.getErrorType();
		
		assertEquals(expectedErrorType, actualErrorType);
	}
	
	@Test
	@Order(16)
	public void testAddCompanyWithEmptyAddress() {
		ApplicationException applicationException = assertThrows(ApplicationException.class, () -> {
			companiesController.addCompany(testCompanyAddressEmpty);
		});
		
		ErrorType expectedErrorType = ErrorType.INVALID_LENGTH_ERROR;
		ErrorType actualErrorType = applicationException.getErrorType();
		
		assertEquals(expectedErrorType, actualErrorType);
	}
	
	@Test
	@Order(17)
	public void testAddCompanyWithLongAddress() {
		ApplicationException applicationException = assertThrows(ApplicationException.class, () -> {
			companiesController.addCompany(testCompanyLongAddress);
		});
		
		ErrorType expectedErrorType = ErrorType.INVALID_LENGTH_ERROR;
		ErrorType actualErrorType = applicationException.getErrorType();
		
		assertEquals(expectedErrorType, actualErrorType);
	}
	
	@Test
	@Order(18)
	public void testAddCompanyWithInvalidCharsInAddress() {
		ApplicationException applicationException = assertThrows(ApplicationException.class, () -> {
			companiesController.addCompany(testCompanyInvalidCharsInAddress);
		});
		
		ErrorType expectedErrorType = ErrorType.INVALID_CHARS_ERROR;
		ErrorType actualErrorType = applicationException.getErrorType();
		
		assertEquals(expectedErrorType, actualErrorType);
	}
	
	@Test
	@Order(19)
	public void testIsCompanyExistsExistingCompany() throws ApplicationException {
		assertTrue(companiesController.isCompanyExists(companyId));
	}
	
	@Test
	@Order(20)
	public void testIsCompanyExistsNoneExistingCompany() throws ApplicationException {
		ApplicationException applicationException = assertThrows(ApplicationException.class, () -> {
			companiesController.isCompanyExists(0L);
		});
		
		ErrorType expectedErrorType = ErrorType.NOT_EXISTS_ERROR;
		ErrorType actualErrorType = applicationException.getErrorType();
		
		assertEquals(expectedErrorType, actualErrorType);
	}
	
	@Test
	@Order(21)
	public void testGetCompanyByIdExistingCompany() throws ApplicationException {
		long expectedId = companyId;
		Company actualCompany = companiesController.getCompanyById(companyId);
		long actualId = actualCompany.getId();
		
		assertEquals(expectedId, actualId);
	}
	
	@Test
	@Order(22)
	public void testGetCompanyByIdNoneExistingCompany() throws ApplicationException {
		ApplicationException applicationException = assertThrows(ApplicationException.class, () -> {
			companiesController.getCompanyById(0);
		});
		
		ErrorType expectedErrorType = ErrorType.NOT_EXISTS_ERROR;
		ErrorType actualErrorType = applicationException.getErrorType();
		
		assertEquals(expectedErrorType, actualErrorType);
	}
	
	@Test
	@Order(23)
	public void testGetAllCompaniesWithCount() throws ApplicationException {
		int count = 5;
		removeAllCompanies();
		setUpMultipleCompanies(count);
		int companiesCount = companiesDao.getAllCompanies().size();
		assertEquals(companiesCount, count);
	}
	
	@Test
	@Order(24)
	public void testGetAllCompaniesEmptySet() throws ApplicationException {
		removeAllCompanies();
		int companiesCount = companiesDao.getAllCompanies().size();
		assertEquals(companiesCount, 0);
	}
	
	@Test
	@Order(25)
	public void testUpdateCompanyAllFields() throws ApplicationException {}
	
	@Test
	@Order(26)
	public void testUpdateCompanyWithExistingEmail() throws ApplicationException {}
	
	@Test
	@Order(27)
	public void testUpdateCompanyNoneExistingCompany() throws ApplicationException {}
	
	@Test
	@Order(28)
	public void testRemoveExistingCompany() throws ApplicationException {}
	
	@Test
	@Order(29)
	public void testRemoveNoneExistingCompany() throws ApplicationException {}
	
	@Test
	@Order(30)
	public void testValidateCapitalizeCompanyName() throws ApplicationException {}
	
	@AfterAll
	public static void tearDownClass() {
		removeAllCompanies();
	}
}
