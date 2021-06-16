package com.lottery.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lottery.model.Lottery;
import com.lottery.model.LotteryRound;
import com.lottery.model.LotteryRoundTag;

public interface LotteryRoundRepository extends JpaRepository<LotteryRound, Long>{
	Page<LotteryRound> findByLotteryAndCutOffTimeGreaterThan(Lottery lottery, Date date, Pageable pageable);
	Page<LotteryRound> findByCutOffTimeGreaterThan(Date date, Pageable pageable);
	Page<LotteryRound> findByCutOffTimeLessThan(Date date, Pageable pageable);
	Page<LotteryRound> findByLottery(Lottery lottery, Pageable pageable);
	Page<LotteryRound> findByTagsIn(List<LotteryRoundTag> tags, Pageable pageable);
}
