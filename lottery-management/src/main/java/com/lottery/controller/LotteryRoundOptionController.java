package com.lottery.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lottery.exception.InvalidParameter;
import com.lottery.model.LotteryItemOption;
import com.lottery.model.LotteryRound;
import com.lottery.model.LotteryRoundOption;
import com.lottery.service.LotteryRoundOptionService;
import com.lottery.service.LotteryRoundService;

@RestController
@RequestMapping(value = "/api/v1")
public class LotteryRoundOptionController {
	@Autowired
	private LotteryRoundOptionService lotteryRoundOptionService;
	@Autowired
	private LotteryRoundService lotteryRoundService;
	
	@GetMapping(value = "/lotteries/rounds/{rid}/options")
	public List<LotteryRoundOption> getLotteryRoundOptions(@RequestParam(required = false, defaultValue = "all") String scope, @PathVariable Long rid) throws InvalidParameter {
		if ("all".equals(scope)) {
			return lotteryRoundOptionService.findByLotteryRoundId(rid);
		} else {
			LotteryRound round = lotteryRoundService.findById(rid).orElseThrow(InvalidParameter::new);
			List<LotteryItemOption>  results = round.getResults();
			List<LotteryRoundOption>  options = round.getOptions();
			List<LotteryRoundOption> matchedOptions = new ArrayList<>() ;
			if (results == null || options == null) {
				return new ArrayList<>();
			}
			for (LotteryItemOption result : results) {
				for (LotteryRoundOption option : options) {
					if (result.getId() == option.getOption().getId()) {
						matchedOptions.add(option);
					}
				}
			}
			return matchedOptions;
		}
		
	}
	
}
