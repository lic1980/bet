package com.lottery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lottery.exception.InvalidParameter;
import com.lottery.model.LotteryItemOption;
import com.lottery.model.LotteryRound;
import com.lottery.service.LotteryRoundService;

@RestController
@RequestMapping(value = "/api/v1")
public class LotteryRoundResultController {

	@Autowired
	private LotteryRoundService lotteryRoundService;

	@GetMapping(value = "/lotteries/rounds/{rid}/results")
	public List<LotteryItemOption> getLotteryRoundResults(@PathVariable Long rid) throws InvalidParameter {
		return lotteryRoundService.findById(rid).orElseThrow(InvalidParameter::new).getResults();
	}
	
	@PostMapping(value = "/lotteries/rounds/{rid}/results")
	public LotteryItemOption postLotteryRoundResult(@PathVariable Long rid, @RequestBody LotteryItemOption option) throws InvalidParameter {
		LotteryRound lotteryRound = lotteryRoundService.findById(rid).orElseThrow(InvalidParameter::new);
		lotteryRound.getResults().add(option);
		lotteryRoundService.saveOrUpdate(lotteryRound);
		return option;
	}
}
