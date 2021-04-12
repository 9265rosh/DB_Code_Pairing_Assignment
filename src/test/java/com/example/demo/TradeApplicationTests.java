package com.example.demo;

import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.db.demo.TradeApplication;
import com.db.demo.controller.TradeController;
import com.db.demo.dao.TradeRepo;
import com.db.demo.dto.Trade;
import com.db.demo.exception.TradeException;
import com.db.demo.service.TradeService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = TradeApplication.class)
class TradeApplicationTests {
	@Autowired
	TradeService tradeServiceMock;

	@Autowired
	TradeRepo tradeRepoMock;

	@Autowired
	TradeController tradeController;

	@Test
	void contextLoads() {
	}

	@SuppressWarnings("rawtypes")
	@Test
	void testTradeSuccess() {
		ResponseEntity responseEntity = tradeController.tradeTransmission(getTrade("T1", 1, LocalDate.now()));
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity);

	}

	@SuppressWarnings({ "unused", "rawtypes" })
	@Test
	void testTradeeWhenMaturityDatePast() {
		try {
			LocalDate localDate = getLocalDate(2015, 05, 21);
			ResponseEntity responseEntity = tradeController.tradeTransmission(getTrade("T2", 1, localDate));
		} catch (TradeException e) {
			Assertions.assertEquals("Invalid Trade : T2, Maturity date is less then the todays date.", e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@Test
	void testTradeWhenOldVersion() {
		// creating a trade
		ResponseEntity responseEntity = tradeController.tradeTransmission(getTrade("T1", 2, LocalDate.now()));
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity);

		// creating similar trade with older version
		try {
			@SuppressWarnings("unused")
			ResponseEntity responseEntity1 = tradeController.tradeTransmission(getTrade("T1", 1, LocalDate.now()));

		} catch (TradeException e) {
			Assertions.assertEquals("Invalid Trade : T1, Trade already exists with higher version.", e.getMessage());
		}
		;
	}

	@SuppressWarnings("rawtypes")
	@Test
	void testTradeWhenSameVersionTrade() {
		// creating a trade
		ResponseEntity responseEntity = tradeController.tradeTransmission(getTrade("T1", 2, LocalDate.now()));
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity);

		// creating a trade with same version
		Trade trade2 = getTrade("T1", 2, LocalDate.now());
		trade2.setBookId("T1B1V2");
		ResponseEntity responseEntity2 = tradeController.tradeTransmission(trade2);
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity2);

	}

	@SuppressWarnings("rawtypes")
	@Test
	void testTradeWhenHigherVersionTrade() {
		// creating a trade
		ResponseEntity responseEntity = tradeController.tradeTransmission(getTrade("T1", 2, LocalDate.now()));
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity);

		// creating a trade with same version
		Trade trade2 = getTrade("T1", 3, LocalDate.now());
		trade2.setBookId("T1B1V2");
		ResponseEntity responseEntity2 = tradeController.tradeTransmission(trade2);
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity2);

	}
	

	private static LocalDate getLocalDate(int year, int month, int day) {
		LocalDate localDate = LocalDate.of(year, month, day);
		return localDate;
	}

	private Trade getTrade(String tradeId, int version, LocalDate maturityDate) {
		Trade trade = new Trade();
		trade.setTradeId(tradeId);
		trade.setBookId("B1");
		trade.setVersion(version);
		trade.setCounterParty("CP-" + version);
		trade.setMaturityDate(maturityDate);
		trade.setExpiredFlag("Y");
		return trade;
	}

}
