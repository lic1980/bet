package com.lottery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lottery.exception.InvalidParameter;
import com.lottery.model.LotteryItemOption;
import com.lottery.model.LotteryRound;
import com.lottery.service.LotteryRoundService;

@RestController
@RequestMapping(value = "/api/v1")
public class LotteryRoundController {

	@Autowired
	private LotteryRoundService lotteryRoundService;

	@GetMapping(value = "/lotteries/rounds")
	public Page<LotteryRound> getLotteryRounds(@RequestParam(required = false, defaultValue = "ongoing") String status, @RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "20") int size) {
		return lotteryRoundService.findByStatus(status, page - 1, size);
	}
	
	@GetMapping(value = "/lotteries/{id}/rounds")
	public Page<LotteryRound> getLotteryRoundsByLottery(@PathVariable Long id, @RequestParam(required = false, defaultValue = "active") String status, @RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "20") int size) {
		return lotteryRoundService.findByLotteryAndStatus(id, status, page - 1, size);
	}
	
	@GetMapping(value = "/lotteries/{lid}/rounds/{rid}")
	public LotteryRound getLotteryRoundByLottery(@PathVariable Long lid, @PathVariable Long rid) {
		return lotteryRoundService.findById(rid).orElse(new LotteryRound());
	}
	
	@GetMapping(value = "/lotteries/rounds/{rid}")
	public LotteryRound getLotteryRound(@PathVariable Long rid) {
		return lotteryRoundService.findById(rid).orElse(new LotteryRound());
	}
	

	@GetMapping(value = "/lotteries/{lid}/rounds/{rid}/results")
	public List<LotteryItemOption> getLotteryRoundResultsById(@PathVariable Long rid) throws InvalidParameter {
		LotteryRound round = lotteryRoundService.findById(rid).orElseThrow(InvalidParameter::new);
		return round.getResults();
	}

}
