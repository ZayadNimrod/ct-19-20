package parser;

import lexer.Token;
import lexer.Tokeniser;
import lexer.Token.TokenClass;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author cdubach
 */
public class Parser {

	private Token token;

	// use for backtracking (useful for distinguishing decls from procs when parsing
	// a program for instance)
	private Queue<Token> buffer = new LinkedList<>();

	private final Tokeniser tokeniser;

	public Parser(Tokeniser tokeniser) {
		this.tokeniser = tokeniser;
	}

	public void parse() {
		// get the first token
		nextToken();

		parseProgram();
	}

	public int getErrorCount() {
		return error;
	}

	private int error = 0;
	private Token lastErrorToken;

	private void error(TokenClass... expected) {

		if (lastErrorToken == token) {
			// skip this error, same token causing trouble
			return;
		}

		StringBuilder sb = new StringBuilder();
		String sep = "";
		for (TokenClass e : expected) {
			sb.append(sep);
			sb.append(e);
			sep = "|";
		}
		System.out.println("Parsing error: expected (" + sb + ") found (" + token + ") at " + token.position);
		error++;
		lastErrorToken = token;
	}

	/*
	 * Look ahead the i^th element from the stream of token. i should be >= 1
	 */
	private Token lookAhead(int i) {
		// ensures the buffer has the element we want to look ahead
		while (buffer.size() < i)
			buffer.add(tokeniser.nextToken());
		assert buffer.size() >= i;

		int cnt = 1;
		for (Token t : buffer) {
			if (cnt == i)
				return t;
			cnt++;
		}

		assert false; // should never reach this
		return null;
	}

	/*
	 * Consumes the next token from the tokeniser or the buffer if not empty.
	 */
	private void nextToken() {
		if (!buffer.isEmpty())
			token = buffer.remove();
		else
			token = tokeniser.nextToken();
	}

	/*
	 * If the current token is equals to the expected one, then skip it, otherwise
	 * report an error. Returns the expected token or null if an error occurred.
	 */
	private Token expect(TokenClass... expected) {
		for (TokenClass e : expected) {
			if (e == token.tokenClass) {
				Token cur = token;
				nextToken();
				return cur;
			}
		}

		error(expected);
		return null;
	}

	/*
	 * Returns true if the current token is equals to any of the expected ones.
	 */
	private boolean accept(TokenClass... expected) {
		boolean result = false;
		for (TokenClass e : expected)
			result |= (e == token.tokenClass);
		return result;
	}

	private void parseProgram() {
		parseIncludes();
		parseStructDeclRep();
		parseVarDeclRep();
		parseFunDeclRep();
		expect(TokenClass.EOF);
	}

	// includes are ignored, so does not need to return an AST node
	private void parseIncludes() {
		if (accept(TokenClass.INCLUDE)) {
			nextToken();
			expect(TokenClass.STRING_LITERAL);
			parseIncludes();
		}

	}

	private void parseStructDeclRep() {
		if (accept(TokenClass.STRUCT)) {
			parseStructDecl();
			parseStructDeclRep();
		}
	}

	private void parseStructDecl() {
		// structtype "{" vardecl vardeclRep "}" ";"
		parseStructType();
		expect(TokenClass.LBRA);

		parseVarDecl();
		parseVarDeclRep();
		expect(TokenClass.RBRA);
		System.out.println("Struct decl SC");
		expect(TokenClass.SC);
	}

	private void parseVarDeclRep() {
		// TODO: figure out when to stop recursing, with accepts
		// vardeclRep ::= vardecl vardeclRep | ε
		// check here is to check that this is a variable decleration rather than a
		// function declaration
		if (accept(TokenClass.INT, TokenClass.CHAR, TokenClass.VOID, TokenClass.STRUCT)
				&& lookAhead(2).tokenClass != TokenClass.LPAR) {

			parseVarDecl();
			parseVarDeclRep();
		}

	}

	private void parseVarDecl() {
		// vardecl ::= type IDENT (ε | arrayDecl)";"
		parseType();
		expect(TokenClass.IDENTIFIER);
		if (accept(TokenClass.SC)) {

			System.out.println("var decl SC");
			expect(TokenClass.SC);
			return;
		} else {
			parseArrayDecl();

			System.out.println("Var decl SC");
			expect(TokenClass.SC);
		}
	}

