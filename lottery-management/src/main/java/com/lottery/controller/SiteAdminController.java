package com.lottery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lottery.exception.InvalidParameter;
import com.lottery.model.Agent;
import com.lottery.model.Customer;
import com.lottery.model.Lottery;
import com.lottery.model.LotteryItem;
import com.lottery.model.LotteryItemOption;
import com.lottery.model.LotteryRound;
import com.lottery.model.LotteryRoundOption;
import com.lottery.model.LotteryRoundTag;
import com.lottery.service.AgentService;
import com.lottery.service.CustomerService;
import com.lottery.service.LotteryItemOptionService;
import com.lottery.service.LotteryItemService;
import com.lottery.service.LotteryRoundOptionService;
import com.lottery.service.LotteryRoundService;
import com.lottery.service.LotteryRoundTagService;
import com.lottery.service.LotteryService;

@RestController
@RequestMapping(value = "/api/v1")
public class SiteAdminController {
	@Autowired
	private AgentService agentService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private LotteryService lotteryService;
	@Autowired
	private LotteryItemService lotteryItemService;
	@Autowired
	private LotteryItemOptionService lotteryItemOptionService;
	@Autowired
	private LotteryRoundService lotteryRoundService;
	@Autowired
	private LotteryRoundOptionService lotteryRoundOptionService;
	@Autowired
	private LotteryRoundTagService lotteryRoundTagService;

	

	@GetMapping(value = "/admins/{id}/agents")
	public Page<Agent> getAllAgents(@RequestParam int page, @RequestParam int size) throws InvalidParameter {
		return agentService.findAll(page - 1, size);
	}

	@GetMapping(value = "/admins/{id}/users")
	public Page<Customer> getAllUsers(@RequestParam int page, @RequestParam int size) throws InvalidParameter {
		return customerService.findAll(page - 1, size);
	}

	@PostMapping(value = "/admins/{id}/lotteries")
	public Lottery postLottery(@RequestBody Lottery lottery) {
		return lotteryService.saveOrUpdate(lottery);
	}

	@PostMapping(value = "/admins/{aid}/lotteries/{lid}/items")
	public LotteryItem postLotteryItem(@PathVariable Long lid, @RequestBody LotteryItem lotteryItem)
			throws InvalidParameter {
		Lottery lottery = lotteryService.findById(lid).orElseThrow(InvalidParameter::new);
		lotteryItem.setLottery(lottery);
		return lotteryItemService.saveOrUpdate(lotteryItem);
	}

	@PostMapping(value = "/admins/{aid}/lotteries/{lid}/items/{iid}/options")
	public LotteryItemOption postLotteryItemOption(@PathVariable Long iid,
			@RequestBody LotteryItemOption lotteryItemOption) throws InvalidParameter {
		LotteryItem item = lotteryItemService.findById(iid).orElseThrow(InvalidParameter::new);
		lotteryItemOption.setItem(item);
		return lotteryItemOptionService.saveOrUpdate(lotteryItemOption);
	}

	@PostMapping(value = "/admins/{aid}/lotteries/{lid}")
	public Lottery postLottery(@PathVariable Long lid, @RequestBody Lottery lottery) throws InvalidParameter {
		lotteryService.findById(lid).orElseThrow(InvalidParameter::new);
		return lotteryService.saveOrUpdate(lottery);
	}

	@PutMapping(value = "/admins/{aid}/lotteries/{lid}")
	public Lottery putLottery(@PathVariable Long lid, @RequestBody Lottery lottery) throws InvalidParameter {
		Lottery target = lotteryService.findById(lid).orElseThrow(InvalidParameter::new);
		target.setName(lottery.getName());

		return lotteryService.saveOrUpdate(target);
	}

	@PutMapping(value = "/admins/{aid}/lotteries/{lid}/items/{iid}")
	public LotteryItem putLotteryItem(@PathVariable Long iid, @RequestBody LotteryItem lotteryItem)
			throws InvalidParameter {
		
		LotteryItem target = lotteryItemService.findById(iid).orElseThrow(InvalidParameter::new);
		target.setName(lotteryItem.getName());
		target.setOrder(lotteryItem.getOrder());
		return lotteryItemService.saveOrUpdate(target);
	}

	@PutMapping(value = "/admins/{aid}/lotteries/{lid}/items/{iid}/options/{oid}")
	public LotteryItemOption putLotteryItemOption(@PathVariable Long oid,
			@RequestBody LotteryItemOption lotteryItemOption) throws InvalidParameter {
		LotteryItemOption target = lotteryItemOptionService.findById(oid).orElseThrow(InvalidParameter::new);
		target.setOrder(lotteryItemOption.getOrder());
		target.setText(lotteryItemOption.getText());
		return lotteryItemOptionService.saveOrUpdate(lotteryItemOption);
	}

	@DeleteMapping(value = "/admins/{aid}/lotteries/{lid}/items/{iid}/options/{oid}")
	public void deleteLotteryItemOption(@PathVariable Long oid) throws InvalidParameter {
		lotteryItemOptionService.findById(oid).orElseThrow(InvalidParameter::new);
		lotteryItemOptionService.deleteById(oid);
	}
	
