package com.db.demo.service;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.demo.dao.TradeRepo;
import com.db.demo.dto.Trade;
import com.db.demo.exception.TradeException;

@Service
public class TradeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TradeService.class);

	@Autowired
	TradeRepo tradeRepo;

	public boolean isValid(Trade trade) {
		if (validMaturityDate(trade)) {
			//Validation for lower Version with same id
			Integer maxVersion = tradeRepo.findMaxVersionById(trade.getTradeId());
			if (maxVersion!=null && maxVersion > trade.getVersion()) {
				LOGGER.error("This Trade already exists with higher version.");
				throw new TradeException("Invalid Trade : "+trade.getTradeId()+", Trade already exists with higher version.");
			}
		}else {
			LOGGER.error("Maturity date is less then the todays date.");
			throw new TradeException("Invalid Trade : "+trade.getTradeId()+", Maturity date is less then the todays date.");
		}
			return true;
	}

	// Validation for to check the maturity is not less then then today date
	private boolean validMaturityDate(Trade trade) {
		return trade.getMaturityDate().isBefore(LocalDate.now()) ? false : true;
	}

	public void saveTrade(Trade trade) {
		trade.setCreatedDate(LocalDate.now());
		tradeRepo.save(trade);
	}

	public void updateTradetoExpire() {		 
		tradeRepo.findTradeToExpire(LocalDate.now()).stream().forEach(t -> {
				t.setExpiredFlag("Y");
				LOGGER.info("Trade "+ t.getTradeId()+ "is expired!");
				tradeRepo.save(t);
		});
	}

}
