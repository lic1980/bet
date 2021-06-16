package com.lottery.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import com.lottery.model.CustomerDepositExchange;

public interface CustomerDepositRecordRepository extends JpaRepository<CustomerDepositExchange, Long>{
}
