package com.lottery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lottery.model.LotteryRoundOption;
import com.lottery.service.LotteryRoundOptionService;

@RestController
@RequestMapping(value = "/api/v1")
public class LotteryRoundOptionController {
	@Autowired
	private LotteryRoundOptionService lotteryRoundOptionService;
	
	@GetMapping(value = "/lotteries/rounds/{rid}/options")
	public List<LotteryRoundOption> getLotteryRoundOptions(@PathVariable Long rid) {
		return lotteryRoundOptionService.findByLotteryRoundId(rid);
	}
}
