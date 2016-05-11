package main;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.*;
import javax.swing.JFileChooser;
import nodes.*;
import nodes.conditional.ConditionNode;
import nodes.conditional.Equals;
import nodes.conditional.GreaterThen;
import nodes.conditional.LessThen;

/**
 * The parser and interpreter. The top level parse function, a main method for
 * testing, and several utility methods are provided. You need to implement
 * parseProgram and all the rest of the parser.
 */
public class Parser {

	/**
	 * Top level parse method, called by the World
	 */
	static RobotProgramNode parseFile(File code) {
		Scanner scan = null;
		try {
			scan = new Scanner(code);

			// the only time tokens can be next to each other is
			// when one of them is one of (){},;
			scan.useDelimiter("\\s+|(?=[{}(),;])|(?<=[{}(),;])");

			RobotProgramNode n = parseProgram(scan); // You need to implement this!!!

			scan.close();
			return n;
		} catch (FileNotFoundException e) {
			System.out.println("Robot program source file not found");
		} catch (ParserFailureException e) {
			System.out.println("Parser error:");
			System.out.println(e.getMessage());
			scan.close();
		}
		return null;
	}

	/** For testing the parser without requiring the world */

	public static void main(String[] args) {
		if (args.length > 0) {
			for (String arg : args) {
				File f = new File(arg);
				if (f.exists()) {
					System.out.println("Parsing '" + f + "'");
					RobotProgramNode prog = parseFile(f);
					System.out.println("Parsing completed ");
					if (prog != null) {
						System.out.println("================\nProgram:");
						System.out.println(prog);
					}
					System.out.println("=================");
				} else {
					System.out.println("Can't find file '" + f + "'");
				}
			}
		} else {
			while (true) {
				JFileChooser chooser = new JFileChooser(".");// System.getProperty("user.dir"));
				int res = chooser.showOpenDialog(null);
				if (res != JFileChooser.APPROVE_OPTION) {
					break;
				}
				RobotProgramNode prog = parseFile(chooser.getSelectedFile());
				System.out.println("Parsing completed");
				if (prog != null) {
					System.out.println("Program: \n" + prog);
				}
				System.out.println("=================");
			}
		}
		System.out.println("Done");
	}

	// Useful Patterns

	static Pattern NUMPAT = Pattern.compile("-?\\d+"); // ("-?(0|[1-9][0-9]*)");
	static Pattern OPENPAREN = Pattern.compile("\\(");
	static Pattern CLOSEPAREN = Pattern.compile("\\)");
	static Pattern OPENBRACE = Pattern.compile("\\{");
	static Pattern CLOSEBRACE = Pattern.compile("\\}");

	// New Patterns
	static Pattern STATEMENT = Pattern.compile("loop|if|while");
	static Pattern ACT = Pattern.compile("move|wait|turnAround|turnL|turnR|shieldOn|shieldOff|takeFuel");
	static Pattern LOOP = Pattern.compile("loop\\{");
	
	static Pattern VARIABLE = Pattern.compile("\\$[A-Za-z][A-Za-z0-9]*");
	static Pattern SEN  = Pattern.compile("fuelLeft|oppLR|oppFB|numBarrels|barrelLR|barrelFB|wallDist");
	static Pattern NUM  = Pattern.compile("-?[0-9]+");

	/**
	 * PROG ::= STMT+
	 */
	static RobotProgramNode parseProgram(Scanner s) {
		// stores all of the nodes
		List<RobotProgramNode> nodes = new ArrayList<RobotProgramNode>(); 

		while(s.hasNext()){
			nodes.add(parseNode(s));		 
		}	
		return new ProgramNode(nodes);
	}



	static RobotProgramNode parseNode(Scanner s){
		RobotProgramNode node = null;

		if(s.hasNext(ACT)){
			node = parseAct(s);
			
		}else if(s.hasNext(STATEMENT)){
			node = parseStatement(s);
		}

		return node;
	}

	static LoopNode parseLoop(Scanner s){
		require("loop","requries 'loop' to parse", s);
		BlockNode block =  parseBlock(s);
		return new LoopNode(block);
	}

	static IfNode parseIf(Scanner s){
		require("if","requries 'if' to parse", s);
		require(OPENPAREN,"requries '(' before condition", s);
		ConditionNode condition = parseCondition(s);
		require(CLOSEPAREN,"requries ')' after condition", s);

		BlockNode block =  parseBlock(s);
		return new IfNode(block, condition);
	}

	static WhileNode parseWhile(Scanner s){
		require("while","requries 'while' to parse", s);
		require(OPENPAREN,"requries '(' before condition", s);
		ConditionNode condition = parseCondition(s);
		require(CLOSEPAREN,"requries ')' after condition", s);

		BlockNode block =  parseBlock(s);
		return new WhileNode(block, condition);
	}

	static ConditionNode parseCondition(Scanner s){
		String oppType = "";

		if(s.hasNext("lt")){
			oppType = "lt";		

		}else if(s.hasNext("gt")){
			oppType = "gt";		

		}else if(s.hasNext("eq")){
			oppType = "eq";		

		}else{
			fail("Incorrect opperator for condition",s);
		}
		
		require(oppType, "Requires " + oppType + " to pass a condition",s);
		require(OPENPAREN, "Requires '(' to pass a condition",s);

		SensorNode sensor = parseSensor(s);
		require(",", "Requires ',' between values in a condition",s);
		int num = parseNum(s);
		require(CLOSEPAREN, "Requires ')' to pass a condition",s);

		ConditionNode node = null;

		switch(oppType){
		case "lt" : node = new LessThen(sensor,num); break;
		case "gt" : node = new GreaterThen(sensor,num); break;
		case "eq" : node = new Equals(sensor,num); break;
		default: fail("Something went wrong trying to create a condition node", s);
		}

		return node;
	}

