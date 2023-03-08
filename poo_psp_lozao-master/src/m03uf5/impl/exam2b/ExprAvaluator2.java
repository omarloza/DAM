package m03uf5.impl.exam2b;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import m03uf5.prob.calcbox.ExprToken;

/*
 * Avaluador d'expressions, permet les quatre operacions bàsiques, parèntisi i números amb decimals
 */
public class ExprAvaluator2 {
	
	public double avalua(String expr) throws BadExpressionException {
		
		try {
			List<ExprToken> iTokens = readInfixTokens(expr);
			System.out.println(iTokens);
			List<ExprToken> pTokens = convertToPostfix(iTokens);
			System.out.println(pTokens);
			return evaluate(pTokens);
			
		} catch (IOException e) {
			throw new BadExpressionException("llegint tokens infix", e);
		}
	}

	private List<ExprToken> readInfixTokens(String expr) throws IOException, BadExpressionException {
		
		List<ExprToken> tokens = new ArrayList<ExprToken>();

		StreamTokenizer st = new StreamTokenizer(new StringReader(expr));
		st.ordinaryChar('/');
		
		int currentToken = st.nextToken();
		while (currentToken != StreamTokenizer.TT_EOF) {

			if (st.ttype == StreamTokenizer.TT_NUMBER) {
				
				if (st.nval < 0) {
					tokens.add(new ExprToken('-'));
					tokens.add(new ExprToken(-st.nval));
				}
				else {
					tokens.add(new ExprToken(st.nval));	
				}
			
			} else if (st.ttype == '(') {
				tokens.add(new ExprToken(true));
			} else if (st.ttype == ')') {
				tokens.add(new ExprToken(false));
			
			} else if (st.ttype == StreamTokenizer.TT_WORD) {
				throw new BadExpressionException("paraula inesperada " + st.sval);
				
			} else {
				tokens.add(new ExprToken((char) currentToken));			
			}

			currentToken = st.nextToken();
		}

		return tokens;
	}

	private List<ExprToken> convertToPostfix(List<ExprToken> tokens) {
		
		Deque<ExprToken> stack = new LinkedList<ExprToken>();
		List<ExprToken> output = new ArrayList<>();

		for (ExprToken token: tokens) {
			// operator
			if (token.isOp()) {
				while (!stack.isEmpty() && isHigerPrec(token.getOp(), stack.peek().getOp())) {
					output.add(stack.pop());
				}
				stack.push(token);

				// left parenthesis
			} else if (token.isOpen()) {
				stack.push(token);

				// right parenthesis
			} else if (token.isClose()) {
				while (!stack.peek().isOpen()) {
					output.add(stack.pop());
					if (stack.isEmpty()) {
						throw new ArithmeticException("parentesi no obert");
					}
				}
				stack.pop();

				// digit
			} else {
				output.add(token);
			}
			
			System.out.println(token + ": " + output + " <====> " + stack);
		}
		
		while (!stack.isEmpty()) {
			output.add(stack.pop());
		}

		return output;
	}

	private double evaluate(List<ExprToken> tokens) throws BadExpressionException {

		Deque<Double> stack = new LinkedList<>();		
		for (ExprToken token: tokens) {
			
			if (token.isOp()) {
				char op = token.getOp();
				switch (op) {
				case '+':
					stack.push(pop(stack) + pop(stack));
					break;
				case '-':
					stack.push(-pop(stack) + pop(stack));
					break;
				case '*':
					stack.push(pop(stack) * pop(stack));
					break;
				case '/':
					double result = 1 / (pop(stack) / pop(stack));
					if (Double.isInfinite(result)) {
						throw new ArithmeticException("divisió per zero");
					}
					else if (Double.isNaN(result)) {
						throw new ArithmeticException("divisió de zero per zero");
					}
					
					stack.push(result);
					break;
				}
			}
			else {
				stack.push(token.getNumber());
			}			
		}
		
		if (stack.size() != 1) {
			throw new BadExpressionException("pila no buida");
		}
		
		return stack.pop();
	}
	
	private double pop(Deque<Double> stack) throws BadExpressionException {
		
		if (stack.isEmpty()) {
			throw new BadExpressionException("pila buida");
		}
		
		return stack.pop();
	}	

	// OPERATORS
	
	private enum Operator {
		ADD(1), SUBTRACT(2), MULTIPLY(3), DIVIDE(4);

		final int precedence;

		Operator(int p) {
			precedence = p;
		}
	}

	private static Map<Character, Operator> ops = new HashMap<Character, Operator>() {
		private static final long serialVersionUID = 1L;

		{
			put('+', Operator.ADD);
			put('-', Operator.SUBTRACT);
			put('*', Operator.MULTIPLY);
			put('/', Operator.DIVIDE);
		}
	};	
	
	private static boolean isHigerPrec(char op, char sub) {
		return (ops.containsKey(sub) && ops.get(sub).precedence >= ops.get(op).precedence);
	}	
	
	public static class BadExpressionException extends Exception {
		
		private static final long serialVersionUID = 1L;

		public BadExpressionException(String message) {
			super(message);
		}
		
		public BadExpressionException(String message, Throwable t) {
			super(message, t);
		}
	}
}
