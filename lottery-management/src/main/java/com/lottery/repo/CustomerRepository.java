package com.lottery.repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lottery.model.Agent;
import com.lottery.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
	Page<Customer> findByAgent(Agent agent, Pageable pageable);
	Optional<Customer> findByTel(String tel);
	Optional<Customer> findByTelAndPassword(String tel, String encryptPassword);
	Optional<Customer> findByIdAndPassword(Long id, String encryptPassword);

	@Modifying
	@Query("UPDATE Customer c SET c.deposit=c.deposit+?2 WHERE c.id=?1 AND c.deposit+?2>=0")
	int updateCustomerDepositById(Long id, Float amount);
}