	@DeleteMapping(value = "/admins/{aid}/lotteries/{lid}/items/{iid}")
	public void deleteLotteryItem(@PathVariable Long iid) throws InvalidParameter {
		LotteryItem item = lotteryItemService.findById(iid).orElseThrow(InvalidParameter::new);

		lotteryItemOptionService.deleteByItem(item);
		lotteryItemService.deleteById(iid);
	}
	
	@DeleteMapping(value = "/admins/{aid}/lotteries/{lid}")
	public void deleteLottery(@PathVariable Long lid) throws InvalidParameter {
		Lottery lottery = lotteryService.findById(lid).orElseThrow(InvalidParameter::new);

		lotteryItemOptionService.deleteByLottery(lottery);
		lotteryItemService.deleteByLottery(lottery);
		lotteryService.deleteById(lid);
	}
	
	@PostMapping(value = "/admins/{aid}/lotteries/{lid}/rounds/{rid}/options")
	public LotteryRoundOption postLotteryRoundOptionByLottery(@PathVariable Long rid, @RequestBody LotteryRoundOption option) throws InvalidParameter {
		LotteryRound round = lotteryRoundService.findById(rid).orElseThrow(InvalidParameter::new);
		LotteryItemOption lotteryItemOption = lotteryItemOptionService.findById(option.getOption().getId()).orElseThrow(InvalidParameter::new);
		option.setRound(round);
		option.setOption(lotteryItemOption);
		option.setId(null);
		return lotteryRoundOptionService.saveOrUpdate(option);
	}
	
	@PostMapping(value = "/admins/{aid}/lotteries/{lid}/rounds/{rid}/results")
	public List<LotteryItemOption> postLotteryRoundResultByLottery(@PathVariable Long rid, @RequestBody List<LotteryItemOption> results) throws InvalidParameter {
		LotteryRound round = lotteryRoundService.findById(rid).orElseThrow(InvalidParameter::new);
		round.setResults(results);
		round = lotteryRoundService.saveOrUpdate(round);
		return round.getResults();
	}
	

	
	@PostMapping(value = "/admins/{aid}/lotteries/rounds")
	public LotteryRound postLotteryRound(@RequestBody LotteryRound round) {
		return lotteryRoundService.saveOrUpdate(round);
	}
	
	@PutMapping(value = "/admins/{aid}/lotteries/rounds/{rid}")
	public LotteryRound putLotteryRound(@PathVariable Long rid, @RequestBody LotteryRound round) throws InvalidParameter {
		if (round.getLottery() == null) {
			throw new InvalidParameter();
		}
		Lottery lottery = lotteryService.findById(round.getLottery().getId()).orElseThrow(InvalidParameter::new);
		
		round.setId(rid);
		round=  lotteryRoundService.saveOrUpdate(round);
		List<LotteryItemOption> lotteryItemOptions = lotteryItemOptionService.findByLotteryId(lottery.getId());
		for (LotteryItemOption lotteryItemOption: lotteryItemOptions) {
			LotteryRoundOption lotteryRoundOption = new LotteryRoundOption();
			lotteryRoundOption.setOption(lotteryItemOption);
			lotteryRoundOption.setRound(round);
			lotteryRoundOptionService.saveOrUpdate(lotteryRoundOption);
		}
		
		return round;
	}
	
	@PutMapping(value = "/admins/{aid}/lotteries/{lid}/rounds/{lid}/options/{oid}")
	public LotteryRoundOption putLotteryRoundOptionByLottery(@PathVariable Long oid, @RequestBody LotteryRoundOption option) throws InvalidParameter {
		LotteryRoundOption lro = lotteryRoundOptionService.findById(oid).orElseThrow(InvalidParameter::new);
		lro.setOdds(option.getOdds());
		return lotteryRoundOptionService.saveOrUpdate(lro);
	}
	
	@PostMapping(value = "/admins/{aid}/lotteries/rounds/tags")
	public LotteryRoundTag postTags(@RequestBody LotteryRoundTag tag) {
		return lotteryRoundTagService.saveOrUpdate(tag);
	}

	@PostMapping(value = "/admins/{aid}/lotteries/rounds/{rid}/tags")
	public void postTagforRound(@PathVariable Long rid, @RequestBody List<LotteryRoundTag> tags) throws InvalidParameter {
		LotteryRound lotteryRound = lotteryRoundService.findById(rid).orElseThrow(InvalidParameter::new);
		lotteryRound.getTags().addAll(tags);
		lotteryRoundService.saveOrUpdate(lotteryRound);
	}
	
	@DeleteMapping(value = "/admins/{aid}/lotteries/rounds/tags/{tid}")
	public void deleteTag(@PathVariable Long tid) {
		lotteryRoundTagService.delete(tid);
	}
	
	@PostMapping(value = "/admins/{aid}/actions/settle-round")
	public void settleRound(@RequestBody LotteryRound round) {
		lotteryRoundService.settleRound(round.getId());
	}
}
