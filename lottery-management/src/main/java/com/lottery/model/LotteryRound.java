package com.lottery.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "lottery_round")
public class LotteryRound {
	@Id
	@Column(updatable = false)
	private Long id;
	@Column
	private String title;
	@Column
	private String description;
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date cutOffTime;
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date openingTime;
	@Column
	private String parameters;
	@ManyToOne
	@JoinColumn(name="lottery_id")
	private Lottery lottery;
	@JsonIgnore
	@ManyToMany
	@JoinTable(name="lottery_round_result",  joinColumns = { @JoinColumn(name = "lottery_round_id")}, inverseJoinColumns = {
            @JoinColumn(name = "lottery_item_option_id") })
	private List<LotteryItemOption> results;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.REMOVE)
	@JoinColumn(name="lottery_round_id")
	private List<LotteryRoundOption> options;
	@JsonIgnore
	@ManyToMany
	@JoinTable(name="lottery_round_lottery_round_tag",  joinColumns = { @JoinColumn(name = "lottery_round_id")}, inverseJoinColumns = {
            @JoinColumn(name = "lottery_round_tag_id") })
	private List<LotteryRoundTag> tags;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Date getCutOffTime() {
		return cutOffTime;
	}
	public void setCutOffTime(Date cutOffTime) {
		this.cutOffTime = cutOffTime;
	}
	public Date getOpeningTime() {
		return openingTime;
	}
	public void setOpeningTime(Date openingTime) {
		this.openingTime = openingTime;
	}
	
	public List<LotteryItemOption> getResults() {
		return results;
	}
	public void setResults(List<LotteryItemOption> results) {
		this.results = results;
	}
	public List<LotteryRoundOption> getOptions() {
		return options;
	}
	public void setOptions(List<LotteryRoundOption> options) {
		this.options = options;
	}
	public Lottery getLottery() {
		return lottery;
	}
	public void setLottery(Lottery lottery) {
		this.lottery = lottery;
	}
	public String getParameters() {
		return parameters;
	}
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<LotteryRoundTag> getTags() {
		return tags;
	}
	public void setTags(List<LotteryRoundTag> tags) {
		this.tags = tags;
	}
	
}
