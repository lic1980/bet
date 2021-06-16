package com.lottery.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lottery.model.CustomerBid;
import com.lottery.model.CustomerBidOption;
import com.lottery.repo.CustomerBidOptionRepository;
import com.lottery.util.IdUtils;

@Service
public class CustomerBidOptionService {
	@Autowired
	private CustomerBidOptionRepository repo;
	
	public List<CustomerBidOption> findByBid(CustomerBid bid) {
		return repo.findByCustomerBid(bid);
	}
	
	public List<CustomerBidOption> findByBids(List<CustomerBid> bids) {
		return repo.findByCustomerBidIn(bids);
	}
	
	public CustomerBidOption saveOrUpdate(CustomerBidOption option) {
		if (StringUtils.isEmpty(option.getId())) {
			option.setId(IdUtils.generateId());
		}
		return repo.save(option);
	}
}
