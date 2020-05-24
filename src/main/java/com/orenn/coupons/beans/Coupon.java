package com.orenn.coupons.beans;
import java.util.Date;

import com.orenn.coupons.enums.CouponCategory;

public class Coupon {
	
	private long id;
	private String title;
	private String description;
	private long companyId;
	private float price;
	private CouponCategory category;
	private String image;
	private int quantity;
	private Date startDate;
	private Date expirationDate;
	
	public Coupon(long id, String title, String description, long companyId, float price,
			CouponCategory category, String image, int quantity, Date startDate, 
			Date expirationDate) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.companyId = companyId;
		this.price = price;
		this.category = category;
		this.image = image;
		this.quantity = quantity;
		this.startDate = startDate;
		this.expirationDate = expirationDate;
	}

	public Coupon(String title, String description, long companyId, float price, CouponCategory category,
			String image, int quantity, Date startDate, Date expirationDate) {
		this.title = title;
		this.description = description;
		this.companyId = companyId;
		this.price = price;
		this.category = category;
		this.image = image;
		this.quantity = quantity;
		this.startDate = startDate;
		this.expirationDate = expirationDate;
	}

	public Coupon() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public CouponCategory getCategory() {
		return category;
	}

	public void setCategory(CouponCategory category) {
		this.category = category;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	@Override
	public String toString() {
		return "Coupon [id=" + id + ", title=" + title + ", description=" + description
				+ ", companyId=" + companyId + ", price=" + price + ", category=" + category + ", image="
				+ image + ", quantity=" + quantity	+ ", startDate=" + startDate
				+ ", expirationDate="	+ expirationDate + "]";
	}
	
}
