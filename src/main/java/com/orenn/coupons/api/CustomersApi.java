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

import com.orenn.coupons.beans.Customer;
import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.logic.CustomersController;

@RestController
@RequestMapping("/customers")
public class CustomersApi {
	
	@Autowired
	private CustomersController customersController;
	
	@PostMapping
	@ResponseBody
	public ResponseEntity<Object> addCustomer(@RequestBody Customer customer) throws ApplicationException {
		this.customersController.addCustomer(customer);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@ResponseBody
	public Customer getCustomerById(@PathVariable("id") long id) throws ApplicationException {
		return this.customersController.getCustomerById(id);
	}
	
	@GetMapping
	@ResponseBody
	public List<Customer> getAllCustomers() throws ApplicationException {
		return this.customersController.getAllCustomers();
	}
	
	@GetMapping(value = "/search", params = "dateOfBirth")
	@ResponseBody
	public List<Customer> getBirthDayCustomers(@RequestParam("dateOfBirth") String dateOfBirth) throws ApplicationException {
		return this.customersController.getBirthDayCustomers(dateOfBirth);
	}
	
	@PutMapping
	@ResponseBody
	public ResponseEntity<Object> updateCustomer(@RequestBody Customer customer) throws ApplicationException {
		this.customersController.updateCustomer(customer);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<Object> removeCustomer(@PathVariable("id") long id) throws ApplicationException {
		this.customersController.removeCustomer(id);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
}
