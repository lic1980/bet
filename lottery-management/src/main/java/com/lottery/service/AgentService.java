package com.lottery.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.zxing.WriterException;
import com.lottery.model.Agent;
import com.lottery.model.Customer;
import com.lottery.repo.AgentRepository;
import com.lottery.util.IdUtils;
import com.lottery.util.SecurityUtils;

@Service
public class AgentService {
	@Autowired
	private AgentRepository repo;
	@Autowired
	private QRCodeService qrCodeService;


	public Optional<Agent> findById(Long id) {
		return repo.findById(id);
	}
	
	public Page<Agent> findAll(int pageNum, int pageSize) {
		Pageable page = PageRequest.of(pageNum, pageSize);
		return repo.findAll(page);
	}
	
	public Optional<Agent> findByTel(String tel) {
		return repo.findByTel(tel);
	}
	
	public Optional<Agent> validatePassword(String tel, String originalPassword) {
		return repo.findByTelAndPassword(tel, SecurityUtils.encryptPassword(originalPassword));
	}
	
	public Optional<Agent> validateByIdAndPassword(Long id, String plainPassword) {
		return repo.findByIdAndPassword(id, SecurityUtils.encryptPassword(plainPassword));
	}
	
	public Agent saveOrUpdate(Agent agent) {
		if (!StringUtils.isEmpty(agent.getPlainPassword())) {
			agent.setPassword(SecurityUtils.encryptPassword(agent.getPlainPassword()));
		}
		if (StringUtils.isEmpty(agent.getId())) {
			long id = IdUtils.generateId();
			agent.setId(id);
		} 
		
		return repo.save(agent);
	}
}
