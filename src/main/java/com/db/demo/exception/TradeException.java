package com.db.demo.exception;

public class TradeException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TradeException(){
		super();
	}
	
	public TradeException(String msg){
		super(msg);
	}

}
