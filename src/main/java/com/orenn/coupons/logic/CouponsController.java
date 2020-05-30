package com.orenn.coupons.logic;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.orenn.coupons.beans.Coupon;
import com.orenn.coupons.dao.CouponsDao;
import com.orenn.coupons.enums.CouponCategory;
import com.orenn.coupons.enums.ErrorType;
import com.orenn.coupons.exceptions.ApplicationException;
//import com.orenn.coupons.utils.DataUtils;
import com.orenn.coupons.utils.ValidationsUtils;

@Controller
public class CouponsController {
	
	@Autowired
	private CouponsDao couponsDao;
	@Autowired
	private CompaniesController companiesController;
	
	public CouponsController() {
	}
	
	public void addCoupon(Coupon coupon) throws ApplicationException {
		if (!isCouponAttributesValid(coupon)) {
			throw new ApplicationException();
		}
		
		this.couponsDao.addCoupon(coupon);
	}
	
	public boolean isCouponExists(long couponId) throws ApplicationException {
		if (!this.couponsDao.isCouponExists(couponId)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
										String.format("Coupon id %s %s", couponId, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		return true;
	}

	public Date getCouponStartDate(long couponId) throws ApplicationException {
		if (!this.couponsDao.isCouponExists(couponId)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
										String.format("Coupon id %s %s", couponId, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		return this.couponsDao.getCouponStartDate(couponId);
	}

	public Date getCouponExpirationDate(long couponId) throws ApplicationException {
		if (!this.couponsDao.isCouponExists(couponId)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
										String.format("Coupon id %s %s", couponId, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		return this.couponsDao.getCouponExpirationDate(couponId);
	}
	
	public int getCouponQuantityById(long couponId) throws ApplicationException {
		if (!this.couponsDao.isCouponExists(couponId)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
										String.format("Coupon id %s %s", couponId, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		return this.couponsDao.getCouponQuantity(couponId);
	}
	
	public Coupon getCouponById(long couponId) throws ApplicationException {
		if (!this.couponsDao.isCouponExists(couponId)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
										String.format("Coupon id %s %s", couponId, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		return this.couponsDao.getCouponById(couponId);
	}
	
	public List<Coupon> getAllCoupons() throws ApplicationException {
		return this.couponsDao.getAllCoupons();
	}
	
	public List<Coupon> getCouponsByCompany(long companyId) throws ApplicationException {
		if (!this.companiesController.isCompanyExists(companyId)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
										String.format("Company id %s %s", companyId, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		return this.couponsDao.getCouponsByCompany(companyId);
	}
	
	public List<Coupon> getCouponsByCategory(String categoryStr) throws ApplicationException {
		if (categoryStr == null) {
			throw new ApplicationException(ErrorType.NULL_ERROR, 
										String.format("%s coupon category", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		
		CouponCategory category = CouponCategory.valueOf(categoryStr);
		return this.couponsDao.getCouponsByCategory(category);
	}
	
	public List<Coupon> getCouponsByPriceOrLower(float price) throws ApplicationException {
		if (price <= 0) {
			throw new ApplicationException(ErrorType.INVALID_PRICE_ERROR, ErrorType.INVALID_PRICE_ERROR.getErrorDescription());
		}
		
		return this.couponsDao.getCouponsByPriceOrLower(price);
	}
	
	public void updateCoupon(Coupon coupon) throws ApplicationException {
		if (!this.couponsDao.isCouponExists(coupon.getId())) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
										String.format("Coupon id %s %s", coupon.getId(), ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		if (!isCouponAttributesValid(coupon)) {
			throw new ApplicationException();
		}
		
		this.couponsDao.updateCoupon(coupon);
	}
	
	public void updateCouponQuantityById(long couponId, int quantity, boolean cancel) throws ApplicationException {
		if (!this.couponsDao.isCouponExists(couponId)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
										String.format("Coupon id %s %s", couponId, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		if (quantity <= 0 || quantity > getCouponQuantityById(couponId)) {
			throw new ApplicationException(ErrorType.INVALID_QUANTITY_ERROR,  
										String.format("%s, cannot be 0 or less and cannot be greater than coupon quantity", 
												ErrorType.INVALID_QUANTITY_ERROR.getErrorDescription()));
		}
		
		this.couponsDao.updateCouponQuantityById(couponId, quantity, cancel);
	}
	
	public void removeCoupon(long couponId) throws ApplicationException {
		if (!this.couponsDao.isCouponExists(couponId)) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
										String.format("Coupon id %s %s", couponId, ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		
		this.couponsDao.removeCoupon(couponId);
	}
	
	public void removeExpiredCoupons() throws ApplicationException {
		this.couponsDao.removeExpiredCoupons();
	}
	
	private boolean isCouponAttributesValid(Coupon coupon) throws ApplicationException {
		if (coupon == null) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s coupon", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		if (!ValidationsUtils.isValidLength(coupon.getTitle(), 10, 50)) {
			throw new ApplicationException(ErrorType.INVALID_LENGTH_ERROR,  
										String.format("%s, title must be between 10 - 50 characters", 
												ErrorType.INVALID_LENGTH_ERROR.getErrorDescription()));
		}
		if (!ValidationsUtils.isValidLength(coupon.getDescription(), 10, 255)) {
			throw new ApplicationException(ErrorType.INVALID_LENGTH_ERROR,  
										String.format("%s, description must be between 10 - 255 characters", 
												ErrorType.INVALID_LENGTH_ERROR.getErrorDescription()));
		}
		if (!ValidationsUtils.isValidDescription(coupon.getTitle(), "Title")) {
			throw new ApplicationException(ErrorType.INVALID_CHARS_ERROR, 
										String.format("%s in title", ErrorType.INVALID_CHARS_ERROR.getErrorDescription()));
		}
		if (!ValidationsUtils.isValidDescription(coupon.getDescription(), "Description")) {
			throw new ApplicationException(ErrorType.INVALID_CHARS_ERROR, 
										String.format("%s in description", ErrorType.INVALID_CHARS_ERROR.getErrorDescription()));
		}
		if (!this.companiesController.isCompanyExists(coupon.getCompanyId())) {
			throw new ApplicationException(ErrorType.NOT_EXISTS_ERROR, 
										String.format("Company id %s %s ", 
												coupon.getCompanyId(), ErrorType.NOT_EXISTS_ERROR.getErrorDescription()));
		}
		if (coupon.getPrice() <= 0) {
			throw new ApplicationException(ErrorType.INVALID_PRICE_ERROR, ErrorType.INVALID_PRICE_ERROR.getErrorDescription());
		}
		if (!ValidationsUtils.isValidUrl(coupon.getImage())) {
			throw new ApplicationException(ErrorType.INVALID_FORMAT_ERROR, 
										String.format("%s, image must use URL format", ErrorType.INVALID_FORMAT_ERROR.getErrorDescription()));
		}
		if (coupon.getQuantity() <= 0) {
			throw new ApplicationException(ErrorType.INVALID_QUANTITY_ERROR, 
										String.format("%s, cannot be 0 or less", ErrorType.INVALID_QUANTITY_ERROR.getErrorDescription()));
		}
		if (coupon.getStartDate().before(new Date())) {
			throw new ApplicationException(ErrorType.INVALID_DATE_ERROR,  
										String.format("%s, start date cannot be set before current time", 
												ErrorType.INVALID_DATE_ERROR.getErrorDescription()));
		}
		if (coupon.getExpirationDate().before(new Date())) {
			throw new ApplicationException(ErrorType.INVALID_DATE_ERROR, ErrorType.INVALID_DATE_ERROR.getErrorDescription() 
										+ ", expiration date cannot be set before current time");
		}
		if (coupon.getStartDate().after(coupon.getExpirationDate())) {
			throw new ApplicationException(ErrorType.INVALID_DATE_ERROR,  
										String.format("%s, start date cannot be after expiration date", 
												ErrorType.INVALID_DATE_ERROR.getErrorDescription()));
		}
		if (coupon.getCategory() == null) {
			throw new ApplicationException(ErrorType.NULL_ERROR, String.format("%s coupon category", ErrorType.NULL_ERROR.getErrorDescription()));
		}
		
		return true;
		
	}
	
}
