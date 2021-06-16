package com.lottery.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
	private String message;
	@ManyToOne
    @JoinColumn(name="initiator_id")
	private Customer initiator;
	@ManyToOne
    @JoinColumn(name="recipient_id")
	private Customer recipient;
	@ManyToOne
    @JoinColumn(name="lottery_round_id")
	private LotteryRound lotteryRound;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "customer_bid_id") 
	private List<CustomerBidOption> options;

	
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
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
	public LotteryRound getLotteryRound() {
		return lotteryRound;
	}
	public void setLotteryRound(LotteryRound lotteryRound) {
		this.lotteryRound = lotteryRound;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public CustomerBidStatus getStatus() {
		return status;
	}
	public void setStatus(CustomerBidStatus status) {
		this.status = status;
	}
	
	public List<CustomerBidOption> getOptions() {
		return options;
	}
	public void setOptions(List<CustomerBidOption> options) {
		this.options = options;
	}

	public Date getBidTime() {
		return bidTime;
	}
	public void setBidTime(Date bidTime) {
		this.bidTime = bidTime;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
