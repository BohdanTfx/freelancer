package com.epam.freelancer.database.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.epam.freelancer.database.transformer.annotation.Column;
import com.epam.freelancer.database.transformer.annotation.Id;
import com.epam.freelancer.database.transformer.annotation.Table;

/**
 * Created by ������ on 16.01.2016.
 */
@Table(name = "ordering")
public class Ordering implements BaseEntity<Integer> {
	@Id
	private Integer id;
	@Column
	private String title;
	@Column(name = "pay_type")
	private String payType;
	@Column
	private String descr;
	@Column(name = "customer_id")
	private Integer customerId;
	@Column
	private Timestamp date;
	@Column
	private Double payment;
	@Column
	private Boolean started;
	@Column(name = "started_date")
	private Timestamp startedDate;
	@Column
	private Boolean ended;
	@Column(name = "ended_date")
	private Timestamp endedDate;
	@Column(name = "private")
	private Boolean priv;
	@Column(name = "is_deleted")
	private Boolean isDeleted;
	@Column
	private Integer version;
	@Column
	private Integer zone;
	private List<Technology> technologies = new ArrayList<>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public Double getPayment() {
		return payment;
	}

	public void setPayment(Double payment) {
		this.payment = payment;
	}

	public Boolean getStarted() {
		return started;
	}

	public void setStarted(Boolean started) {
		this.started = started;
	}

	public Timestamp getStartedDate() {
		return startedDate;
	}

	public void setStartedDate(Timestamp startedDate) {
		this.startedDate = startedDate;
	}

	public Boolean getEnded() {
		return ended;
	}

	public void setEnded(Boolean ended) {
		this.ended = ended;
	}

	public Timestamp getEndedDate() {
		return endedDate;
	}

	public void setEndedDate(Timestamp endedDate) {
		this.endedDate = endedDate;
	}

	public Boolean getPriv() {
		return priv;
	}

	public void setPriv(Boolean priv) {
		this.priv = priv;
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

		Ordering ordering = (Ordering) o;

		if (title != null ? !title.equals(ordering.title)
				: ordering.title != null)
			return false;
		if (customerId != null ? !customerId.equals(ordering.customerId)
				: ordering.customerId != null)
			return false;
		if (date != null ? !date.equals(ordering.date) : ordering.date != null)
			return false;
		return !(isDeleted != null ? !isDeleted.equals(ordering.isDeleted)
				: ordering.isDeleted != null);

	}

	@Override
	public int hashCode() {
		int result = title != null ? title.hashCode() : 0;
		result = 31 * result + (customerId != null ? customerId.hashCode() : 0);
		result = 31 * result + (date != null ? date.hashCode() : 0);
		result = 31 * result + (isDeleted != null ? isDeleted.hashCode() : 0);
		return result;
	}

	public Integer getZone() {
		return zone;
	}

	public void setZone(Integer zone) {
		this.zone = zone;
	}

	public List<Technology> getTechnologies() {
		return technologies;
	}

	public void setTechnologies(List<Technology> technologies) {
		this.technologies = technologies;
	}
}
