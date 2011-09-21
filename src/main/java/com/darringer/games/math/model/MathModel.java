package com.darringer.games.math.model;

import static com.darringer.games.math.model.GameState.New;
import static com.darringer.games.math.model.MathModelRecord.NO_ANSWER;

/**
 * Container for the data associated with the math game
 * 
 * @author cdarringer
 */
public class MathModel {

	private GameState gameState;
	private OperationType operationType;
	private OrderType orderType;
	private long lastStartTimeInMillis;
	private Integer currentAnswer;
	private Integer questionFirstNumber;
	private Integer questionSecondNumber;
	private MathModelRecord[] grid = new MathModelRecord[100];

	
	/**
	 * 
	 */
	public MathModel() { }
	
	/**
	 * 
	 * @param operationType
	 * @param orderType
	 */
	public MathModel(OperationType operationType, OrderType orderType) {
		for (int i=0; i < 100; i++) {
			grid[i] = new MathModelRecord();
		}
		this.operationType = operationType;
		this.orderType = orderType;
		this.lastStartTimeInMillis = System.currentTimeMillis();
		this.questionFirstNumber = NO_ANSWER;
		this.questionSecondNumber = NO_ANSWER;
		this.currentAnswer = NO_ANSWER;
		this.gameState = New;
	}
	
	/**
	 * 
	 * @param givenAnswers
	 */
	public void setGivenAnswers(int[] givenAnswers) {
		for (int i=0; i < 100; i++) {
			grid[i].setGivenAnswer(givenAnswers[i]);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public int[] getGivenAnswers() {
		int[] givenAnswers = new int[100];
		for (int i=0; i < 100; i++) {
			givenAnswers[i] = grid[i].getGivenAnswer();
		}
		return givenAnswers;
	}
	
	/**
	 * 
	 * @param responseTimes
	 */
	public void setResponseTimes(int[] responseTimes) {
		for (int i=0; i < 100; i++) {
			grid[i].setResponseTimeInSeconds(responseTimes[i]);
		}
	}
	
	/**
	 * 
	 * @param gridStates
	 */
	public void setGridStates(GameState[] gridStates) {
		for (int i=0; i < 100; i++) {
			grid[i].setState(gridStates[i]);
		}		
	}

	/**
	 * 
	 * @return
	 */
	public int[] getResponseTimes() {
		int[] responseTimes = new int[100];
		for (int i=0; i < 100; i++) {
			responseTimes[i] = grid[i].getResponseTimeInSeconds();
		}
		return responseTimes;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int getGivenAnswer(int x, int y) {
		return grid[getIndex(x, y)].getGivenAnswer();
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param answer
	 */
	public void setGivenAnswer(int x, int y, int answer) {
		grid[getIndex(x, y)].setGivenAnswer(answer);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int getResponseTime(int x, int y) {
		return grid[getIndex(x, y)].getResponseTimeInSeconds();
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public GameState getGameState(int x, int y) {
		return grid[getIndex(x, y)].getState();
	}
	
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param timeInSeconds
	 */
	public void setResponseTime(int x, int y, int timeInSeconds) {
		grid[getIndex(x, y)].setResponseTimeInSeconds(timeInSeconds);
	}
	
	/**
	 * 
	 * @return
	 */
	public long getLastStartTimeInMillis() {
		return lastStartTimeInMillis;
	}

	/**
	 * 
	 * @param lastStartTimeInMillis
	 */
	public void setLastStartTimeInMillis(long lastStartTimeInMillis) {
		this.lastStartTimeInMillis = lastStartTimeInMillis;
	}
	
	/**
	 * 
	 * @return
	 */
	public OperationType getOperationType() {
		return operationType;
	}

	/**
	 * 
	 * @param operationType
	 */
	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

	/**
	 * 
	 * @return
	 */
	public OrderType getOrderType() {
		return orderType;
	}

	/**
	 * 
	 * @param orderType
	 */
	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}	
	
	/**
	 * 
	 * @return
	 */
	public Integer getQuestionFirstNumber() {
		return questionFirstNumber;
	}

	/**
	 * 
	 * @param questionFirstNumber
	 */
	public void setQuestionFirstNumber(Integer questionFirstNumber) {
		this.questionFirstNumber = questionFirstNumber;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getQuestionSecondNumber() {
		return questionSecondNumber;
	}

	/**
	 * 
	 * @param questionSecondNumber
	 */
	public void setQuestionSecondNumber(Integer questionSecondNumber) {
		this.questionSecondNumber = questionSecondNumber;
	}

	/**
	 * 
	 * @return
	 */
	public GameState getGameState() {
		return gameState;
	}

	/**
	 * 
	 * @param gameState
	 */
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	
	/**
	 * 
	 * @return
	 */
	public Integer getCurrentAnswer() {
		return currentAnswer;
	}

	/**
	 * 
	 * @param currentAnswer
	 */
	public void setCurrentAnswer(Integer currentAnswer) {
		this.currentAnswer = currentAnswer;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getAnswersAsString() {
		String stringValue = String.valueOf(grid[0].getGivenAnswer());
		for (int i=1; i < 100; i++) {
			stringValue+="," + String.valueOf(grid[i].getGivenAnswer());
		}
		return stringValue;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getResponseTimesAsString() {
		String stringValue = String.valueOf(grid[0].getResponseTimeInSeconds());
		for (int i=1; i < 100; i++) {
			stringValue+="," + String.valueOf(grid[i].getResponseTimeInSeconds());
		}
		return stringValue;
	}
		
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private int getIndex(int x, int y) {
		return ((x - 1) * 10) + (y - 1);
	}
}
