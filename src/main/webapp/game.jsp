<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.darringer.games.math.model.GameState"%>
<%@ page import="com.darringer.games.math.model.OperationType"%>

<jsp:useBean id="mathJSPView" class="com.darringer.games.math.web.MathJSPView" scope="request" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Welcome to Frania's Math Game!</title>
<link rel="stylesheet" type="text/css" href="mathStyles.css" />

<!--  do some basic form validation for numbers or '-' sign -->
<script language="JavaScript">
function checkForNumbers(evt) {
    evt = (evt) ? evt : window.event
    var charCode = (evt.which) ? evt.which : evt.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57) && (charCode != 45)) {
        status = "This field accepts numbers only."
        return false
    }
    status = ""
    return true
}
</script>
</head>

<body OnLoad="document.answerForm.currentAnswer.focus();">
<center>
<h2>Welcome to Math-o-Matic!</h2>

<!--  create the math question -->
<% if (mathJSPView.getGameState() != GameState.Done) { %>
<form name="answerForm" method="POST" action="MathServlet">
<table>
	<tr>
		<!-- first number -->
		<td class="question">
		<%
			out.print(mathJSPView.getQuestionFirstNumber());
		%>
		</td>

		<!--  operator -->
		<td class="question">
		<% 
			out.print(mathJSPView.getOperationType().getSymbol()); 
		%>				
		</td>

		<!-- second number -->
		<td class="question">
		<%
			out.print(mathJSPView.getQuestionSecondNumber());
		%>
		</td>

		<!--  answer -->
		<td class="question">=</td>
		<td class="question">
			<input style="vertical-align:middle" type="text" name="currentAnswer" size="3" maxlength="3"
				onKeyPress="return checkForNumbers(event)" />
			?
		</td>
	</tr>
</table>
<p>
<!--  game state relayed through hidden form fields -->
<input name="type" type="hidden" value="<% out.print(mathJSPView.getOperationType().toString()); %>" />
<input name="order" type="hidden" value="<% out.print(mathJSPView.getOrderType()); %>" /> 
<input name="questionFirstNumber" type="hidden" value="<% out.print(mathJSPView.getQuestionFirstNumber()); %>" /> 
<input name="questionSecondNumber" type="hidden" value="<% out.print(mathJSPView.getQuestionSecondNumber()); %>" /> 
<input name="answers" type="hidden" value="<% out.print(mathJSPView.getAnswers()); %>" /> 
<input name="times" type="hidden" value="<% out.print(mathJSPView.getResponseTimes()); %>" /> 
<input name="currentTime" type="hidden" value="<% out.print(System.currentTimeMillis()); %>" /> 
<input type="submit" value="Answer!" />
</p>
<% } %>

<!--  draw the grid -->
<table class="grid">
	<tr>
		<td class="empty" />
		<td class="empty">1</td>
		<td class="empty">2</td>
		<td class="empty">3</td>
		<td class="empty">4</td>
		<td class="empty">5</td>
		<td class="empty">6</td>
		<td class="empty">7</td>
		<td class="empty">8</td>
		<td class="empty">9</td>
		<td class="empty">10</td>
		<td class="empty" />
	</tr>
	<%
		for (int x = 1; x < 11; x++) {
	%>
	<tr valign="middle">
		<td class="empty"><% out.print(x); %></td>
		<% for (int y = 1; y < 11; y++) { %>
			<% if (mathJSPView.getState(x, y) == GameState.Unknown) { %>
				<td align="center" class="empty">
			<% } else if (mathJSPView.getState(x, y) == GameState.GoodAnswer) { %>
				<td align="center" class="good">
			<% } else if (mathJSPView.getState(x, y) == GameState.BetterAnswer) { %>
				<td align="center" class="better">
			<% } else if (mathJSPView.getState(x, y) == GameState.BestAnswer) { %>	
				<td align="center" class="best">
			<% } else if (mathJSPView.getState(x, y) == GameState.BadAnswer) { %>
				<td align="center" class="wrong">
			<% } else { %>	
				<td align="center">
			<% } %> 
			<% if (mathJSPView.getState(x, y) == GameState.Unknown) { %> 
				? 
			<% } else { %>
				<% out.print(mathJSPView.getValue(x, y)); %> 
			<% } %>
		</td>
		<% } %>
		<td class="empty" height="25" width="25">
			<% out.print(x); %>
		</td>
	</tr>
	<% } %>
	<tr>
		<td class="empty" />
		<td class="empty">1</td>
		<td class="empty">2</td>
		<td class="empty">3</td>
		<td class="empty">4</td>
		<td class="empty">5</td>
		<td class="empty">6</td>
		<td class="empty">7</td>
		<td class="empty">8</td>
		<td class="empty">9</td>
		<td class="empty">10</td>
		<td class="empty" />
	</tr>
</table>
</form>

<!-- Status -->
<p class="status">
	<% switch(mathJSPView.getGameState()) { 
	case BadAnswer: %>
		Sorry, that was not the right answer.
	<%	break;
	case GoodAnswer: %>
		That was correct but a little slow...
	<%	break;
	case BetterAnswer: %>
		That was correct!
	<%	break;
	case BestAnswer: %>
		That was correct and really fast!
	<%	break;
	case Done: %>
		Game over! Take a break ;)
	<%	break;
	case SystemError: %>
		State: System error!
	<%	break;
	} %>
</p>

<!--  Useful links --> <br />
<p>
<a href="index.html">Start over</a>
</p>
<p>
<a href="http://www.darringer.com/index.html">Return to darringer.com</a>
</p>
<br />
<p class="footer">
LAST UPDATE: September 7, 2011
</p>
</center>
</body>
</html>