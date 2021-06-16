package com.lottery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lottery.model.LotteryRoundTag;
import com.lottery.service.LotteryRoundTagService;

@RestController
@RequestMapping(value = "/api/v1")
public class LotteryRoundTagController {

	@Autowired
	private LotteryRoundTagService lotteryRoundTagService;

	@GetMapping(value = "/lotteries/rounds/tags")
	public Page<LotteryRoundTag> getLotteryRoundTags(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "20") int size) {
		return lotteryRoundTagService.findAll(page - 1, size);
	}
	
	@GetMapping(value = "/lotteries/rounds/{rid}/tags")
	public Page<LotteryRoundTag> getLotteryRoundTagsByRound(@PathVariable(required = false) Long rid, @RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "20") int size) {
		return lotteryRoundTagService.findByRound(rid, page - 1, size);
	}

}
