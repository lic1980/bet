package com.lottery.controller;

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
		if ("initiator".equals(role))  {
			return customerBidService.findByInitiator(id, page - 1, size);
		}
		return customerBidService.findAvailable(id, page - 1, size);
	}
	
	@GetMapping(value = "/customers/{cid}/bids/{bid}")
	public CustomerBid getCustomerBids(@PathVariable Long cid, @PathVariable Long bid) throws InvalidParameter {
		return customerBidService.findById(bid).orElseThrow(InvalidParameter::new);
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
		if (bid.getFee() == null || bid.getFee() <=1 || bid.getOption() == null || bid.getOption().getId() == null || bid.getOdds() == null) {
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
