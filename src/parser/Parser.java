package parser;

import lexer.Token;
import lexer.Tokeniser;
import lexer.Token.TokenClass;

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
		expect(TokenClass.SC);
	}

	private void parseVarDeclRep() {
		// TODO: figure out when to stop recursing, with accepts
		// vardeclRep ::= vardecl vardeclRep | ε
		if (accept(TokenClass.INT, TokenClass.CHAR, TokenClass.VOID, TokenClass.STRUCT)) {
			parseVarDecl();
			parseVarDeclRep();
		}

	}

	private void parseVarDecl() {
		// vardecl ::= type IDENT (ε | arrayDecl)";"

		parseType();
		expect(TokenClass.IDENTIFIER);
		if (accept(TokenClass.SC)) {
			nextToken();
			return;
		} else {
			parseArrayDecl();
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
		} else {
			parseStmt();
			parseStmtRep();
		}
	}

	private void parseStmt() {
		/*
		 * stmt ::= block | "while" "(" exp ")" stmt # while loop | "if" "(" exp ")"
		 * stmt maybeElse # if then else | "return" maybeExp ";" # return | exp (assign
		 * | ε) ";" # assignment vs expression statement, e.g. a function call
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
			expect(TokenClass.SC);
		} else {
			parseExp();
			parseMaybeAssign();
		}

	}

	private void parseMaybeAssign() {
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
		// exp ::= noOpExp
		// | binaryOp

		// start set of NoOpExp is the same as that of binaryOp...
//		if(accept(TokenClass.LPAR,TokenClass.IDENTIFIER,TokenClass.INT_LITERAL,TokenClass.CHAR_LITERAL,TokenClass.STRING_LITERAL,TokenClass.MINUS,TokenClass.ASTERIX,TokenClass.SIZEOF,TokenClass.LPAR)) {
//			parseNoOpExp();
//		}else if () {
//			parseBinaryOp();
//		}
		/// the above also has the same start set...

		// binaryOp can just become NoOpExp, so lets just send ourselves there
		parseBinaryOp();

		// or maybe try-except programming...?
		error();// ?

	}

	private void parseNoOpExp() {
		/*
		 * noOpExp ::= "(" exp ")" | (IDENT | INT_LITERAL) | "-" exp | CHAR_LITERAL |
		 * STRING_LITERAL | arrayaccess | fieldaccess | valueat | funcall | sizeof |
		 * typecast
		 */
		// well we have ambiguity
		if (accept(TokenClass.LPAR)) {
			expect(TokenClass.LPAR);
			parseExp();
			expect(TokenClass.RPAR);
		} else if (accept(TokenClass.IDENTIFIER)) {
			if (lookAhead(1).tokenClass == TokenClass.LPAR) {
				parseFunCall();
			}
			expect(TokenClass.IDENTIFIER);
		} else if (accept(TokenClass.INT_LITERAL)) {
			expect(TokenClass.INT_LITERAL);
		} else if (accept(TokenClass.CHAR_LITERAL)) {
			expect(TokenClass.CHAR_LITERAL);
		} else if (accept(TokenClass.STRING_LITERAL)) {
			expect(TokenClass.STRING_LITERAL);
		} else if (accept(TokenClass.MINUS)) {
			expect(TokenClass.MINUS);
			parseExp();
		} else if (accept(TokenClass.ASTERIX)) {
			parseValueAt();
		} else if (accept(TokenClass.SIZEOF)) {
			parseSizeOf();
		} else if (accept(TokenClass.LPAR)) {
			parseTypeCast();
		} else {
			// arrayAcces and FieldAccess have the same start set, so lets make a
			// nonterminal for those
			parseArrayOrFieldAccess();
		}

	}

	private void parseBinaryOp() {
		// binaryOp ::= binaryOp2 ("||" binaryOp | ε)
		parseBinaryOp2();
		if (accept(TokenClass.OR)) {
			expect(TokenClass.OR);
			parseBinaryOp();
		}
	}

	private void parseBinaryOp2() {
		// binaryOp2 ::= binaryOp3 ("&&" binaryOp2 | ε)
		parseBinaryOp3();
		if (accept(TokenClass.AND)) {
			expect(TokenClass.AND);
			parseBinaryOp2();
		}
	}

	private void parseBinaryOp3() {
		// binaryOp3 ::= binaryOp4 (("!=" | "==") binaryOp3 | ε)
		parseBinaryOp4();
		if (accept(TokenClass.NE, TokenClass.EQ)) {
			nextToken();
			parseBinaryOp3();
		}
	}

	private void parseBinaryOp4() {
		// binaryOp4 ::= binaryOp5 ((">" | "<" | ">=" | "<=" ) binaryOp4 | ε)
		parseBinaryOp5();
		if (accept(TokenClass.GT, TokenClass.LT, TokenClass.GE, TokenClass.LE)) {
			nextToken();
			parseBinaryOp4();
		}
	}

	private void parseBinaryOp5() {
		// binaryOp5 ::= binaryOp6 (("+" | "-") binaryOp5 | ε)
		parseBinaryOp6();
		if (accept(TokenClass.PLUS, TokenClass.MINUS)) {
			nextToken();
			parseBinaryOp5();
		}
	}

	private void parseBinaryOp6() {
		// binaryOp6 ::= noOpExp (("/" | "*" | "%") binaryOp6 | ε)
		parseNoOpExp();
		if (accept(TokenClass.DIV, TokenClass.ASTERIX, TokenClass.REM)) {
			nextToken();
			parseBinaryOp6();
		}
	}

	private void parseFunCall() {
		// funcall ::= IDENT "(" maybeArgs ")"
		expect(TokenClass.IDENTIFIER);
		expect(TokenClass.LPAR);
		parseMaybeArgsCloseBrace();
	}

	private void parseMaybeArgsCloseBrace() {
		// maybeArgs ::= exp extraArg |ε
		// only ever called from funCall, so our follow set is ")"
		if (accept(TokenClass.RPAR)) {
			return;
		} else {
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

	private void parseArrayOrFieldAccess() {
		parseExp();
		if (accept(TokenClass.LSBR)) {
			parseArrayAccess();
		} else if (accept(TokenClass.DOT)) {
			parseFieldAccess();
		}

		error();// ?
	}

	private void parseArrayAccess() {
		// arrayaccess ::= exp "[" exp "]"
		// parseExp();
		// parseArrayOrFieldAccess parses Exp for us

		expect(TokenClass.LSBR);
		parseExp();
		expect(TokenClass.RSBR);
	}

	private void parseFieldAccess() {
		// fieldaccess ::= exp "." IDENT
		// parseExp();
		// parseArrayOrFieldAccess parses Exp for us
		expect(TokenClass.DOT);
		expect(TokenClass.IDENTIFIER);
	}

	private void parseValueAt() {
		// valueat ::= "*" exp
		expect(TokenClass.ASTERIX);
		parseExp();
	}

	private void parseSizeOf() {
		// sizeof ::= "sizeof" "(" type ")"
		expect(TokenClass.SIZEOF);
		expect(TokenClass.LPAR);
		parseType();
		expect(TokenClass.RPAR);
	}

	private void parseTypeCast() {
		// typecast ::= "(" type ")" exp
		expect(TokenClass.LPAR);
		parseType();
		expect(TokenClass.RPAR);
		parseExp();
	}

}
