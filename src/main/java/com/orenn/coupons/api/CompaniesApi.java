package com.orenn.coupons.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.orenn.coupons.beans.Company;
import com.orenn.coupons.enums.IndustryType;
import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.logic.CompaniesController;

@RestController
@RequestMapping("/companies")
public class CompaniesApi {
	
	@Autowired
	private CompaniesController companiesController;
	
	@PostMapping
	@ResponseBody
	public ResponseEntity<Object> addCompany(@RequestBody Company company) throws ApplicationException {
		this.companiesController.addCompany(company);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@ResponseBody
	public Company getCompanyById(@PathVariable("id") long id) throws ApplicationException {
		return this.companiesController.getCompanyById(id);
	}
	
	@GetMapping
	@ResponseBody
	public List<Company> getAllCompanies() throws ApplicationException {
		return this.companiesController.getAllCompanies();
	}
	
	@GetMapping(value = "/search", params = "industry")
	@ResponseBody
	public List<Company> getCompaniesByType(@RequestParam("industry") IndustryType industry) throws ApplicationException {
		return this.companiesController.getCompaniesByType(industry);
	}
	
	@PutMapping
	@ResponseBody
	public ResponseEntity<Object> updateCompany(@RequestBody Company company) throws ApplicationException {
		this.companiesController.updateCompany(company);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<Object> removeCompany(@PathVariable("id") long id) throws ApplicationException {
		this.companiesController.removeCompany(id);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
}
