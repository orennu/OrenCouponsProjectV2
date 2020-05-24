package com.orenn.coupons.dao.interfaces;

import java.util.List;

import com.orenn.coupons.beans.Company;
import com.orenn.coupons.enums.IndustryType;
import com.orenn.coupons.exceptions.ApplicationException;

public interface ICompaniesDao {
	
	public long addCompany(Company company) throws ApplicationException;
	
	public boolean isCompanyExists(Long companyId) throws ApplicationException;

	public boolean isCompanyExists(String companyName) throws ApplicationException;
	
	public boolean isCompanyEmailExists(String companyEmail) throws ApplicationException;
	
	public boolean isCompanyPhoneNumberExists(String companyPhoneNumber) throws ApplicationException;
	
	public Company getCompanyById(long comapnyId) throws ApplicationException;
	
	public List<Company> getAllCompanies() throws ApplicationException;
	
	public List<Company> getCompaniesByType(IndustryType industry) throws ApplicationException;
	
	public void updateCompany(Company company) throws ApplicationException;
	
	public void removeCompany(long companyId) throws ApplicationException;

}
