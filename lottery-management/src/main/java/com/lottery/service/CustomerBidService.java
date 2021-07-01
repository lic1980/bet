package com.lottery.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.lottery.exception.InvalidParameter;
import com.lottery.model.Agent;
import com.lottery.model.Customer;
import com.lottery.model.CustomerBid;
import com.lottery.model.CustomerBidResult;
import com.lottery.model.CustomerBidStatus;
import com.lottery.model.CustomerDepositExchange;
import com.lottery.model.CustomerDepositExchangeType;
import com.lottery.model.LotteryItemOption;
import com.lottery.model.LotteryRound;
import com.lottery.model.LotteryRoundOption;
import com.lottery.repo.CustomerBidRepository;
import com.lottery.util.IdUtils;

@Service
public class CustomerBidService {
	@Autowired
	private CustomerBidRepository repo;
	@Autowired
	private CustomerDepositRecordService customerDepositRecordService;
	
	public Optional<CustomerBid> findById(Long id) {
		return repo.findById(id);
	}
	
	@Transactional(rollbackOn = {Exception.class})
	public CustomerBid cancelBid(Long bid) throws InvalidParameter {
		int num =  repo.updateCustomerBidStatusById(bid, CustomerBidStatus.CANCEL, CustomerBidStatus.NEW);
		if (num == 0) {
			throw new InvalidParameter();
		}
		CustomerBid customerBid = repo.findById(bid).orElseThrow(InvalidParameter::new);
		CustomerDepositExchange exchange = new CustomerDepositExchange();
		exchange.setAmount(customerBid.getFee());
		exchange.setCustomer(customerBid.getInitiator());
		exchange.setType(CustomerDepositExchangeType.WITHDRAW);
		exchange.setReference(customerBid.getId());
		customerDepositRecordService.changeCustomerDepositRecord(exchange);

		return customerBid;
	}
	
	@Transactional(rollbackOn = {Exception.class})
	public CustomerBid acceptBid(Long bid, Customer recipient) throws InvalidParameter {
		int num = repo.updateCustomerBidRecipientById(bid, recipient);
		if ( num == 0) {
			throw new InvalidParameter();
		}
		CustomerBid customerBid = repo.findById(bid).orElseThrow(InvalidParameter::new);
		CustomerDepositExchange exchange = new CustomerDepositExchange();
		exchange.setAmount(0 - customerBid.getFee());
		exchange.setCustomer(customerBid.getRecipient());
		exchange.setType(CustomerDepositExchangeType.ACCEPT);
		exchange.setReference(customerBid.getId());
		customerDepositRecordService.changeCustomerDepositRecord(exchange);

		return customerBid;
	}
	
	@Transactional(rollbackOn = {Exception.class})
	public CustomerBid createCustomerBid(CustomerBid bid) throws InvalidParameter {
		bid.setId(IdUtils.generateId());
		bid.setStatus(CustomerBidStatus.NEW);
		bid.setCreateTime(new Date());
		
		CustomerDepositExchange exchange = new CustomerDepositExchange();

		CustomerBid result = repo.save(bid);
		
		exchange.setAmount(0 - bid.getFee());
		exchange.setCustomer(bid.getInitiator());
		exchange.setType(CustomerDepositExchangeType.BID);
		exchange.setReference(bid.getId());
		customerDepositRecordService.changeCustomerDepositRecord(exchange);
		
		return result;
	}
	
	@Transactional(rollbackOn = {Exception.class})
	public CustomerBid settleCustomerBid(CustomerBid bid) throws InvalidParameter {

		float awardInitiator = 0;
		float awardRecipent = 0;
		float serviceCharge = 0 ;
		List<LotteryItemOption> results = getResult(bid);
		if(isWin(bid.getOption(), results)) {
			awardInitiator = bid.getFee() * bid.getOdds();
			bid.setResult(CustomerBidResult.WIN);
		} else {
			awardRecipent = bid.getFee() + bid.getFee() * bid.getOdds();
			bid.setResult(CustomerBidResult.LOSE);
		}
		if(awardInitiator > 0) {
			CustomerDepositExchange exchangeInitiator = new CustomerDepositExchange();
			exchangeInitiator.setAmount(awardInitiator);
			exchangeInitiator.setCustomer(bid.getInitiator());
			exchangeInitiator.setType(CustomerDepositExchangeType.AWARD);
			exchangeInitiator.setReference(bid.getId());
			customerDepositRecordService.changeCustomerDepositRecord(exchangeInitiator);
		}
		
		if(awardRecipent > 0 ) {
			CustomerDepositExchange exchangeRecipent = new CustomerDepositExchange();
			exchangeRecipent.setAmount(awardRecipent);
			exchangeRecipent.setCustomer(bid.getRecipient());
			exchangeRecipent.setType(CustomerDepositExchangeType.MARGIN);
			exchangeRecipent.setReference(bid.getId());
			customerDepositRecordService.changeCustomerDepositRecord(exchangeRecipent);
		}
		bid.setStatus(CustomerBidStatus.SETTLED);
		bid.setServiceCharge(serviceCharge);
		bid = repo.save(bid);
		return bid;
	}
	
	private List<LotteryItemOption> getResult(CustomerBid bid) {
		return bid.getOption().getRound().getResults();
	}
	
	private boolean isWin(LotteryRoundOption option, List<LotteryItemOption> results) {
		Long bidOptionId = option.getOption().getId();
		for (LotteryItemOption result : results) {
			if (bidOptionId.equals(result.getId())) {
				return true;
			}
		}
		return false;
	}
	
	public Page<CustomerBid> findAvailable(Long cusId, int pageNum, int pageSize) {
		PageRequest page = PageRequest.of(pageNum, pageSize);
		Customer cus = new Customer();
		cus.setId(cusId);
		return repo.findByInitiatorNotAndRecipientIsNull(cus, page);
	}
	
	public Page<CustomerBid> findByInitiator(Long cusId, int pageNum, int pageSize) {
		PageRequest page = PageRequest.of(pageNum, pageSize);
		Customer cus = new Customer();
		cus.setId(cusId);
		return repo.findByInitiator(cus, page);
	}
	
	public Page<CustomerBid> findByRecipient(Long cusId, int pageNum, int pageSize) {
		PageRequest page = PageRequest.of(pageNum, pageSize);
		Customer cus = new Customer();
		cus.setId(cusId);
		return repo.findByRecipient(cus, page);
	}
	
	public Page<CustomerBid> findByCustomerAgentAndStatus(Long agentId, List<CustomerBidStatus> statuses, int pageNum, int pageSize) {
		PageRequest page = PageRequest.of(pageNum, pageSize);
		Agent agent = new Agent();
		agent.setId(agentId);
		return repo.findByInitiatorAgentAndStatusIn(agent, statuses, page);
	}
	
	public Page<CustomerBid> findByLotteryRound(Long roundId, int pageNum, int pageSize){
		PageRequest page = PageRequest.of(pageNum, pageSize);
		LotteryRound round = new LotteryRound();
		round.setId(roundId);
		return repo.findByOptionRound(round, page);
	}
}
