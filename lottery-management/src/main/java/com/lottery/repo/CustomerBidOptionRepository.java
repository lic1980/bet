package com.lottery.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lottery.model.CustomerBid;
import com.lottery.model.CustomerBidOption;

public interface CustomerBidOptionRepository extends JpaRepository<CustomerBidOption, Long>{
	List<CustomerBidOption> findByCustomerBid(CustomerBid bid);
	List<CustomerBidOption> findByCustomerBidIn(List<CustomerBid> bid);
}
