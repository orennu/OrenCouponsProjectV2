package com.orenn.coupons.dao.interfaces;

import java.util.Date;
import java.util.List;

import com.orenn.coupons.beans.Purchase;
import com.orenn.coupons.exceptions.ApplicationException;

public interface IPurchasesDao {
	
	public long addPurchase(Purchase purchase) throws ApplicationException;
	
	public boolean isPurchaseExists(long purchaseId) throws ApplicationException;
	
	public boolean isPurchaseExists(long couponId, long customerId) throws ApplicationException;
	
	public Purchase getPurchaseById(long purchaseId) throws ApplicationException;
	
	public int getPurchasesQuantityByDate(Date purchaseDate) throws ApplicationException;
	
	public int getPurchasesQuantityByCustomer(long customerId) throws ApplicationException;
	
	public int getPurchasesQuantityByCoupon(long couponId) throws ApplicationException;
	
	public List<Purchase> getAllPurchases() throws ApplicationException;
	
	public List<Purchase> getPurchasesByDate(Date purchaseDate) throws ApplicationException;
	
	public List<Purchase> getPurchasesByCustomer(long customerId) throws ApplicationException;
	
	public List<Purchase> getPurchasesByCoupon(long couponId) throws ApplicationException;
	
	public void removePurchase(long purchaseId) throws ApplicationException;
	
	public void removePurchasesByCustomer(long customerId) throws ApplicationException;
	
}
