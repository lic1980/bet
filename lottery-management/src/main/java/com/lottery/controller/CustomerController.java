package com.lottery.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.lottery.model.CustomerBidStatus;
import com.lottery.model.LotteryRoundOption;
import com.lottery.service.CustomerBidService;
import com.lottery.service.CustomerService;
import com.lottery.service.LotteryRoundOptionService;

@RestController
@RequestMapping(value = "/api/v1")
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private CustomerBidService customerBidService;
	@Autowired
	private LotteryRoundOptionService lotteryRoundOptionService;

	@GetMapping(value = "/customers/{id}")
	public Customer getCustomer(@PathVariable Long id) {
		return customerService.findById(id).orElse(new Customer());
	}

	@GetMapping(value = "/customers/{id}/bids")
	public Page<CustomerBid> getCustomerBids(@PathVariable Long id, @RequestParam(defaultValue="none") String role, @RequestParam int page, @RequestParam int size) {
		if ("recipient".equals(role)) {
			return customerBidService.findByRecipient(id, page - 1, size);
		} 
		return customerBidService.findByInitiator(id, page - 1, size);
	}
	
	@GetMapping(value = "/customers/{id}/customers/bids")
	public Page<CustomerBid> getOtherCustomerBids(@PathVariable Long id, @RequestParam(defaultValue="none") String role, @RequestParam int page, @RequestParam int size) {
		List<CustomerBidStatus> statuses = new ArrayList<>();
		statuses.add(CustomerBidStatus.NEW);
		return customerBidService.findByInitiatorNotAndStatus(id, statuses, page - 1, size);
	}
	
	@GetMapping(value = "/customers/{cid}/bids/{bid}")
	public CustomerBid getCustomerBids(@PathVariable Long cid, @PathVariable Long bid) throws InvalidParameter {
		return customerBidService.findById(bid).orElseThrow(InvalidParameter::new);
	}

	@PatchMapping(value = "/customers/{cid}/bids/{bidId}")
	public CustomerBid cancelOrAcceptBid(@PathVariable Long cid, @PathVariable Long bidId, @RequestBody CustomerBid requestBid) throws InvalidParameter {
		CustomerBid bid = customerBidService.findById(bidId).orElseThrow(InvalidParameter::new);

		if (cid.equals(bid.getInitiator().getId()) && bid.getRecipient() == null && requestBid.getStatus() == CustomerBidStatus.CANCEL) {
			return customerBidService.cancelBid(bidId);
		}
		if (bid.getRecipient() == null && requestBid.getStatus() == CustomerBidStatus.ACCEPTED) {
			Customer recipient = customerService.findById(cid).orElseThrow(InvalidParameter::new);
			if (recipient.getDeposit() - bid.getFee()*bid.getOdds() < 0) {
				throw new InvalidParameter();
			}
			return customerBidService.acceptBid(bidId, recipient);
		}
		throw new InvalidParameter();
	}

	@PostMapping(value = "/customers/{id}/bids")
	public CustomerBid addCustomerBid(@PathVariable(value = "id") Long customerId, @RequestBody CustomerBid bid)
			throws InvalidParameter {
		if (bid.getFee() == null || bid.getFee() < 10 || bid.getOption() == null || bid.getOption().getId() == null || bid.getOdds() == null) {
			throw new InvalidParameter();
		}

		LotteryRoundOption lotteryRoundOption = lotteryRoundOptionService.findById(bid.getOption().getId())
				.orElseThrow(InvalidParameter::new);
		bid.setOption(lotteryRoundOption);
		Customer customer = customerService.findById(customerId).orElseThrow(InvalidParameter::new);
		if (customer.getDeposit() - bid.getFee() < 0) {
			throw new InvalidParameter();
		}
		bid.setInitiator(customer);
		return customerBidService.createCustomerBid(bid);
	}

}
