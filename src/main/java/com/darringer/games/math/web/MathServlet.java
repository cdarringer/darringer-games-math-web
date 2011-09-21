package com.darringer.games.math.web;

import static com.darringer.games.math.model.MathModelRecord.NO_ANSWER;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.darringer.games.math.controller.MathController;
import com.darringer.games.math.model.MathModel;
import com.darringer.games.math.model.OperationType;
import com.darringer.games.math.model.OrderType;

/**
 * Servlet to control a simple math training game for my daughter.
 * Builds a {@link MathModel} from servlet parameters, users a 
 * {@link MathController} to score and figure out the next question, 
 * and a {@link MathJSPView} to pass information back to a jsp.
 * 
 * @author cdarringer
 */
@WebServlet("/MathServlet")
public class MathServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(MathServlet.class);
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MathServlet() {
        super();
    }
    
    /**
     * @see HttpServlet#init()
     */
    @Override
    public void init() {
		PropertyConfigurator.configure(MathServlet.class.getClassLoader().getResource("log4j.properties"));    	
    }

    /**
     * Main control loop
     * @see HttpServlet#service()
     */
    @Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MathModel model;
		MathController controller = new MathController();
		MathJSPView view = new MathJSPView();
		
		// build model from the view
		log.info("Servlet service...");
		log.info("Building math model from servlet request...");
		if (isNewGame(request)) {
			log.info("This appears to be a new game.");
			model = buildNewModelFromView(request);
		} else {
			log.info("Reconstructing model for existing game.");
			model = buildExistingModelFromView(request);
			controller.checkCurrentAnswer(model);
			controller.rateExistingAnswers(model);
		}
				
		// get next question
		log.info("Preparing the next question...");
		controller.prepareNextQuestion(model);
		
		// display the status, optional next question, and the grid
		log.info("Preparing the view...");
		view = new MathJSPView(model);
		request.setAttribute("mathJSPView", view);

		// forward the request to our presentation servlet
		log.info("Forwarding to the game page for display..");
		String nextJSP = "/game.jsp";
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
		dispatcher.forward(request, response);
	}
	
    
	/**
	 * 
	 * @param request
	 * @return
	 */
	public boolean isNewGame(HttpServletRequest request) {
		String action = request.getParameter("action");
		return ((action != null) && (action.equals("new")));
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public MathModel buildNewModelFromView(HttpServletRequest request) {
		// it is assumed that type and order will always be passed on new games
		String typeString = request.getParameter("type");
		String orderString = request.getParameter("order");
		OperationType operationType = OperationType.valueOf(typeString);
		OrderType orderType = OrderType.valueOf(orderString);
		return new MathModel(operationType, orderType);
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public MathModel buildExistingModelFromView(HttpServletRequest request) {
		// start with a new model
		MathModel model = buildNewModelFromView(request);
		
		// get the expected parameters
		String currentAnswer = request.getParameter("currentAnswer");
		String startTimeInMillis = request.getParameter("currentTime");
		String answers = request.getParameter("answers");
		String times = request.getParameter("times");
		String questionFirstNumber = request.getParameter("questionFirstNumber");
		String questionSecondNumber = request.getParameter("questionSecondNumber");

		// the only input we cannot rely on is the user's answer...
		try {
			model.setCurrentAnswer(Integer.valueOf(currentAnswer));
		} catch (NumberFormatException nfe) {
			log.warn("Answer was not a valid number, we will ignore it");
			model.setCurrentAnswer(NO_ANSWER);
		}
		
		// update the model
		model.setLastStartTimeInMillis(Long.valueOf(startTimeInMillis));
		model.setGivenAnswers(getIntegerArrayFromString(answers));
		model.setResponseTimes(getIntegerArrayFromString(times));
		model.setQuestionFirstNumber(Integer.valueOf(questionFirstNumber));
		model.setQuestionSecondNumber(Integer.valueOf(questionSecondNumber));
		return model;
	}
	
	
	/**
	 * 
	 * @param integersString
	 * @return
	 */
	private int[] getIntegerArrayFromString(String integersString) {
		int[] integers = new int[100];
		String[] integersStringArray = integersString.split(",");
		for (int i = 0; i < 100; i++) {
			integers[i] = Integer.valueOf(integersStringArray[i]);
		}
		return integers;
	}
	
}
