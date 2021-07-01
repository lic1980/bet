package com.lottery.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "customer_bid")
public class CustomerBid {
	@Id
	@Column(updatable = false)
	private Long id;
	@Column
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date createTime;
	@Column
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date bidTime;
	@Column
	@Enumerated(EnumType.ORDINAL)
	private CustomerBidStatus status;
	@Column
	@Enumerated(EnumType.ORDINAL)
	private CustomerBidResult result;
	@Column
	private Float odds;
	@Column
	private Float fee;
	@Column
	private Float serviceCharge;
	@Column
	private Float exchangeAmount;
	@Column
	private String message;
	@ManyToOne
    @JoinColumn(name="lottery_round_option_id")
	private LotteryRoundOption option;
	@ManyToOne
    @JoinColumn(name="initiator_id")
	private Customer initiator;
	@ManyToOne
    @JoinColumn(name="recipient_id")
	private Customer recipient;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getBidTime() {
		return bidTime;
	}
	public void setBidTime(Date bidTime) {
		this.bidTime = bidTime;
	}
	public CustomerBidStatus getStatus() {
		return status;
	}
	public void setStatus(CustomerBidStatus status) {
		this.status = status;
	}
	public CustomerBidResult getResult() {
		return result;
	}
	public void setResult(CustomerBidResult result) {
		this.result = result;
	}
	public Float getOdds() {
		return odds;
	}
	public void setOdds(Float odds) {
		this.odds = odds;
	}
	public Float getFee() {
		return fee;
	}
	public void setFee(Float fee) {
		this.fee = fee;
	}
	public Float getServiceCharge() {
		return serviceCharge;
	}
	public void setServiceCharge(Float serviceCharge) {
		this.serviceCharge = serviceCharge;
	}
	public Float getExchangeAmount() {
		return exchangeAmount;
	}
	public void setExchangeAmount(Float exchangeAmount) {
		this.exchangeAmount = exchangeAmount;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public LotteryRoundOption getOption() {
		return option;
	}
	public void setOption(LotteryRoundOption option) {
		this.option = option;
	}
	public Customer getInitiator() {
		return initiator;
	}
	public void setInitiator(Customer initiator) {
		this.initiator = initiator;
	}
	public Customer getRecipient() {
		return recipient;
	}
	public void setRecipient(Customer recipient) {
		this.recipient = recipient;
	}

	
	
	
	
}
