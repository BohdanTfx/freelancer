package com.epam.freelancer.database.model;

import com.epam.freelancer.database.transformer.annotation.Column;
import com.epam.freelancer.database.transformer.annotation.Id;
import com.epam.freelancer.database.transformer.annotation.Table;

/**
 * Created by ������ on 16.01.2016.
 */
@Table(name = "follower")
public class Follower implements BaseEntity<Integer> {
	@Id
	private Integer id;
	@Column(name = "dev_id")
	private Integer devId;
	@Column(name = "cust_id")
	private Integer custId;
	@Column
	private String message;
	@Column(name = "order_id")
	private Integer orderId;
	@Column(name = "is_deleted")
	private Boolean isDeleted;
	@Column
	private Integer version;
	@Column(name="is_hired")
	private Boolean isHired;
	@Column
	private String author;
	private Ordering order;
	private Developer developer;
	private Customer customer;

	public Follower() {
	}

	public Integer getDevId() {
		return devId;
	}

	public void setDevId(Integer devId) {
		this.devId = devId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public Boolean getDeleted() {
		return isDeleted;
	}

	@Override
	public void setDeleted(Boolean deleted) {
		this.isDeleted = deleted;
	}

	@Override
	public Integer getVersion() {
		return version;
	}

	@Override
	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Follower follower = (Follower) o;

		if (devId != null ? !devId.equals(follower.devId)
				: follower.devId != null)
			return false;
		if (message != null ? !message.equals(follower.message)
				: follower.message != null)
			return false;
		if (orderId != null ? !orderId.equals(follower.orderId)
				: follower.orderId != null)
			return false;
		return !(isDeleted != null ? !isDeleted.equals(follower.isDeleted)
				: follower.isDeleted != null);

	}

	@Override
	public int hashCode() {
		int result = devId != null ? devId.hashCode() : 0;
		result = 31 * result + (message != null ? message.hashCode() : 0);
		result = 31 * result + (orderId != null ? orderId.hashCode() : 0);
		result = 31 * result + (isDeleted != null ? isDeleted.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Follower{" + "id=" + id + ", devId=" + devId + ", message='"
				+ message + '\'' + ", orderId=" + orderId + ", isDeleted="
				+ isDeleted + ", version=" + version + '}';
	}

	public Ordering getOrder() {
		return order;
	}

	public void setOrder(Ordering order) {
		this.order = order;
	}

	public Developer getDeveloper() {
		return developer;
	}

	public void setDeveloper(Developer developer) {
		this.developer = developer;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Boolean getIsHired() {
		return isHired;
	}

	public void setIsHired(Boolean isHired) {
		this.isHired = isHired;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
}
