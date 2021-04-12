package com.db.demo.schduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.db.demo.service.TradeService;

public class TradeScheduler {
	
	@Autowired
	TradeService tradeService;
	
	//scheduled for every day at midnight
	@Scheduled(cron = "${schedule.expire.trade}")
	public void scheduleToUpdateExipreFlag() {
		tradeService.updateTradetoExpire();
	}
}
