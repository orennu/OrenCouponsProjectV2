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

import com.orenn.coupons.beans.Coupon;
import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.logic.CouponsController;

@RestController
@RequestMapping("/coupons")
public class CouponsApi {
	
	@Autowired
	private CouponsController couponsController;
	
	@PostMapping
	@ResponseBody
	public ResponseEntity<Object> addCoupon(@RequestBody Coupon coupon) throws ApplicationException {
		this.couponsController.addCoupon(coupon);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@ResponseBody
	public Coupon getCouponById(@PathVariable("id") long id) throws ApplicationException {
		return this.couponsController.getCouponById(id);
	}
	
	@GetMapping
	@ResponseBody
	public List<Coupon> getAllCoupons() throws ApplicationException {
		return this.couponsController.getAllCoupons();
	}
	
	@GetMapping(value = "/search", params = "companyId")
	@ResponseBody
	public List<Coupon> getCouponsByCompany(@RequestParam("companyId") long companyId) throws ApplicationException {
		return this.couponsController.getCouponsByCompany(companyId);
	}
	
	@GetMapping(value = "/search", params = "category")
	@ResponseBody
	public List<Coupon> getCouponsByCategory(@RequestParam("category") String category) throws ApplicationException {
		return this.couponsController.getCouponsByCategory(category);
	}
	
	@GetMapping(value = "/search", params = "price")
	@ResponseBody
	public List<Coupon> getCouponsByPriceOrLower(@RequestParam("price") float price) throws ApplicationException {
		return this.couponsController.getCouponsByPriceOrLower(price);
	}
	
	@PutMapping
	@ResponseBody
	public ResponseEntity<Object> updateCoupon(@RequestBody Coupon coupon) throws ApplicationException {
		this.couponsController.updateCoupon(coupon);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<Object> removeCoupon(@PathVariable("id") long id) throws ApplicationException {
		this.couponsController.removeCoupon(id);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
}