	private void parseArrayDecl() {
		// "[" INT_LITERAL "]"
		expect(TokenClass.LSBR);
		expect(TokenClass.INT_LITERAL);
		expect(TokenClass.RSBR);
	}

	private void parseFunDeclRep() {
		// funcdeclRep::= functdecl funcdeclRep | ε
		if (accept(TokenClass.INT, TokenClass.CHAR, TokenClass.VOID, TokenClass.STRUCT)) {
			parseFunDecl();
			parseFunDeclRep();
		}
	}

	private void parseFunDecl() {
		// fundecl ::= type IDENT "(" params ")" block # function declaration
		parseType();
		expect(TokenClass.IDENTIFIER);
		expect(TokenClass.LPAR);
		parseParams();
		expect(TokenClass.RPAR);
		parseBlock();
	}

	private void parseType() {
		// ("int" | "char" | "void" | structtype) optionalStar
		if (accept(TokenClass.INT, TokenClass.CHAR, TokenClass.VOID)) {
			nextToken();
		} else {
			// parse structType, else we fail
			parseStructType();
		}

		if (accept(TokenClass.ASTERIX)) {
			nextToken();
		}
		// not fussed if the star is there or not

	}

	private void parseStructType() {
		// "struct" IDENT
		expect(TokenClass.STRUCT);
		expect(TokenClass.IDENTIFIER);
	}

	private void parseParams() {
		// params ::= type IDENT extraParam | ε
		if (accept(TokenClass.INT, TokenClass.CHAR, TokenClass.VOID, TokenClass.STRUCT)) {
			parseType();
			expect(TokenClass.IDENTIFIER);
			parseExtraParam();
		}
	}

	private void parseExtraParam() {
		// extraParam ::= "," type IDENT extraParam | ε
		if (accept(TokenClass.COMMA)) {
			expect(TokenClass.COMMA);
			parseType();
			expect(TokenClass.IDENTIFIER);
			parseExtraParam();
		}
	}

	private void parseStmtRep() {
		// stmtRep ::= stmt stmtRep | ε
		// follow set of stmtRep is just "}", since it is right-recursive (is that a
		// term?) and only called from block, so check for that

		if (accept(TokenClass.RBRA)) {
			// do nothing
			return;
		}else if(accept(TokenClass.EOF)) {
			//we've been crashing after this recurses infitely after hitting EOF
			return;
		} else {
			parseStmt();
			parseStmtRep();
		}
	}

	private void parseStmt() {
		/*
		 * stmt ::= block 
		 * | "while" "(" exp ")" stmt # while loop 
		 * | "if" "(" exp ")" stmt maybeElse # if then else | "return" maybeExp ";" # return 
		 * | exp (assign | ε) ";" # assignment vs expression statement, e.g. a function call
		 */
		if (accept(TokenClass.LBRA)) {
			parseBlock();
		} else if (accept(TokenClass.WHILE)) {
			expect(TokenClass.WHILE);
			expect(TokenClass.LPAR);
			parseExp();
			expect(TokenClass.RPAR);
			parseStmt();
		} else if (accept(TokenClass.IF)) {
			expect(TokenClass.IF);
			expect(TokenClass.LPAR);
			parseExp();
			expect(TokenClass.RPAR);
			parseStmt();
			parseMaybeElse();
		} else if (accept(TokenClass.RETURN)) {
			expect(TokenClass.RETURN);
			parseMaybeExp();

			System.out.println("stmt SC");
			expect(TokenClass.SC);
		} else {
			parseExp();
			parseMaybeAssign();

			// System.out.print("STMT SC");
			expect(TokenClass.SC);
		}

	}

	private void parseMaybeAssign() {
		// needs to be going here, but it doesn't
		// (assign | ε)
		if (accept(TokenClass.ASSIGN)) {
			// assign :: "=" exp
			expect(TokenClass.ASSIGN);
			parseExp();
		}
	}

	private void parseMaybeElse() {
		// maybeElse ::= "else" stmt | ε
		if (accept(TokenClass.ELSE)) {
			expect(TokenClass.ELSE);
			parseStmt();
		}
	}

	private void parseMaybeExp() {
		// maybeExp ::= exp | ε
		// only place this is called from is stmt, our follow set is ";"
		if (accept(TokenClass.SC)) {
			return;
		} else {
			parseExp();
		}
	}

