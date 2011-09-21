package com.darringer.games.math.model;

public enum OperationType {
	Addition("+"), Subtraction("-"), Multiplication("X"), Division("/");
	
	String symbol;
	
	OperationType(String symbol) {
		this.symbol = symbol;
	}
	
	public String getSymbol() {
		return this.symbol;
	}
}
