package com.darringer.games.math.controller;

import static com.darringer.games.math.model.GameState.BadAnswer;
import static com.darringer.games.math.model.GameState.BestAnswer;
import static com.darringer.games.math.model.GameState.BetterAnswer;
import static com.darringer.games.math.model.GameState.Done;
import static com.darringer.games.math.model.GameState.GoodAnswer;
import static com.darringer.games.math.model.GameState.SystemError;
import static com.darringer.games.math.model.GameState.Unknown;
import static com.darringer.games.math.model.MathModelRecord.NO_ANSWER;
import static com.darringer.games.math.model.MathModelRecord.NO_TIME;
import static com.darringer.games.math.model.MathModelRecord.WRONG_ANSWER;
import static com.darringer.games.math.model.OperationType.Division;
import static com.darringer.games.math.model.OrderType.Scrambled;

import java.util.Random;

import org.apache.log4j.Logger;

import com.darringer.games.math.model.GameState;
import com.darringer.games.math.model.MathModel;

/**
 * The controller works with the {@link MathModel} and 
 * contains any "logic" behind the game
 * 
 * @author cdarringer
 */
public class MathController {

	private static long TIME_LAG_IN_SECONDS = 1l;
	private static long TIME_BEST_IN_SECONDS = 2l;
	private static long TIME_BETTER_IN_SECONDS = 5l;
	private static int NO_MORE_QUESTIONS = -1;
	
	private static Logger log = Logger.getLogger(MathController.class);

	/**
	 * Check the given answer and update the model with the results
	 * @param model
	 * @return
	 */
	public MathModel checkCurrentAnswer(MathModel model) {
		
		// did they provide a valid answer?
		if (model.getCurrentAnswer() == NO_ANSWER) {
			// no they didn't...
			return model;
		}
		
		// record the current answer in the grid
		log.info("Checking the current answer...");
		int x = model.getQuestionFirstNumber();
		int y = model.getQuestionSecondNumber();
		setGivenAnswer(model, x, y);
		
		// what is the right answer?
		int correctAnswer;
		switch (model.getOperationType()) {
		case Addition:
			correctAnswer = model.getQuestionFirstNumber() + model.getQuestionSecondNumber();
			break;
		case Subtraction:
			correctAnswer = model.getQuestionFirstNumber() - model.getQuestionSecondNumber();
			break;
		case Multiplication:
			correctAnswer = model.getQuestionFirstNumber() * model.getQuestionSecondNumber();
			break;
		case Division:
			correctAnswer = model.getQuestionFirstNumber() / model.getQuestionSecondNumber();
			break;
		default:
			log.error("Unexpected operator!");
			correctAnswer = NO_ANSWER;
			model.setGameState(SystemError);
			return model;
		}
		log.info("The correct answer is " + correctAnswer);

		// did they get the answer right?
		if (model.getCurrentAnswer() != correctAnswer) {
			log.info("The given answer (" + model.getCurrentAnswer() + ") was incorrect.");
			model.setGameState(BadAnswer);
			setResponseTime(model, x, y, WRONG_ANSWER);
		} else {
			// the answer was right - how long did they take to provide it?
			log.info("The given answer was correct.");
			long startTime = model.getLastStartTimeInMillis();
			long endTime = System.currentTimeMillis();
			long elapsedTimeInSeconds = (endTime - startTime) / 1000;
			int responseTimeInSeconds = (int) (elapsedTimeInSeconds - TIME_LAG_IN_SECONDS);
			responseTimeInSeconds = responseTimeInSeconds < 0 ? 0 : responseTimeInSeconds;
			log.info("The response time was " + responseTimeInSeconds + " second(s).");
			model.setGameState(rateGoodResponseTime(responseTimeInSeconds));
			setResponseTime(model, x, y, responseTimeInSeconds);
		}
		return model;
	}
	
