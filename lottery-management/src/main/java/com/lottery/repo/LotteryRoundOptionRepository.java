package com.lottery.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lottery.model.LotteryRound;
import com.lottery.model.LotteryRoundOption;

public interface LotteryRoundOptionRepository extends JpaRepository<LotteryRoundOption, Long>{
	List<LotteryRoundOption> findByRound(LotteryRound round);
}
