package com.orenn.coupons.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.orenn.coupons.beans.Purchase;
import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.logic.PurchasesController;

@RestController
@RequestMapping("/purchases")
public class PurchasesApi {
	
	@Autowired
	private PurchasesController purchasesController;
	
	@PostMapping
	@ResponseBody
	public ResponseEntity<Object> addPurchase(@RequestBody Purchase purchase) throws ApplicationException {
		this.purchasesController.addPurchase(purchase);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@ResponseBody
	public Purchase getCouponById(@PathVariable("id") long id) throws ApplicationException {
		return this.purchasesController.getPurchaseById(id);
	}
	
	@GetMapping
	@ResponseBody
	public List<Purchase> getAllPurchases() throws ApplicationException {
		return this.purchasesController.getAllPurchases();
	}
	
	@GetMapping(value = "/search", params = "purchaseDate")
	@ResponseBody
	public List<Purchase> getPurchasesByDate(@RequestParam("purchaseDate") String purchaseDate) throws ApplicationException {
		return this.purchasesController.getPurchasesByDate(purchaseDate);
	}
	
	@GetMapping(value = "/search", params = "customerId")
	@ResponseBody
	public List<Purchase> getPurchasesByCustomer(@RequestParam("customerId") long customerId) throws ApplicationException {
		return this.purchasesController.getPurchasesByCustomer(customerId);
	}
	
	@GetMapping(value = "/search", params = "couponId")
	@ResponseBody
	public List<Purchase> getPurchasesByCoupon(@RequestParam("couponId") long couponId) throws ApplicationException {
		return this.purchasesController.getPurchasesByCoupon(couponId);
	}
	
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<Object> removePurchase(@PathVariable("id") long id) throws ApplicationException {
		this.purchasesController.removePurchase(id);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
}
