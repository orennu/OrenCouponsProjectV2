package com.orenn.coupons.logic;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.orenn.coupons.beans.Purchase;
import com.orenn.coupons.dao.PurchasesDao;
import com.orenn.coupons.enums.ErrorType;
import com.orenn.coupons.exceptions.ApplicationException;

@Controller
public class PurchasesController {
	
	@Autowired
	private PurchasesDao purchasesDao;
	@Autowired
	private CouponsController couponsController;
	@Autowired
	private CustomersController customersController;
	
	public PurchasesController() {
	}
	
	public long addPurchase(Purchase purchase) throws ApplicationException {
		if (!isPurchaseAttributesValid(purchase)) {
			throw new ApplicationException();
		}
		if (this.purchasesDao.isPurchaseExists(purchase.getCouponId(), purchase.getCustomerId())) {
			throw new ApplicationException(ErrorType.ALREADY_EXISTS_ERROR, String.format("Purchase %s", ErrorType.ALREADY_EXISTS_ERROR.getErrorDescription()));
		}
		
		this.couponsController.updateCouponQuantityById(purchase.getCouponId(), purchase.getQuantity(), false);
		return this.purchasesDao.addPurchase(purchase);
	}
	
	public Purchase getPurchaseById(long purchaseId) throws ApplicationException {
		if (!this.purchasesDao.isPurchaseExists(purchaseId)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
										String.format("Purchase id %s %s", purchaseId, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		return this.purchasesDao.getPurchaseById(purchaseId);
	}
	
	public int getPurchasesQuantityByDate(Date purchaseDate) throws ApplicationException {
		return this.purchasesDao.getPurchasesQuantityByDate(purchaseDate);
	}
	
	public int getPurchasesQuantityByCustomer(long customerId) throws ApplicationException {
		return this.purchasesDao.getPurchasesQuantityByCustomer(customerId);
	}
	
	public int getPurchasesQuantityByCoupon(long couponId) throws ApplicationException {
		return this.purchasesDao.getPurchasesQuantityByCoupon(couponId);
	}
	
	public List<Purchase> getAllPurchases() throws ApplicationException {
		return this.purchasesDao.getAllPurchases();
	}
	
	
	public List<Purchase> getPurchasesByDate(Date purchaseDate) throws ApplicationException {
		return this.purchasesDao.getPurchasesByDate(purchaseDate);
	}
	
	public List<Purchase> getPurchasesByCustomer(long customerId) throws ApplicationException {
		if (this.customersController.isCustomerExists(customerId)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
										String.format("Customer id %s %s", customerId, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		return this.purchasesDao.getPurchasesByCustomer(customerId);
	}
	
	public List<Purchase> getPurchasesByCoupon(long couponId) throws ApplicationException {
		if (this.couponsController.isCouponExists(couponId)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
										String.format("Coupon id %s %s", couponId, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		return this.purchasesDao.getPurchasesByCoupon(couponId);
	}
	
	public void removePurchase(long purchaseId, long couponId) throws ApplicationException {
		if (!this.purchasesDao.isPurchaseExists(purchaseId)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
										String.format("Purchase id %s %s", purchaseId, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		int quantity = this.purchasesDao.getPurchasesQuantityByCoupon(couponId);
		this.couponsController.updateCouponQuantityById(couponId, quantity, true);
		this.purchasesDao.removePurchase(purchaseId);
	}
	
	public void removePurchasesByCustomer(long customerId) throws ApplicationException {
		if (this.customersController.isCustomerExists(customerId)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
					String.format("Customer id %s %s", customerId, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		this.purchasesDao.removePurchasesByCustomer(customerId);
	}
	
	private boolean isPurchaseAttributesValid(Purchase purchase) throws ApplicationException {
		if (purchase == null) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s Purchase", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		if (!this.couponsController.isCouponExists(purchase.getCouponId())) {
			throw new ApplicationException();
		}
		if (this.customersController.getCustomerById(purchase.getCustomerId()).getUser().getType().getName() != "Customer") {
			throw new ApplicationException(ErrorType.FORBIDDEN_ERROR, 
										String.format("%s, only csutomer can purchase coupons", ErrorType.FORBIDDEN_ERROR.getErrorDescription()));
		}
		if (purchase.getQuantity() <= 0) {
			throw new ApplicationException(ErrorType.INVALID_QUANTITY_ERROR, 
										String.format("%s, must be greater than 0", ErrorType.INVALID_QUANTITY_ERROR.getErrorDescription()));
		}
		if (purchase.getQuantity() > this.couponsController.getCouponQuantityById(purchase.getCouponId())) {
			throw new ApplicationException(ErrorType.INVALID_QUANTITY_ERROR, 
										String.format("%s cannot be greater than quantity of coupons", ErrorType.INVALID_QUANTITY_ERROR.getErrorDescription()));
		}
		
		return true;
	}

}