	private void parseBlock() {
		// block ::= "{" vardeclRep stmtRep "}"
		expect(TokenClass.LBRA);
		parseVarDeclRep();
		parseStmtRep();
		expect(TokenClass.RBRA);
	}

	private void parseExp() {

		// the scoreboard has an issue where we fail to parse an expression, so we enter
		// an infinte recursion loop and crasyh
		// and this is different from a "normal" failure to parse
		// I'm going to need to sort out the grammar, but for now, this should suffice
		// for most cases
		try {
			// TODO: for better debug, maybe get our current position?
			// dunno if I'll need this anymore, but it's better to be safe I guess
			/*
			 * exp ::= "(" exp ")" arrayOrFieldAccessOrBinaryOps 
			 * | identOrFunCall arrayOrFieldAccessOrBinaryOps 
			 * | INT_LITERAL arrayOrFieldAccessOrBinaryOps 
			 * | "-" exp arrayOrFieldAccessOrBinaryOps 
			 * | CHAR_LITERAL  arrayOrFieldAccessOrBinaryOps 
			 * | STRING_LITERAL arrayOrFieldAccessOrBinaryOps
			 * | valueat arrayOrFieldAccessOrBinaryOps 
			 * | funcall arrayOrFieldAccessOrBinaryOps 
			 * | sizeof arrayOrFieldAccessOrBinaryOps 
			 * | typecast arrayOrFieldAccessOrBinaryOps
			 * 
			 */
			if (accept(TokenClass.IDENTIFIER)) {
				parseIdentOrFunCall();
				parseArrayOrFieldAccessOrBinaryOps();
			} else if (accept(TokenClass.INT_LITERAL)) {
				expect(TokenClass.INT_LITERAL);
				parseArrayOrFieldAccessOrBinaryOps();
			} else if (accept(TokenClass.CHAR_LITERAL)) {
				expect(TokenClass.CHAR_LITERAL);
				parseArrayOrFieldAccessOrBinaryOps();
			} else if (accept(TokenClass.STRING_LITERAL)) {
				expect(TokenClass.STRING_LITERAL);
				parseArrayOrFieldAccessOrBinaryOps();
			} else if (accept(TokenClass.LPAR)) {
				parseBracketOrTypeCast();
				parseArrayOrFieldAccessOrBinaryOps();
			} else if (accept(TokenClass.MINUS)) {
				expect(TokenClass.MINUS);
				parseExp();
				parseArrayOrFieldAccessOrBinaryOps();
			} else if (accept(TokenClass.ASTERIX)) {
				parseValueAt();
				parseArrayOrFieldAccessOrBinaryOps();
			} else if (accept(TokenClass.SIZEOF)) {
				parseSizeOf();
				parseArrayOrFieldAccessOrBinaryOps();
			} else {
				error(TokenClass.IDENTIFIER,TokenClass.INT_LITERAL,TokenClass.CHAR_LITERAL,TokenClass.STRING_LITERAL,TokenClass.LPAR,TokenClass.MINUS,TokenClass.ASTERIX,TokenClass.SIZEOF);
				nextToken();
			}

		} catch (StackOverflowError e) {
			System.out.println("Failed to parse Expression due to infinite recursion\n");
			error();
			
			nextToken();
		}

	}

	private void parseValueAt() {
		// valueat ::= "*" exp # Value at operator (pointer indirection)
		expect(TokenClass.ASTERIX);
		parseExp();
	}

	private void parseIdentOrFunCall() {
		// identOrFunCall := IDENT (funCall | ε)
		expect(TokenClass.IDENTIFIER);
		if (accept(TokenClass.LPAR)) {
			parseFunCall();
		}
	}

	private void parseFunCall() {
		// funcall ::= "(" maybeArgs ")"
		expect(TokenClass.LPAR);
		parseMaybeArgs();
		expect(TokenClass.RPAR);
	}

	private void parseMaybeArgs() {
		// maybeArgs ::= exp extraArg |ε
		// if we encounter a closing paren, then we have reached the end of the args
		if (!accept(TokenClass.RPAR)) {
			parseExp();
			parseExtraArg();
		}
	}

	private void parseExtraArg() {
		// extrArg ::= "," exp extraArg | ε
		if (accept(TokenClass.COMMA)) {
			expect(TokenClass.COMMA);
			parseExp();
			parseExtraArg();
		}
	}

