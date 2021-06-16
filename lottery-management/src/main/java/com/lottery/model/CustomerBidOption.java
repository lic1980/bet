package com.lottery.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "customer_bid_option")
public class CustomerBidOption {
	@Id
	@Column(updatable = false)
	private Long id;
	
	@ManyToOne
    @JoinColumn(name="lottery_round_option_id")
	private LotteryRoundOption option;
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name="customer_bid_id")
	private CustomerBid customerBid;
	@Column
	private float odds;
	@Column
	private float fee;
	
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	public LotteryRoundOption getOption() {
		return option;
	}
	public void setOption(LotteryRoundOption option) {
		this.option = option;
	}
	public float getOdds() {
		return odds;
	}
	public void setOdds(float odds) {
		this.odds = odds;
	}
	public float getFee() {
		return fee;
	}
	public void setFee(float fee) {
		this.fee = fee;
	}
	public CustomerBid getCustomerBid() {
		return customerBid;
	}
	public void setCustomerBid(CustomerBid customerBid) {
		this.customerBid = customerBid;
	}
	
	
}