	/**
	 * 
	 * @param model
	 */
	public void rateExistingAnswers(MathModel model) {
		GameState[] states = new GameState[100];
		for (int i=0; i < 100; i++) {
			int currentResponseTime = model.getResponseTimes()[i];
			if (currentResponseTime == NO_TIME) {
				states[i] = Unknown;
			} else if (currentResponseTime == WRONG_ANSWER) {
				states[i] = BadAnswer;
			} else if (currentResponseTime > TIME_BETTER_IN_SECONDS) {
				states[i] = GoodAnswer;
			} else if (currentResponseTime > TIME_BEST_IN_SECONDS) {
				states[i] = BetterAnswer;
			} else {
				states[i] = BestAnswer;
			}			
		}
		model.setGridStates(states);
	}

	/**
	 * 
	 * @param model
	 * @return
	 */
	public MathModel prepareNextQuestion(MathModel model) {
		int index;
		if (model.getOrderType().equals(Scrambled)) {
			log.info("Getting next scrambled question...");
			index = getNextScrambledIndex(model);
		} else {
			log.info("Getting next sequential question...");
			index = getNextOrderedIndex(model);
		}
		if (index == NO_MORE_QUESTIONS) {
			log.info("There are no more questions left, the game is done!");
			model.setGameState(Done);
		} else {
			if (model.getOperationType() == Division) {
				model.setQuestionFirstNumber(((index / 10) + 1) * ((index % 10) + 1));
				model.setQuestionSecondNumber((index / 10) + 1);
			} else {
				model.setQuestionFirstNumber((index / 10) + 1);
				model.setQuestionSecondNumber((index % 10) + 1);
			}
		}
		return model;
	}

	/**
	 * Returns a number from 0 to 99 representing the grid index
	 * of the next random unanswered question.
	 * 
	 * @param model
	 * @return
	 */
	private int getNextScrambledIndex(MathModel model) {
		Random random = new Random(model.getLastStartTimeInMillis());
		int randomIndex = Math.abs(random.nextInt() % 100);
		for (int i = randomIndex; i < (randomIndex + 100); i++) {
			if (model.getGivenAnswers()[i % 100] ==  NO_ANSWER) {
				return i % 100;
			}
		}
		return NO_MORE_QUESTIONS;
	}
	
	/**
	 * Returns a number from 0 to 99 representing the grid index
	 * of the next sequential unanswered question.
	 * 
	 * @param model
	 * @return
	 */
	private int getNextOrderedIndex(MathModel model) {
		for (int i = 0; i < 100; i++) {
			if (model.getGivenAnswers()[i] == NO_ANSWER) {
				return i;
			}
		}
		return NO_MORE_QUESTIONS;
	}
	
	/**
	 * 
	 * @param responseTimeInSeconds
	 * @return
	 */
	private GameState rateGoodResponseTime(int responseTimeInSeconds) {
		if (responseTimeInSeconds > TIME_BETTER_IN_SECONDS) {
			return GoodAnswer;
		} else if (responseTimeInSeconds > TIME_BEST_IN_SECONDS) {
			return BetterAnswer;
		} else {
			return BestAnswer;
		}
	}
	
	
	/**
	 * 
	 * @param model
	 * @param x
	 * @param y
	 */
	private void setGivenAnswer(MathModel model, int x, int y) {
		if (model.getOperationType() == Division) {
			// for 50 / 10 = 5, we store 5 at (10, 5) 
			model.setGivenAnswer(y, x / y, model.getCurrentAnswer());
		} else {
			// for 5 * 10 = 10, we store 50 at (5, 10)
			model.setGivenAnswer(x, y, model.getCurrentAnswer());
		}		
	}

	/**
	 * 
	 * @param model
	 * @param x
	 * @param y
	 * @param time
	 */
	private void setResponseTime(MathModel model, int x, int y, int time) {
		if (model.getOperationType() == Division) {
			model.setResponseTime(y, x / y, time);				
		} else {
			model.setResponseTime(x, y, time);
		}
	}
	
}
