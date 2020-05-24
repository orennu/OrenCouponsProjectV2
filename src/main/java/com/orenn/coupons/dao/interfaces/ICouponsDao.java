package com.orenn.coupons.dao.interfaces;

import java.util.Date;
import java.util.List;

import com.orenn.coupons.beans.Coupon;
import com.orenn.coupons.enums.CouponCategory;
import com.orenn.coupons.exceptions.ApplicationException;

public interface ICouponsDao {
	
	public long addCoupon(Coupon coupon) throws ApplicationException;
	
	public boolean isCouponExists(long couponId) throws ApplicationException;
	
	public Date getCouponStartDate(long couponId) throws ApplicationException;
	
	public Date getCouponExpirationDate(long couponId) throws ApplicationException;
	
	public int getCouponQuantity(long couponId) throws ApplicationException;
	
	public Coupon getCouponById(long couponId) throws ApplicationException;
	
	public List<Coupon> getAllCoupons() throws ApplicationException;
	
	public List<Coupon> getCouponsByCompany(long companyId) throws ApplicationException;
	
	public List<Coupon> getCouponsByCategory(CouponCategory category) throws ApplicationException;
	
	public List<Coupon> getCouponsByPriceOrLower(float price) throws ApplicationException;
	
	public void updateCoupon(Coupon coupon) throws ApplicationException;
	
	public void updateCouponQuantityById(long couponId, int quantity, boolean cancel) throws ApplicationException; 
	
	public void removeCoupon(long couponId) throws ApplicationException;
	
	public void removeCouponsByCompanyId(long companyId) throws ApplicationException;
	
	public void removeExpiredCoupons() throws ApplicationException;

}
