// Starter code for Project 1

// Change this to your NetId
package dsa;

import java.util.ArrayDeque; 
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.*;
import java.lang.Long;
import java.lang.math;

/** Class to store a node of expression tree
    For each internal node, element contains a binary operator
    List of operators: +|*|-|/|%|^
    Other tokens: (|)
    Each leaf node contains an operand (long integer)
*/

public class Expression{
    public enum TokenType {  // NIL is a special token that can be used to mark bottom of stack
	PLUS, TIMES, MINUS, DIV, MOD, POWER, OPEN, CLOSE, NIL, NUMBER
    }
    
    public static class Token {
	TokenType token;
	int priority; // for precedence of operator
	Long number;  // used to store number of token = NUMBER
	String string;

	Token(TokenType op, int pri, String tok) {
	    token = op;
	    priority = pri;
	    number = null;
	    string = tok;
	}

	// Constructor for number.  To be called when other options have been exhausted.
	Token(String tok) {
	    token = TokenType.NUMBER;
	    number = Long.parseLong(tok);
	    string = tok;
	}
	
	boolean isOperand() { return token == TokenType.NUMBER; }

	public long getValue() {
	    return isOperand() ? number : 0;
	}

	public String toString() { return string; }
    }

    Token element;
    Expression left, right;

    // Create token corresponding to a string
    // tok is "+" | "*" | "-" | "/" | "%" | "^" | "(" | ")"| NUMBER
    // NUMBER is either "0" or "[-]?[1-9][0-9]*
    static Token getToken(String tok) {  // To do
	Token result;
	switch(tok) {
	case "+":
	    result = new Token(TokenType.PLUS, 1, tok);  // modify if priority of "+" is not 1
	    break;
	case "-":
	    result = new Token(TokenType.MINUS, 1, tok);
	    break;
	case "*":
	    result = new Token(TokenType.TIMES, 2, tok);
	    break;
	case "/":
	    result = new Token(TokenType.DIV, 2, tok);
	    break;
	case "%":
	    result = new Token(TokenType.MOD, 2, tok);
	    break;
	case "^":
	    result = new Token(TokenType.POWER, 3, tok);
	    break;
	case "(":
	    result = new Token(TokenType.OPEN, 0, tok);
	    break;
	case ")":
	    result = new Token(TokenType.CLOSE, 0, tok);
	    break;
	case "#":
		result = new Token(TokenType.NIL, 0, tok);
		break;
	case "0":
	case "1":
	case "2":
	case "3":
	case "4":
	case "5":
	case "6":
	case "7":
	case "8":
	case "9":
		result = new Token(tok);
		break;
	default:
	    result = new Token(tok);
	    break;
	}
	return result;
    }
    
    private Expression() {
	element = null;
    }
    
    private Expression(Token oper, Expression left, Expression right) {
	this.element = oper;
	this.left = left;
	this.right = right;
    }

    private Expression(Token num) {
	this.element = num;
	this.left = null;
	this.right = null;
    }

    // Given a list of tokens corresponding to an infix expression,
    // return its equivalent postfix expression as a list of tokens.
    public static List<Token> infixToPostfix(List<Token> exp) {  // To do
		//output list
		List<Token> q = new ArrayList<>();
		//operator stack
		ArrayDeque<Token> st = new ArrayDeque<>();
		
		//use NIL to mark bottom of the stack
		Token bottom = new Token(TokenType.NIL, 0, "#");
		st.push(bottom);
		
		//traverse entire infix expression one token at a time
		for (int i = 0; i < exp.size(); i++)
		{
			Token current = exp.get(i);
			Token top = st.peek();
		
			//current is an operand
			if (current.number != null)
				q.add(current);
			
			//stack is empty besides NIL marking bottom
			else if (top.token == TokenType.NIL){
				st.push(current);
			}
			
			//current is an open parentheses
			else if (current.token == TokenType.OPEN){
				st.push(current);
			}
		
			//current is an operator not ()
			else if (current.priority == 3 || current.priority == 2 || current.priority == 1)
			{
				while(top.token != TokenType.OPEN && top.priority >= current.priority){
					q.add(st.pop());
				}
				while(top.priority == current.priority)
				{
					q.add(st.pop());
				}
				st.push(current);
			}
		
			//current is close parentheses
			else if(current.token == TokenType.CLOSE)
			{
				top = st.peek();

				//output all inner operators
				while(top.token != TokenType.OPEN) 
				{
					q.add(st.pop());
					top = st.peek();
				}

				//discard open parentheses
				st.pop();
				top = st.peek();

				//output all leftover operators
				if (top.token != TokenType.NIL && top.token != TokenType.OPEN){
					q.add(st.pop());
				}
				
				
			}
		}
		
		//output all final operators left in stack
		while (!st.isEmpty() && st.peek().token != TokenType.NIL) 
			q.add(st.pop()); 

		return q;
    }


