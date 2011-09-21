package com.darringer.games.math.model;

import static com.darringer.games.math.model.GameState.Unknown;

/**
 * Data associated with individual cells in the game grid
 * 
 * @author cdarringer
 * @see MathModel
 */
public class MathModelRecord {
	public static final int WRONG_ANSWER = -888;
	public static final int NO_ANSWER = -999;
	public static final int NO_TIME = -999;
	
	private int givenAnswer;
	private int responseTimeInSeconds;
	private GameState state;

	/**
	 * 
	 */
	public MathModelRecord() {
		this.givenAnswer = NO_ANSWER;
		this.responseTimeInSeconds = NO_TIME;
		this.state = Unknown;
	}

	/**
	 * 
	 * @return
	 */
	public int getGivenAnswer() {
		return givenAnswer;
	}

	/**
	 * 
	 * @param givenAnswer
	 */
	public void setGivenAnswer(int givenAnswer) {
		this.givenAnswer = givenAnswer;
	}

	/**
	 * 
	 * @return
	 */
	public int getResponseTimeInSeconds() {
		return responseTimeInSeconds;
	}

	/**
	 * 
	 * @param responseTimeInSeconds
	 */
	public void setResponseTimeInSeconds(int responseTimeInSeconds) {
		this.responseTimeInSeconds = responseTimeInSeconds;
	}

	/**
	 * 
	 * @return
	 */
	public GameState getState() {
		return state;
	}

	/**
	 * 
	 * @param state
	 */
	public void setState(GameState state) {
		this.state = state;
	}
}