	private void parseSizeOf() {
		// sizeof ::= "sizeof" "(" type ")" # size of type
		expect(TokenClass.SIZEOF);
		expect(TokenClass.LPAR);
		parseType();
		expect(TokenClass.RPAR);
	}

	private void parseBracketOrTypeCast() {
		// bracketsOrtypeCast := "(" ( brackets | typeCast)
		expect(TokenClass.LPAR);
		if (accept(TokenClass.INT, TokenClass.CHAR, TokenClass.VOID, TokenClass.STRUCT)) {
			parseTypeCast();
		} else {
			parseBrackets();
		}
	}

	private void parseBrackets() {
		// brackets := exp ")"
		parseExp();
		expect(TokenClass.RPAR);

	}

	private void parseTypeCast() {
		// typecast ::= type ")" exp # type casting
		parseType();
		expect(TokenClass.RPAR);
		parseExp();
	}

	private void parseArrayOrFieldAccessOrBinaryOps() {
		// arrayOrFieldAccessOrBinaryOps :: = (arrayAccess | fieldAccess | binaryOps |
		// ε)
		if (accept(TokenClass.LSBR)) {
			parseArrayAccess();
		} else if (accept(TokenClass.DOT)) {
			parseFieldAccess();
		} else if (accept(TokenClass.GT, TokenClass.LT, TokenClass.GE, TokenClass.LE, TokenClass.NE, TokenClass.EQ,
				TokenClass.PLUS, TokenClass.MINUS, TokenClass.DIV, TokenClass.ASTERIX, TokenClass.REM, TokenClass.OR,
				TokenClass.AND)) {
			// ">" | "<" | ">=" | "<=" | "!=" | "==" | "+" | "-" | "/" | "*" | "%" | "||" |
			// "&&"
			parseBinaryOps();
		}
	}

	private void parseArrayAccess() {
		// arrayaccess ::= "[" exp "]" # array access
		expect(TokenClass.LSBR);
		parseExp();
		expect(TokenClass.RSBR);
	}

	private void parseFieldAccess() {
		// fieldaccess ::= "." IDENT # structure field member access
		expect(TokenClass.DOT);
		expect(TokenClass.IDENTIFIER);
	}

	private void parseBinaryOps() {
		//binaryOps := ( "||" |ε ) binaryOps2








		if(accept(TokenClass.OR)) {expect(TokenClass.OR);}
		parseBinaryOps2();
	}

	private void parseBinaryOps2() {
		// binaryOps2 := ("&&" | ε ) binaryOps3
		if (accept(TokenClass.AND)) {
			expect(TokenClass.AND);
		}
		parseBinaryOps3();
	}

	private void parseBinaryOps3() {
		// binaryOps3 := ( "!=" | "==" | ε) binaryOps4
		if (accept(TokenClass.NE)) {
			expect(TokenClass.NE);
		} else if (accept(TokenClass.EQ)) {
			expect(TokenClass.EQ);
		}
		parseBinaryOps4();

	}

	private void parseBinaryOps4() {
		// binaryOps4 := (">" | "<" | ">=" | "<=" | ε) binaryOps5
		if (accept(TokenClass.GT)) {
			expect(TokenClass.GT);
		} else if (accept(TokenClass.LT)) {
			expect(TokenClass.LT);
		} else if (accept(TokenClass.GE)) {
			expect(TokenClass.GE);
		} else if (accept(TokenClass.LE)) {
			expect(TokenClass.LE);
		}
		parseBinaryOps5();

	}
	
	private void parseBinaryOps5() {
		//:= ( "+" | "-" | ε ) binaryOps6()
		if (accept(TokenClass.PLUS)) {
			expect(TokenClass.PLUS);
		} else if (accept(TokenClass.MINUS)) {
			expect(TokenClass.MINUS);
		}
		
		parseBinaryOps6();
	}
	
	private void parseBinaryOps6() {
		//binaryOps6 := ( "/" | "*" | "%" | ε ) exp
		if (accept(TokenClass.DIV)) {
			expect(TokenClass.DIV);
		} else if (accept(TokenClass.ASTERIX)) {
			expect(TokenClass.ASTERIX);
		}else if (accept(TokenClass.REM)) {
			expect(TokenClass.REM);
		}
		
		parseExp();
	}
}
