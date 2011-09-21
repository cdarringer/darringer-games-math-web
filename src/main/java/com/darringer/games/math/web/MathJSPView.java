	package com.darringer.games.math.web;

import com.darringer.games.math.model.GameState;
import com.darringer.games.math.model.MathModel;
import com.darringer.games.math.model.OperationType;


/**
 * Present the model in a way that makes it easy for a JSP page to display
 * 
 * @author cdarringer
 */
public class MathJSPView {
	
	private MathModel model;
	
	/**
	 * Default constructor required by the JSP page
	 */
	public MathJSPView() {	
	}
	
	/**
	 * 
	 * @param model
	 */
	public MathJSPView(MathModel model) {
		this.model = model;
	}
		
	/**
	 * 
	 * @return
	 */
	public String getAnswers() {
		return model.getAnswersAsString();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getResponseTimes() {
		return model.getResponseTimesAsString();
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public String getValue(int x, int y) {
		return String.valueOf(model.getGivenAnswer(x, y));
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public GameState getState(int x, int y) { 
		return model.getGameState(x, y);
	}	
	
	/**
	 * 
	 * @return
	 */
	public String getOrderType() {
		return this.model.getOrderType().toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public OperationType getOperationType() {
		return this.model.getOperationType();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getQuestionFirstNumber() {
		return String.valueOf(model.getQuestionFirstNumber());
	}

	/**
	 * 
	 * @return
	 */
	public String getQuestionSecondNumber() {
		return String.valueOf(model.getQuestionSecondNumber());
	}
	
	/**
	 * 
	 * @return
	 */
	public GameState getGameState() {
		return model.getGameState();
	}
}
