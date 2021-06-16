package com.lottery.service;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lottery.model.Customer;
import com.lottery.model.CustomerDepositExchange;
import com.lottery.repo.CustomerDepositRecordRepository;
import com.lottery.util.IdUtils;

@Service
public class CustomerExchangeService {
	@Autowired
	private CustomerDepositRecordRepository repo;
	@Autowired
	private CustomerService customerService;

	@Transactional(rollbackOn = {RuntimeException.class, Error.class})
	public CustomerDepositExchange addCustomerDepositRecord(CustomerDepositExchange deposit) {
		if (StringUtils.isEmpty(deposit.getId())) {
			deposit.setId(IdUtils.generateId());
		}
		deposit.setTime(new Date());
		CustomerDepositExchange record =  repo.save(deposit);
		Customer customer = record.getCustomer();
		customer.setDeposit(customer.getDeposit() + deposit.getAmount());
		customerService.saveOrUpdate(customer);
		return record;
	}
}
