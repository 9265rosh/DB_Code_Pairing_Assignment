package com.db.demo.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.db.demo.dto.Trade;

public interface TradeRepo extends JpaRepository<Trade, String>{
	
	@Query("SELECT max(tr.version) FROM Trade tr WHERE tr.tradeId = :id")
	Integer findMaxVersionById(@Param("id")String id);
	
	@Query("SELECT tr from Trade tr WHERE tr.maturityDate < :todayDate")
	List<Trade> findTradeToExpire(@Param("todayDate") LocalDate todayDate);
	

}
