package com.lottery.service;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lottery.exception.InvalidParameter;
import com.lottery.model.Customer;
import com.lottery.model.CustomerDepositExchange;
import com.lottery.repo.CustomerDepositRecordRepository;
import com.lottery.util.IdUtils;

@Service
public class CustomerDepositRecordService {
	@Autowired
	private CustomerDepositRecordRepository repo;
	@Autowired
	private CustomerService customerService;

	@Transactional(rollbackOn = {Exception.class})
	public CustomerDepositExchange changeCustomerDepositRecord(CustomerDepositExchange exchange) throws InvalidParameter {
		exchange.setId(IdUtils.generateId());
		exchange.setTime(new Date());
		CustomerDepositExchange record =  repo.save(exchange);
		Customer customer =  customerService.changeDepositByCustomerId(exchange.getCustomer().getId(), exchange.getAmount()).orElseThrow(InvalidParameter::new);
		if (customer.getDeposit() < 0 ) {
			throw new InvalidParameter();
		}
		return record;
	}
}
