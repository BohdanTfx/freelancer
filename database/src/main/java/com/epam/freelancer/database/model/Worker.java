package com.epam.freelancer.database.model;

/**
 * Created by ������ on 16.01.2016.
 */

import com.epam.freelancer.database.transformer.annotation.Column;
import com.epam.freelancer.database.transformer.annotation.Id;
import com.epam.freelancer.database.transformer.annotation.Table;

@Table(name = "worker")
public class Worker implements BaseEntity<Integer> {
	@Id
	private Integer id;
	@Column(name = "order_id")
	private Integer orderId;
	@Column(name = "dev_id")
	private Integer devId;
	@Column(name = "new_hourly")
	private Double newHourly;
	@Column(name = "sum_hours")
	private Double sumHours;
	@Column(name = "is_deleted")
	private Boolean isDeleted;
	@Column
	private Boolean accepted;
	@Column
	private Integer version;
	private Ordering order;
	private Developer developer;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getDevId() {
		return devId;
	}

	public void setDevId(Integer devId) {
		this.devId = devId;
	}

	public Double getNewHourly() {
		return newHourly;
	}

	public void setNewHourly(Double newHourly) {
		this.newHourly = newHourly;
	}

	public Double getSumHours() {
		return sumHours;
	}

	public void setSumHours(Double sumHours) {
		this.sumHours = sumHours;
	}

	public Boolean getAccepted() {
		return accepted;
	}

	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
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

		Worker worker = (Worker) o;

		if (orderId != null ? !orderId.equals(worker.orderId)
				: worker.orderId != null)
			return false;
		if (devId != null ? !devId.equals(worker.devId) : worker.devId != null)
			return false;
		return !(isDeleted != null ? !isDeleted.equals(worker.isDeleted)
				: worker.isDeleted != null);

	}

	@Override
	public int hashCode() {
		int result = orderId != null ? orderId.hashCode() : 0;
		result = 31 * result + (devId != null ? devId.hashCode() : 0);
		result = 31 * result + (isDeleted != null ? isDeleted.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Worker{" + "id=" + id + ", orderId=" + orderId + ", devId="
				+ devId + ", newHourly=" + newHourly + ", sumHours=" + sumHours
				+ ", isDeleted=" + isDeleted + ", version=" + version + '}';
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
}
