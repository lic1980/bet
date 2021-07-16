package com.lottery.controller;

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lottery.exception.InternalServiceException;
import com.lottery.exception.InvalidParameter;
import com.lottery.model.Agent;
import com.lottery.model.Customer;
import com.lottery.model.CustomerBid;
import com.lottery.model.CustomerBidStatus;
import com.lottery.model.CustomerDepositExchange;
import com.lottery.service.AgentService;
import com.lottery.service.CustomerBidService;
import com.lottery.service.CustomerDepositRecordService;
import com.lottery.service.CustomerService;

@RestController
@RequestMapping(value = "/api/v1")
public class AgentController {
	private static final Logger LOGGER = Logger.getLogger(AgentController.class);
	
	private static final int DEPOSIT_MAX = 5000;
	private static final int DEPOSIT_MIN = -5000;
	@Autowired
	private AgentService agentService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private CustomerBidService customerBidService;
	@Autowired
	private CustomerDepositRecordService customerDepositRecordService;

	@GetMapping(value = "/agents/{id}")
	public Agent getAgent(@PathVariable Long id) {
		return agentService.findById(id).orElse(new Agent());
	}

	@GetMapping(value = "/agents/{id}/bids")
	public Page<CustomerBid> getAgentBids(@PathVariable Long id, @RequestParam String status, @RequestParam int page,
			@RequestParam int size) {
		List<CustomerBidStatus> statuses = new ArrayList<>();
		if ("history".equals(status)) {
			statuses.add(CustomerBidStatus.CANCEL);
			statuses.add(CustomerBidStatus.REJECTED);
			statuses.add(CustomerBidStatus.SETTLED);
		} else {
			statuses.add(CustomerBidStatus.NEW);
			statuses.add(CustomerBidStatus.ACCEPTED);
		}
		return customerBidService.findByInitiatorAgentAndStatus(id, statuses, page - 1, size);
	}

	@GetMapping(value = "/agents/{id}/customers")
	public Page<Customer> getCustomers(@PathVariable(value = "id") Long agentId, @RequestParam int page,
			@RequestParam int size) {
		return customerService.findByAgent(agentId, page - 1, size);
	}
	
	@PostMapping(value = "/agents/{id}/customers")
	public Customer addCustomer(@RequestBody Customer customer) throws InvalidParameter, InternalServiceException {
		if (StringUtils.isEmpty(customer.getTel())) {
			throw new InvalidParameter();
		}
		if (customerService.findByTel(customer.getTel()).isPresent()) {
			throw new InvalidParameter();
		}
		if (StringUtils.isEmpty(customer.getPlainPassword())) {
			customer.setPlainPassword(customer.getTel());
		}
		return customerService.saveOrUpdate(customer);
	}
	
	@PatchMapping(value = "/agents/{id}/customers/{aid}")
	public Customer patchCustomer(@PathVariable Long aid, @RequestBody Customer customer) throws InvalidParameter, InternalServiceException {
		if (StringUtils.isEmpty(aid)) {
			throw new InvalidParameter();
		}
		Customer target =  customerService.findById(aid).orElseThrow(InvalidParameter::new);
		
		if (!StringUtils.isEmpty(customer.getNewPlainPassword())) {
			target.setPlainPassword(customer.getNewPlainPassword());
		}
		return customerService.saveOrUpdate(target);
	}

	@PostMapping(value = "/agents/{id}/customers/{customerId}/exchanges")
	public CustomerDepositExchange changeCustomerBid(@PathVariable(value = "customerId") String customerId,
			@RequestBody CustomerDepositExchange exchange) throws InvalidParameter {

		if (exchange.getAmount() < DEPOSIT_MIN || exchange.getAmount() > DEPOSIT_MAX) {
			throw new InvalidParameter();
		}

		if (exchange.getReference() == null || exchange.getCustomer() == null) {
			throw new InvalidParameter();
		}

		Agent agent = agentService.findById(exchange.getReference()).orElseThrow(InvalidParameter::new);
		Customer customer = customerService.findById(exchange.getCustomer().getId()).orElseThrow(InvalidParameter::new);
		if (!customer.getAgent().getId().equals(agent.getId())) {
			throw new InvalidParameter();
		}
		if (customer.getDeposit() + exchange.getAmount() < 0) {
			throw new InvalidParameter();
		}
		return customerDepositRecordService.changeCustomerDepositRecord(exchange);
	}
}
