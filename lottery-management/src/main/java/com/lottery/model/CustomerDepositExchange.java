package com.lottery.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "customer_deposit_exchange")
public class CustomerDepositExchange {
	@Id
	@Column(updatable = false)
	private Long id;
	@Column
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date time;
	@ManyToOne
    @JoinColumn(name="customer_id")
	private Customer customer;
	@Column
	private float amount;
	@Column
	private CustomerDepositExchangeType type;
	@Column
	private Long reference;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public CustomerDepositExchangeType getType() {
		return type;
	}
	public void setType(CustomerDepositExchangeType type) {
		this.type = type;
	}
	public Long getReference() {
		return reference;
	}
	public void setReference(Long reference) {
		this.reference = reference;
	}

}
