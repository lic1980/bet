package com.lottery.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lottery.model.LotteryRound;
import com.lottery.model.LotteryRoundTag;
import com.lottery.repo.LotteryRoundTagRepository;
import com.lottery.util.IdUtils;

@Service
public class LotteryRoundTagService {
	@Autowired
	private LotteryRoundTagRepository repo;
	
	public Page<LotteryRoundTag> findAll(int pageNum, int pageSize) {
		PageRequest page = PageRequest.of(pageNum, pageSize);
		return repo.findAll(page);
	}
	
	public Page<LotteryRoundTag> findByRound(Long id, int pageNum, int pageSize) {
		PageRequest page = PageRequest.of(pageNum, pageSize);
		List<LotteryRound> rounds = new ArrayList<>();
		LotteryRound round = new LotteryRound();
		round.setId(id);
		rounds.add(round );
		return repo.findByLotteryRoundsIn(rounds, page);
	}
	
	public LotteryRoundTag saveOrUpdate(LotteryRoundTag tag) {
		if (StringUtils.isEmpty(tag.getId())) {
			tag.setId(IdUtils.generateId());
		}
		return repo.save(tag);
	}
	
	public void delete(long id) {
		repo.deleteById(id);
	}
}
