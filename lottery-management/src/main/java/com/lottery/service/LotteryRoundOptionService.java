package com.lottery.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lottery.model.LotteryRound;
import com.lottery.model.LotteryRoundOption;
import com.lottery.repo.LotteryRoundOptionRepository;
import com.lottery.util.IdUtils;

@Service
public class LotteryRoundOptionService {
	@Autowired
	private LotteryRoundOptionRepository repo;
	
	public Optional<LotteryRoundOption> findById(Long id) {
		return repo.findById(id);
	}
	
	public List<LotteryRoundOption> findByLotteryRoundId(Long id) {
		LotteryRound round = new LotteryRound();
		round.setId(id);
		return repo.findByRound(round);
	}
	
	public LotteryRoundOption saveOrUpdate(LotteryRoundOption option) {
		if (StringUtils.isEmpty(option.getId())) {
			option.setId(IdUtils.generateId());
		}
		return repo.save(option);
	}
}
