package com.db.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.db.demo.dto.Trade;
import com.db.demo.exception.TradeException;
import com.db.demo.service.TradeService;

@RestController
public class TradeController {
	@Autowired
	TradeService tradeService;

	@PostMapping("/storetrade")
	public ResponseEntity<String> tradeTransmission(@RequestBody Trade trade) {
		try {
			if (tradeService.isValid(trade)) {
				tradeService.saveTrade(trade);
			}
		} catch (TradeException tr) {
			throw tr;
		}catch (Exception e) {
			throw new TradeException("Some error occured while processing the trade!");
		}
		

		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
