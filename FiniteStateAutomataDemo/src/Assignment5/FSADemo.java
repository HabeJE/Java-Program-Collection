package Assignment5;

import java.util.Scanner;

public class FSADemo {	//Client class that contains main function. Creates a finite state automata, which accepts a grammar of ones and zeroes. Checks if a received string has an even numbers of both ones and zeroes, returning true if this is the case, and false if it is not.
	
	private static Scanner input = new Scanner(System.in);
	
	public static void main(String[] args) {
		
		
		System.out.println("-State Pattern Demo-");
		FiniteStateAutomata FSA = new FiniteStateAutomata();	//CREATE object representing FSA
		System.out.println("Please input a string of ones and zeroes: ");
		String string = input.nextLine();	//receive string input of ones and zeroes
		System.out.println("String recieved is: " + "'" + string + "'");
		System.out.println("Checking if string is within FSA language");
		boolean isAccepted = FSA.withinLanguage(string);	//Check if string is accepted by the language
		if (isAccepted == true) {	//Return this message if the string is in the language
			System.out.println("The string is accepted; it contains an even number of ones and zeroes");
		}
		else {	//Return this message if it isn't
			System.out.println("The string is rejected; it contains either an odd number of ones, zeroes, or both");
		}	
	}
}

class FiniteStateAutomata {	//Context participant and main class, which communicates to the client, maintaining  the current state of the Automata within the field 'currentState' and defining the interface methods oneInput() and zeroInput() for the client 
	//fields
	private PossibleStates currentState;	//field 'currentState' defines the state which the JEAHFiniteStateAutomata object is currently maintaining.
	//constructor
	public FiniteStateAutomata() {	//Constructor defines the initial state of this finite state automata, by setting the currentState as JEAHQZeroAndFinal, which is fittingly also the final state.
		this.setCurrentState(new QZeroAndFinal());
	}
	//methods
	public PossibleStates getCurrentState() {	//getter method for 'currentState'
		return currentState;
	}
	public void setCurrentState(PossibleStates currentState) {	//setter method for 'currentState'
		this.currentState = currentState;
	}
	private void oneInput() {	//oneInput() in the FiniteStateAutomata object communicates to currentState to execute the oneInput() method.
		currentState.oneInput(this);	//This will cause the currentState to change to a different state within the finite state automata based on a perceived input of the number one in the string within the context of which state the 'currentState' is.
	}
	private void zeroInput() {	//zeroInput() in the FiniteStateAutomata object communicates to currentState to execute the zeroInput()  method.
		currentState.zeroInput(this);	//This will cause the currentState to change to a different state within the finite state automata based on a perceived input of the number zero in the string within the context of which state the 'currentState' is.
	}
	public boolean withinLanguage(String string) {	//This boolean method is the actual check for if the received string is within the language of the automata.
		if (string.isEmpty()) {	//an empty string contains an even number of ones and zeroes, since zero (the number of inputs in the this string for both input one and input zero) is an even number. Return true.
			return true;
		}
		else if (string.matches("[01]+") == false) {	//If there are any other characters besides one and zero in the string, throws an exception.
			throw new IllegalArgumentException("This language only excepts a string of ones and zeroes. String: " + string + " contains unexpected inputs.");
		}
		for (int counter = 0; counter <= string.length() - 1; counter++)	
		{
			char c = string.charAt(counter);	//The method will go through and read each character in the string.
			if (c == '1') {	//if the character is one.
				this.oneInput();	//Relate it to the oneInput(), and execute that method for the FiniteStateAutomata object.
			}
			else {	//Otherwise, if the character is zero.
				this.zeroInput();	//Relate it to the zeroInput(), and execute that method for the FiniteStateAutomata object.
			}
		}
		if (currentState instanceof QZeroAndFinal) {	//The final state in this method is Q0, so if the currentState after all characters are read falls upon this state, then the string is accepted in the language. Return true.
			return true;
		}
		else {	//Otherwise, if the currentState falls on either Q1, Q2, or Q3 after the entire string is read, then the string is rejected from the language. Return false.
			return false;
		}
	}
}

interface PossibleStates {	//The State interface for all possible states in this automata, which are defined Q0, Q1, Q2, and Q3.
	void oneInput(FiniteStateAutomata context);	//method oneInput() represents the action performed when a 'one' is read from the string input.
	void zeroInput(FiniteStateAutomata context);	//method zeroInput() represents the actions performed when a 'zero' is read from the string input.
}

class QZeroAndFinal implements PossibleStates{	//Concrete State QZeroAndFinal represents initial and final state Q0, which will be accessed by the automata whenever the string is empty, or otherwise has an even number of both ones and zeroes.
	@Override
	public void oneInput(FiniteStateAutomata context) {	//An input of one on currentState Q0 will change the currentState of the Finite State Automata to state Q2
		System.out.println("Input of one at state zero, moving to state two.");
		context.setCurrentState(new QTwo());
	}
	@Override
	public void zeroInput(FiniteStateAutomata context) {	//An input of zero on current state Q0 will change the currentState of the FSA to state Q1.
		System.out.println("Input of zero at state zero, moving to state one.");
		context.setCurrentState(new QOne());
	}
}

class QOne implements PossibleStates{ //Concrete State QOne represents state Q1, which is accessed when all previously read inputs have an odd total of zero inputs, but an even total of one inputs.
	@Override
	public void oneInput(FiniteStateAutomata context) {	//An input of one on currentState Q1 will change the currentState of the FSA to the state Q3.
		System.out.println("Input of one at state one, moving to state three.");
		context.setCurrentState(new QThree());
	}
	@Override 
	public void zeroInput(FiniteStateAutomata context) { //An input of zero on currentState Q1 will change the currentState of the FSA to the state Q0.
		System.out.println("Input of zero at state one, moving to state zero.");
		context.setCurrentState(new QZeroAndFinal());
	}
}

class QTwo implements PossibleStates{	//Concrete State QTwo represents state Q2, which is accessed when all previously read inputs have an even total of zero inputs, but an odd total of one inputs.
	@Override 
	public void oneInput(FiniteStateAutomata context) {	//An input of one on currentState Q2 will change the currentState of the FSA to the state Q0.
		System.out.println("Input of one at state two, moving to state zero.");
		context.setCurrentState(new QZeroAndFinal());
	}
	@Override
	public void zeroInput(FiniteStateAutomata context) {	//An input of zero on currentState Q2 will change the currentState of the FSA to the state Q3.
		System.out.println("Input of zero at state two, moving to state three.");
		context.setCurrentState(new QThree());
	}
}

class QThree implements PossibleStates{	//Concrete State QThree represents state Q3, which is accessed when all previously read inputs have an odd total of both one and zero inputs.
	@Override
	public void oneInput(FiniteStateAutomata context) {	//An input of one on currentState Q3 will change the currentState of the FSA to the state Q1.
		System.out.println("Input of one at state three, moving to state one");
		context.setCurrentState(new QOne());
	}
	@Override 
	public void zeroInput(FiniteStateAutomata context) {	//An input of zero on currentState Q3 will change the currentState of the FSA to the state Q2.
		System.out.println("Input of zero at state three, moving to state two");
		context.setCurrentState(new QTwo());
	}
}