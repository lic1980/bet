package com.lottery.model;

import java.text.MessageFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "lottery_round_option")
public class LotteryRoundOption {
	@Id
	@Column(updatable = false)
	private Long id;
	@Column
	private float odds;
	@ManyToOne
    @JoinColumn(name="lottery_item_option_id")
	private LotteryItemOption option;
	@ManyToOne
    @JoinColumn(name="lottery_round_id")
	private LotteryRound round;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public float getOdds() {
		return odds;
	}
	public void setOdds(float odds) {
		this.odds = odds;
	}
	public LotteryItemOption getOption() {
		return option;
	}
	public void setOption(LotteryItemOption option) {
		this.option = option;
	}
	public LotteryRound getRound() {
		return round;
	}
	public void setRound(LotteryRound round) {
		this.round = round;
	}

	public String getOptionText( ) {
		String paramStr = getRound().getParameters();
		if (getOption().getText() != null && paramStr != null){
			return MessageFormat.format(getOption().getText(), paramStr.split(","));
		} else {
			return "";
		}
		
	}
}
