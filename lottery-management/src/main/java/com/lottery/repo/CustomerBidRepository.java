package com.lottery.repo;


import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lottery.model.Agent;
import com.lottery.model.Customer;
import com.lottery.model.CustomerBid;
import com.lottery.model.CustomerBidStatus;
import com.lottery.model.LotteryRound;

public interface CustomerBidRepository extends JpaRepository<CustomerBid, Long>{
	Page<CustomerBid> findByInitiatorNotAndRecipientIsNull(Customer cus, Pageable pageable);
	Page<CustomerBid> findByInitiator(Customer cus, Pageable pageable);
	Page<CustomerBid> findByRecipient(Customer cus, Pageable pageable);
	Page<CustomerBid> findByOptionRound(LotteryRound round, Pageable pageable);
	Page<CustomerBid> findByInitiatorAgentAndStatusIn(Agent agent, List<CustomerBidStatus> statuses, Pageable pageable);
	Page<CustomerBid> findByInitiatorNotAndStatusIn(Customer cus, List<CustomerBidStatus> statuses, Pageable pageable);
	
	@Modifying
	@Query("UPDATE CustomerBid b SET b.status=?2 WHERE b.id=?1 AND b.status=?3")
	int updateCustomerBidStatusById(Long id, CustomerBidStatus newStatus, CustomerBidStatus oldStatus);
	@Modifying
	@Query("UPDATE CustomerBid b SET b.recipient=?2, b.status=1, b.bidTime=?3 where b.id=?1 AND b.status=0 AND b.recipient=null")
	int updateCustomerBidRecipientById(Long id, Customer recipient, Date date);
}
