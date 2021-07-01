package com.lottery.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lottery.controller.LoginController;
import com.lottery.exception.InvalidParameter;
import com.lottery.model.CustomerBid;
import com.lottery.model.CustomerBidStatus;
import com.lottery.model.Lottery;
import com.lottery.model.LotteryRound;
import com.lottery.repo.LotteryRoundRepository;
import com.lottery.util.IdUtils;

@Service
public class LotteryRoundService {
	private static final Logger LOGGER = Logger.getLogger(LoginController.class);
	@Autowired
	private LotteryRoundRepository repo;
	@Autowired
	private CustomerBidService customerBidService;
	
	public List<LotteryRound> find() {
		return repo.findAll();
	}
	
	public Optional<LotteryRound> findById(Long id) {
		return repo.findById(id);
	}
	
	public Page<LotteryRound> findByStatus(String status, int pageNum, int pageSize) {
		PageRequest page = PageRequest.of(pageNum, pageSize);
		if ("ongoing".equals(status)) {
			return repo.findByCutOffTimeGreaterThan(new Date(), page);
		}
		if ("closed".equals(status)) {
			return repo.findByCutOffTimeLessThanAndResultsNull(new Date(), page);
		}
		return repo.findByResultsNotNull(page);
	}
	
	public Page<LotteryRound> findByLotteryAndStatus(Long lotteryId, String status, int pageNum, int pageSize) {
		PageRequest page = PageRequest.of(pageNum, pageSize);
		Lottery lottery = new Lottery();
		lottery.setId(lotteryId);
		if ("ongoing".equals(status)) {
			return repo.findByLotteryAndCutOffTimeGreaterThan(lottery, new Date(), page);
		} 
		return repo.findByLottery(lottery, page);
	}
	
	public LotteryRound saveOrUpdate(LotteryRound lotteryRound) {
		if (StringUtils.isEmpty(lotteryRound.getId())) {
			lotteryRound.setId(IdUtils.generateId());
		}
		return repo.save(lotteryRound);
	}
	
	public void settleRound(Long roundId) {
		int pageNum = 0;
		int pageSize = 100;
		Page<CustomerBid> page = customerBidService.findByLotteryRound(roundId, pageNum, pageSize);
		settleBid(page.getContent());
		pageNum ++;
		while (pageNum <= page.getTotalPages()) {
			page = customerBidService.findByLotteryRound(roundId, pageNum, pageSize);
			settleBid(page.getContent());
			pageNum ++;
		}

	}
	
	private void settleBid(List<CustomerBid> bids) {
		for (CustomerBid bid : bids) {
			try {
				if (bid.getStatus() == CustomerBidStatus.ACCEPTED) {
					customerBidService.settleCustomerBid(bid);
				}
			} catch (InvalidParameter e) {
				LOGGER.error("failed to settle bid", bid, e);
			}
		}
		
	}
}