	static SensorNode parseSensor(Scanner s){

		if(s.hasNext("fuelLeft")){
			require("fuelLeft","requries 'fuelLeft' to parse", s);
			return new SensorNode.FuelLeft();

		}else if(s.hasNext("oppLR")){
			require("oppLR","requries 'oppLR' to parse", s);
			return new SensorNode.OppLR();

		}else if(s.hasNext("oppFB")){
			require("oppFB","requries 'oppFB' to parse", s);
			return new SensorNode.OppFB();

		}else if(s.hasNext("numBarrels")){
			require("numBarrels","requries 'numBarrels' to parse", s);
			return new SensorNode.NumBarrels();

		}else if(s.hasNext("barrelLR")){
			require("barrelLR","requries 'barrelLR' to parse", s);
			return new SensorNode.BarrelLR();

		}else if(s.hasNext("barrelFB")){
			require("barrelFB","requries 'barrelFB' to parse", s);
			return new SensorNode.BarrelFB();

		}else if(s.hasNext("wallDist")){
			require("wallDist","requries 'wallDist' to parse", s);
			return new SensorNode.WallDistance();
		}else{
			fail("No sensor to pass 1" ,s);
			return null;
		}
	}

	static int parseNum(Scanner s){
		if(s.hasNextInt())
			return s.nextInt();
		else 
			fail("No Integer to pass",s);

		return 0;
	}

	static BlockNode parseBlock(Scanner s){
		List<RobotProgramNode> nodes = new ArrayList<RobotProgramNode>();

		require(OPENBRACE, "Requires an opening '{' to be a block",s);
		while(s.hasNext(ACT) || s.hasNext(STATEMENT)){
			nodes.add(parseStatement(s));
		}
		require(CLOSEBRACE, "Requires a closing '}' to be a block",s);

		return new BlockNode(nodes);
	}

	static RobotProgramNode parseStatement(Scanner s){
		RobotProgramNode node = null;

		if(s.hasNext(ACT)){
			node = parseAct(s);
			
		}else if(s.hasNext("loop")){
			node = parseLoop(s);	
			
		}else if(s.hasNext("if")){
			node = parseIf(s);
			
		}else if(s.hasNext("while")){
			node = parseWhile(s);
			
		}else{
			fail("No statement to pass",s);
		}

		return node;
	}

	static ActNode parseAct(Scanner s){

		ActNode node = null;

		if(s.hasNext("move")){
			require("move", "Requires 'move' to parse",s);
			node = new ActNode.Move();

		}else if(s.hasNext("turnL")){
			require("turnL", "Requires 'turnL' to to parse",s);
			node = new ActNode.TurnL();

		}else if(s.hasNext("turnR")){
			require("turnR", "Requires 'turnR' to to parse",s);
			node =  new ActNode.TurnR();
		
		}else if(s.hasNext("turnAround")){
			require("turnAround", "Requires 'turnAround' to to parse",s);
			node =  new ActNode.TurnAround();

		}else if(s.hasNext("shieldOn")){
			require("shieldOn", "Requires 'sheildOn' to to parse",s);
			node =  new ActNode.ShieldOn();
			
		}else if(s.hasNext("shieldOff")){
			require("shieldOff", "Requires 'shiedOff' to to parse",s);
			node =  new ActNode.ShieldOff();
			
		}else if(s.hasNext("takeFuel")){
			require("takeFuel", "Requires 'takeFuel' to to parse",s);
			node =  new ActNode.TakeFuel();

		}else if(s.hasNext("wait")){
			require("wait", "Requires 'wait' to to parse",s);
			node =  new ActNode.Wait();

		}else{
			fail("No act to parse" , s);
		}

		require(";", "Requires a semicolon after each act", s);
		return node;
	}




	// utility methods for the parser

	/**
	 * Report a failure in the parser.
	 */
	static void fail(String message, Scanner s) {
		String msg = message + "\n   @ ...";
		for (int i = 0; i < 10 && s.hasNext(); i++) {
			msg += " " + s.next();
		}
		throw new ParserFailureException(msg + "...");
	}

	/**
	 * Requires that the next token matches a pattern if it matches, it consumes
	 * and returns the token, if not, it throws an exception with an error
	 * message
	 */
	static String require(String p, String message, Scanner s) {
		if (s.hasNext(p)) {
			return s.next(p);
		}
		fail(message, s);
		return null;
	}

	static String require(Pattern p, String message, Scanner s) {
		if (s.hasNext(p)) {
			return s.next(p);
		}
		fail(message, s);
		return null;
	}

	/**
	 * Requires that the next token matches a pattern (which should only match a
	 * number) if it matches, it consumes and returns the token as an integer if
	 * not, it throws an exception with an error message
	 */
	static int requireInt(String p, String message, Scanner s) {
		if (s.hasNext(p) && s.hasNextInt()) {
			return s.nextInt();
		}
		fail(message, s);
		return -1;
	}

	static int requireInt(Pattern p, String message, Scanner s) {
		if (s.hasNext(p) && s.hasNextInt()) {
			return s.nextInt();
		}
		fail(message, s);
		return -1;
	}

	/**
	 * Checks whether the next token in the scanner matches the specified
	 * pattern, if so, consumes the token and return true. Otherwise returns
	 * false without consuming anything.
	 */
	static boolean checkFor(String p, Scanner s) {
		if (s.hasNext(p)) {
			s.next();
			return true;
		} else {
			fail("Method checkFor() could not match that string", s);
			return false;
		}
	}

	static boolean checkFor(Pattern p, Scanner s) {
		if (s.hasNext(p)) {
			s.next();
			return true;
		} else {
			return false;
		}
	}

}

// You could add the node classes here, as long as they are not declared public (or private)



