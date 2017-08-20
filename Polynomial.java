package poly;

import java.io.*;
import java.util.StringTokenizer;

/**
 * This class implements a term of a polynomial.
 * 
 * @author runb-cs112
 *
 */
class Term {
	/**
	 * Coefficient of term.
	 */
	public float coeff;
	
	/**
	 * Degree of term.
	 */
	public int degree;
	
	/**
	 * Initializes an instance with given coefficient and degree.
	 * 
	 * @param coeff Coefficient
	 * @param degree Degree
	 */
	public Term(float coeff, int degree) {
		this.coeff = coeff;
		this.degree = degree;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		return other != null &&
		other instanceof Term &&
		coeff == ((Term)other).coeff &&
		degree == ((Term)other).degree;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (degree == 0) {
			return coeff + "";
		} else if (degree == 1) {
			return coeff + "x";
		} else {
			return coeff + "x^" + degree;
		}
	}
}

/**
 * This class implements a linked list node that contains a Term instance.
 * 
 * @author runb-cs112
 *
 */
class Node {
	
	/**
	 * Term instance. 
	 */
	Term term;
	
	/**
	 * Next node in linked list. 
	 */
	Node next;
	
	/**
	 * Initializes this node with a term with given coefficient and degree,
	 * pointing to the given next node.
	 * 
	 * @param coeff Coefficient of term
	 * @param degree Degree of term
	 * @param next Next node
	 */
	public Node(float coeff, int degree, Node next) {
		term = new Term(coeff, degree);
		this.next = next;
	}
}

/**
 * This class implements a polynomial.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Pointer to the front of the linked list that stores the polynomial. 
	 */ 
	Node poly;
	
	/** 
	 * Initializes this polynomial to empty, i.e. there are no terms.
	 *
	 */
	public Polynomial() {
		poly = null;
	}
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param br BufferedReader from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 */
	public Polynomial(BufferedReader br) throws IOException {
		String line;
		StringTokenizer tokenizer;
		float coeff;
		int degree;
		
		poly = null;
		
		while ((line = br.readLine()) != null) {
			tokenizer = new StringTokenizer(line);
			coeff = Float.parseFloat(tokenizer.nextToken());
			degree = Integer.parseInt(tokenizer.nextToken());
			poly = new Node(coeff, degree, poly);
		}
	}
	
	
	/**
	 * Returns the polynomial obtained by adding the given polynomial p
	 * to this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial to be added
	 * @return A new polynomial which is the sum of this polynomial and p.
	 */
	public Polynomial add(Polynomial p) {
		/** COMPLETE THIS METHOD **/
		
		Polynomial beingAdded = this;
		Polynomial total = new Polynomial();
		
		if (p.poly == null) {
			total = beingAdded;
			return total;
		}
		
		else if (beingAdded.poly == null) {
			total = p;
			return total;
		}
		
		else {
			total.poly = new Node (0,0,null);
			
			Node front = total.poly;
			
			Node ptr = beingAdded.poly;
			Node ptr2 = p.poly;
			
			while (ptr2 != null || ptr != null) {
				if ((ptr2 != null && ptr != null) && (ptr.term.degree == ptr2.term.degree)) {
					float totalCoeff = ptr.term.coeff + ptr2.term.coeff;
					int totalDegree = ptr2.term.degree;
					total.poly.term = new Term (totalCoeff, totalDegree);
				
					ptr = ptr.next;
					ptr2 = ptr2.next;
				}
				
				else {
					if (ptr2 != null && ((ptr != null) || ptr2.term.degree < ptr.term.degree)) {
						total.poly.term = ptr2.term;
						ptr2 = ptr2.next;
					}
					
					else {
						total.poly.term = ptr.term;
						ptr = ptr.next;
					}
				}
				
				total.poly.next = new Node (0,0,null);
				total.poly = total.poly.next;
			}
			

			Node prev = null;
			Node ptr3 = front;

			while (ptr3 != null) {
				if (ptr3.term.coeff == 0) {
					ptr3 = ptr3.next;
					
					if (prev == null) {
						prev = ptr3;
					} 
					
					else {
						prev.next = ptr3;
					}
				} 
				
				else {
					prev = ptr3;
					ptr3 = prev.next;
				}
			}

			total.poly = front;
			
			if (total.poly.next.term.coeff == 0 && total.poly.term.coeff == 0) {
				Polynomial zerosOut = new Polynomial();
				zerosOut.poly = new Node (0, 0, null);
				return zerosOut;
			}
			
			else {
				return total;
			}
		}
	}
	/**
	 * Returns the polynomial obtained by multiplying the given polynomial p
	 * with this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial with which this polynomial is to be multiplied
	 * @return A new polynomial which is the product of this polynomial and p.
	 */
	public Polynomial multiply(Polynomial p) {
		/** COMPLETE THIS METHOD **/
		Polynomial beingMultiplied = this;
		
		if (beingMultiplied.poly == null || p.poly == null) {
			Polynomial start = new Polynomial();
			start.poly = new Node (0, 0, null);
			return start;
		}
		
		else {
			Polynomial result = new Polynomial();
			result.poly = new Node (0, 0, null);
			
			int high = 0;
			int low = 99999999;
			
			Node ptr = beingMultiplied.poly;
			Node ptr2 = p.poly;
			Node front = result.poly;
			
			while (ptr2 != null) {
				ptr = this.poly; 
				
				while (ptr != null) {
					if (ptr.term.degree + ptr2.term.degree > high) {
						high = ptr.term.degree + ptr2.term.degree;
					}
					
					if (ptr.term.degree + ptr2.term.degree < low) {
						low = ptr.term.degree + ptr2.term.degree;
					}
					
					ptr = ptr.next;
				}
				
				ptr2 = ptr2.next;
			}
			
			ptr2 = p.poly;
			
			Node make = front;
			for (int i = low; i <= high; i++) {
				make.term.coeff = 0;
				make.term.degree = i;
				
				make.next = new Node (0, 0, null);
				make = make.next;
			}
			
			ptr2 = p.poly;
			
			while (ptr2 != null) {
				ptr = this.poly; 
				
				while (ptr != null) {
					int degree = ptr2.term.degree + ptr.term.degree;
					make = front;
					
					while (make != null) {
						if (make.term.degree == degree) {
							make.term.coeff = ptr2.term.coeff * ptr.term.coeff;
						}
						
						make = make.next;
					}
					
					ptr = ptr.next;
				}
				
				ptr2 = ptr2.next;
			}
			
			make = front;
			
			while (make != null) {
				if (make.term.degree == high) {
					make.next = null;
					make = make.next;
				}
				else
					make = make.next;
			}
			
			result.poly = front;
			
			return result;
		}
	}
	
	/**
	 * Evaluates this polynomial at the given value of x
	 * 
	 * @param x Value at which this polynomial is to be evaluated
	 * @return Value of this polynomial at x
	 */
	public float evaluate(float x) {
		/** COMPLETE THIS METHOD **/
		Polynomial beingEvaluated = this;
		float result = 0;
		for (Node ptr = beingEvaluated.poly; ptr != null; ptr = ptr.next) {
			result = result + (ptr.term.coeff * (float)Math.pow(x, ptr.term.degree));
			
		}
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String retval;
		
		if (poly == null) {
			return "0";
		} else {
			retval = poly.term.toString();
			for (Node current = poly.next ;
			current != null ;
			current = current.next) {
				retval = current.term.toString() + " + " + retval;
			}
			return retval;
		}
	}
}
