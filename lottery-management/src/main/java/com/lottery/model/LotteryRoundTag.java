package com.lottery.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "lottery_round_tag")
public class LotteryRoundTag {
	@Id
	@Column(updatable = false)
	private Long id;
	@Column
	private String text;
	@JsonIgnore
	@ManyToMany
	@JoinTable(name="lottery_round_lottery_round_tag",  joinColumns = { @JoinColumn(name = "lottery_round_tag_id")}, inverseJoinColumns = {
            @JoinColumn(name = "lottery_round_id") })
	private List<LotteryRound> lotteryRounds;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<LotteryRound> getLotteryRounds() {
		return lotteryRounds;
	}
	public void setLotteryRounds(List<LotteryRound> lotteryRounds) {
		this.lotteryRounds = lotteryRounds;
	}
	
	
}
