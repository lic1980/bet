package com.lottery.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lottery.exception.InvalidParameter;
import com.lottery.model.Customer;
import com.lottery.model.CustomerBid;
import com.lottery.model.CustomerBidOption;
import com.lottery.model.CustomerBidStatus;
import com.lottery.model.LotteryRound;
import com.lottery.model.LotteryRoundOption;
import com.lottery.service.CustomerBidOptionService;
import com.lottery.service.CustomerBidService;
import com.lottery.service.CustomerService;
import com.lottery.service.LotteryRoundService;

@RestController
@RequestMapping(value = "/api/v1")
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private CustomerBidService customerBidService;
	@Autowired
	private CustomerBidOptionService customerBidOptionService;
	@Autowired
	private LotteryRoundService lotteryRoundService;

	@GetMapping(value = "/customers/{id}")
	public Customer getCustomer(@PathVariable Long id) {
		return customerService.findById(id).orElse(new Customer());
	}
	
	@GetMapping(value = "/customers/{id}/bids/options")
	public List<CustomerBidOption> getCustomerBidOptions(@RequestParam List<Long> bidIds) {
		List<CustomerBid>  bids = new ArrayList<>();
				
		for(Long id: bidIds) {
			CustomerBid bid = new CustomerBid();
			bid.setId(id);
			bids.add(bid);
		}
		return customerBidOptionService.findByBids(bids);
	}

	@GetMapping(value = "/customers/{id}/bids")
	public Page<CustomerBid> getCustomerBids(@PathVariable Long id, @RequestParam(defaultValue="none") String role, @RequestParam int page, @RequestParam int size) {
		if ("recipient".equals(role)) {
			return customerBidService.findByRecipient(id, page - 1, size);
		} 
		if ("initiator".equals(role))  {
			return customerBidService.findByInitiator(id, page - 1, size);
		}
		return customerBidService.findAvailable(id, page - 1, size);
	}
	
	@GetMapping(value = "/customers/{cid}/bids/{bid}")
	public CustomerBid getCustomerBids(@PathVariable Long cid, @PathVariable Long bid) throws InvalidParameter {
		return customerBidService.findById(bid).orElseThrow(InvalidParameter::new);
	}
	
	@GetMapping(value = "/customers/{cid}/bids/{bid}/options")
	public List<CustomerBidOption> getCustomerBidOptions(@PathVariable Long cid, @PathVariable Long bid) throws InvalidParameter {
		CustomerBid customerBid = new CustomerBid();
		customerBid.setId(bid);
		return customerBidOptionService.findByBid(customerBid);
	}
	
	@PatchMapping(value = "/customers/{cid}/bids/{bid}")
	public CustomerBid cancelOrAcceptBid(@PathVariable Long cid, @PathVariable Long bid, @RequestBody CustomerBid requestBid) throws InvalidParameter {
		
		if (requestBid.getRecipient() == null && requestBid.getStatus() == CustomerBidStatus.CANCEL) {
			return customerBidService.cancelBid(bid);
		}
		if (requestBid.getRecipient() != null && requestBid.getStatus() == CustomerBidStatus.ACCEPTED) {
			Customer recipient = new Customer();
			recipient.setId(cid);
			return customerBidService.acceptBid(bid, recipient);
		}
		throw new InvalidParameter();
	}

	@PostMapping(value = "/customers/{id}/bids")
	public CustomerBid addCustomerBid(@PathVariable(value = "id") Long customerId, @RequestBody CustomerBid bid)
			throws InvalidParameter {
		if (bid.getLotteryRound() == null) {
			throw new InvalidParameter();
		}
		if (bid.getOptions() == null || bid.getOptions().isEmpty()) {
			throw new InvalidParameter();
		}

		LotteryRound lotteryRound = lotteryRoundService.findById(bid.getLotteryRound().getId())
				.orElseThrow(InvalidParameter::new);
		List<LotteryRoundOption> options = lotteryRound.getOptions();

		float totalFee = 0;
		for (CustomerBidOption optionPassed : bid.getOptions()) {
			totalFee = totalFee + optionPassed.getFee();
			LotteryRoundOption option = findOption(options, optionPassed.getOption().getId()).orElseThrow(InvalidParameter::new);

			optionPassed.setOption(option);
		}

		Customer customer = customerService.findById(customerId).orElseThrow(InvalidParameter::new);
		if (customer.getDeposit() - totalFee < 0) {
			throw new InvalidParameter();
		}
		bid.setInitiator(customer);
		return customerBidService.createCustomerBid(bid);
	}

	private Optional<LotteryRoundOption> findOption(List<LotteryRoundOption> options, Long optionId) {
		for (LotteryRoundOption option : options) {
			if (option.getId().equals(optionId)) {
				return Optional.of(option);
			}
		}
		return Optional.empty();
	}
}
