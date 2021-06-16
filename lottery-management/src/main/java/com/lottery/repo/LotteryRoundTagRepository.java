package com.lottery.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lottery.model.LotteryRound;
import com.lottery.model.LotteryRoundTag;

public interface LotteryRoundTagRepository extends JpaRepository<LotteryRoundTag, Long> {
	Page<LotteryRoundTag> findByLotteryRoundsIn(List<LotteryRound> lotteryRounds, Pageable pageable);
}