	public static Expression infixToExpression(List<Token> exp) {  // To do
		// operator deque
		ArrayDeque<Token> op = new ArrayDeque<>();
		// operand deque
		ArrayDeque<Expression> expTree = new ArrayDeque<>();
		// expression for left child
		Expression left = new Expression();
		// expression for right child
		Expression right = new Expression();
		// expression oper parent
		Token oper = null;
		//push nil to op stack
		Token nil = new Token(TokenType.NIL, 0, "#");
		op.push(nil);

		// index through list
		for (int i = 0; i < exp.size(); i++) {
			Token c = exp.get(i);
			
			// if c is a number then push to expTree deque
			if (c.token == TokenType.NUMBER) {
				expTree.push(new Expression(c));
				// if a number is the last item in list
				while (i == (exp.size() - 1) && op.size() > 1) {
					left = expTree.pop();
					right = expTree.pop();
					oper = op.pop();
					expTree.push(new Expression(oper, left, right));
				}
			}
			// if c is a closed )
			else if (c.token == TokenType.CLOSE) {
				// create tree
				while (op.peek().token != TokenType.OPEN) {
					left = expTree.pop();
					right = expTree.pop();
					oper = op.pop();
					expTree.push(new Expression(oper, left, right));
				}
				op.pop();
				if (op.size() > 1 && op.peek().token != TokenType.OPEN && expTree.size() > 1) {
					left = expTree.pop();
					right = expTree.pop();
					oper = op.pop();
					expTree.push(new Expression(oper, left, right));
				}
			}
			//else if c is an operator push to op deque
			else {
				// if c is greater priority or equal
				if (op.peek().priority <= c.priority || c.token == TokenType.OPEN) {
					op.push(c);
				}
				// if c is lesser priority then build tree from op deque and expTree deque
				else if (op.peek().priority > c.priority) {
					left = expTree.pop();
					right = expTree.pop();
					oper = op.pop();
					expTree.push(new Expression(oper, left, right));
					op.push(c);
				}
			}
		}
   		return expTree.pop();
    }

    // Given a postfix expression, evaluate it and return its value.
    public static long evaluatePostfix(List<Token> exp) {  // To do
		//operand stack
		ArrayDeque<Long> st = new ArrayDeque<>();

		//traverse each element in postfix expression
		for (int i = 0; i < exp.size(); i++)
		{
			Token current = exp.get(i);
			long a = 0;
			long b = 0;
		
			//current is an operand
			if (current.number != null)
				st.push(current.number);
			
			//current is an operator
			else
			{
				//get associated operands from stack
				b = st.pop();
				a = st.pop();

				//perform calculation based on operator and place result on operand stack
				if(current.token == TokenType.PLUS){
					st.push(a + b);
				}
				else if(current.token == TokenType.MINUS){
					st.push(a - b);
				}
				else if(current.token == TokenType.TIMES){
					st.push(a * b);
				}
				else if(current.token == TokenType.DIV){
					st.push(a / b);
				}
				else if(current.token == TokenType.MOD){
					st.push(a % b);
				}
				else if (current.token == TokenType.POWER){
					st.push((long)(Math.pow(a, b)));
				}
			}
		}
		
		return st.getFirst();
    }

    // Given an expression tree, evaluate it and return its value.
    public static long evaluateExpression(Expression tree) { // To do
		if (tree.element.number != null) {
			return tree.element.getValue();
		} else {
			long l = evaluateExpression(tree.left);
			long r = evaluateExpression(tree.right);
			if (tree.element.token == TokenType.PLUS) { 
				return (long)(l + r); 
			}
			else if (tree.element.token == TokenType.MINUS) { 
				return (long)(r - l); 
			}
			else if (tree.element.token == TokenType.TIMES) { 
				return (long)(l * r); 
			}
			else if (tree.element.token == TokenType.DIV) { 
				return (long)(r/l);
			}
			else if (tree.element.token == TokenType.MOD) { 
				return (long)(l % r); 
			}
			else if (tree.element.token == TokenType.POWER) {
				return (long)(Math.pow(r, l));
			}
		}
		return (long)1;
	}
    
	
    // sample main program for testing
    public static void main(String[] args) throws FileNotFoundException {
		Scanner in;
		
		if (args.length > 0) {
			File inputFile = new File(args[0]);
			in = new Scanner(inputFile);
		} else {
			in = new Scanner(System.in);
		}
	
		int count = 0;
		while(in.hasNext()) {
			String s = in.nextLine();
			List<Token> infix = new LinkedList<>();
			Scanner sscan = new Scanner(s);
			int len = 0;
			while(sscan.hasNext()) {
			infix.add(getToken(sscan.next()));
			len++;
			}
			// in.close();
			// sscan.close();
			if(len > 0) {
			count++;
			System.out.println("Expression number: " + count);
			System.out.println("Infix expression: " + infix);
			Expression exp = infixToExpression(infix);
			List<Token> post = infixToPostfix(infix);
			System.out.println("Postfix expression: " + post);
			long pval = evaluatePostfix(post);
			long eval = evaluateExpression(exp);
			System.out.println("Postfix eval: " + pval + " Exp eval: " + eval + "\n");
			}
		}
	}
}