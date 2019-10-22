package parser;

import ast.*;

import lexer.Token;
import lexer.Tokeniser;
import lexer.Token.TokenClass;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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

	public Program parse() {
		// get the first token
		nextToken();

		return parseProgram();
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

	private Program parseProgram() {
		parseIncludes();
		List<StructTypeDecl> structs = parseStructDeclRep();
		List<VarDecl> vars = parseVarDeclRep();
		List<FunDecl> funs = parseFunDeclRep();
		expect(TokenClass.EOF);
		return new Program(structs, vars, funs);
	}

	// includes are ignored, so does not need to return an AST node
	private void parseIncludes() {
		if (accept(TokenClass.INCLUDE)) {
			nextToken();
			expect(TokenClass.STRING_LITERAL);
			parseIncludes();
		}

	}

	private List<StructTypeDecl> parseStructDeclRep() {
		List<StructTypeDecl> ret = new LinkedList<StructTypeDecl>();
		if (accept(TokenClass.STRUCT)) {
			ret.add(parseStructDecl());
			ret.addAll(parseStructDeclRep());
		}
		return ret;
	}

	private StructTypeDecl parseStructDecl() {
		// structtype "{" vardecl vardeclRep "}" ";"
		StructType structType = parseStructType();
		expect(TokenClass.LBRA);

		List<VarDecl> varDecls = new LinkedList<VarDecl>();
		varDecls.add(parseVarDecl());
		varDecls.addAll(parseVarDeclRep());

		expect(TokenClass.RBRA);
		expect(TokenClass.SC);

		return new StructTypeDecl(structType, varDecls);
	}

	private List<VarDecl> parseVarDeclRep() {
		//
		// vardeclRep ::= vardecl vardeclRep | ε
		// check here is to check that this is a variable decleration rather than a
		// function declaration
		List<VarDecl> ret = new LinkedList<VarDecl>();
		// this accept crashes sometimes?
		// System.out.print(token);
		if (accept(TokenClass.INT, TokenClass.CHAR, TokenClass.VOID, TokenClass.STRUCT)) {
			// System.out.print("L");
			Token look = lookAhead(2);
			if (look != null) {
				// System.out.print("N");
				// should this be LPAR
				if (look.tokenClass != TokenClass.LPAR) {
					ret.add(parseVarDecl());
					ret.addAll(parseVarDeclRep());
				}
			}
		}
		return ret;

	}

	private VarDecl parseVarDecl() {
		// vardecl ::= type IDENT (ε | arrayDecl)";"
		Type type = parseType();
		String varname = token.data;
		expect(TokenClass.IDENTIFIER);
		if (accept(TokenClass.SC)) {
			expect(TokenClass.SC);
		} else if (accept(TokenClass.LSBR)) {
			int len = parseArrayDecl();
			expect(TokenClass.SC);
			type = new ArrayType(type, len);
		} else {
			error(TokenClass.SC, TokenClass.LSBR);
		}

		return new VarDecl(type, varname);

	}

	private int parseArrayDecl() {
		// "[" INT_LITERAL "]"
		int ret = 0;
		expect(TokenClass.LSBR);
		if (accept(TokenClass.INT_LITERAL)) {
			ret = Integer.valueOf(token.data);
		}
		expect(TokenClass.INT_LITERAL);
		expect(TokenClass.RSBR);
		return ret;
	}

	private List<FunDecl> parseFunDeclRep() {
		// funcdeclRep::= functdecl funcdeclRep | ε

		List<FunDecl> ret = new LinkedList<FunDecl>();
		if (accept(TokenClass.INT, TokenClass.CHAR, TokenClass.VOID, TokenClass.STRUCT)) {
			ret.add(parseFunDecl());
			ret.addAll(parseFunDeclRep());
		}

		return ret;
	}

	private FunDecl parseFunDecl() {
		// fundecl ::= type IDENT "(" params ")" block # function declaration
		Type type = parseType();
		String name = token.data;
		expect(TokenClass.IDENTIFIER);
		expect(TokenClass.LPAR);
		List<VarDecl> params = parseParams();
		expect(TokenClass.RPAR);
		Block block = parseBlock();
		return new FunDecl(type, name, params, block);
	}

	private Type parseType() {
		// ("int" | "char" | "void" | structtype) optionalStar
		Type ret = null;
		if (accept(TokenClass.INT)) {
			nextToken();
			ret = BaseType.INT;
		} else if (accept(TokenClass.CHAR)) {
			nextToken();
			ret = BaseType.CHAR;
		} else if (accept(TokenClass.VOID)) {
			nextToken();
			ret = BaseType.VOID;
		} else {
			// parse structType, else we fail
			ret = parseStructType();
		}

		if (accept(TokenClass.ASTERIX)) {
			// this is a pointer
			ret = new PointerType(ret);
			expect(TokenClass.ASTERIX);
		}
		return ret;

	}

	private StructType parseStructType() {
		// "struct" IDENT
		expect(TokenClass.STRUCT);
		StructType ret = new StructType(token.data);
		expect(TokenClass.IDENTIFIER);
		return ret;
	}

	private List<VarDecl> parseParams() {
		// params ::= type IDENT extraParam | ε
		List<VarDecl> ret = new LinkedList<VarDecl>();
		if (accept(TokenClass.INT, TokenClass.CHAR, TokenClass.VOID, TokenClass.STRUCT)) {
			Type t = parseType();
			String id = token.data;
			expect(TokenClass.IDENTIFIER);
			ret.add(new VarDecl(t, id));
			ret.addAll(parseExtraParam());
		}
		return ret;
	}

	private List<VarDecl> parseExtraParam() {
		// extraParam ::= "," type IDENT extraParam | ε
		List<VarDecl> ret = new LinkedList<VarDecl>();
		if (accept(TokenClass.COMMA)) {
			expect(TokenClass.COMMA);
			Type t = parseType();
			String id = token.data;
			expect(TokenClass.IDENTIFIER);
			ret.add(new VarDecl(t, id));
			ret.addAll(parseExtraParam());
		}
		return ret;
	}

	private List<Stmt> parseStmtRep() {
		// stmtRep ::= stmt stmtRep | ε
		// follow set of stmtRep is just "}", since it is right-recursive (is that a
		// term?) and only called from block, so check for that
		List<Stmt> ret = new LinkedList<Stmt>();
		if (accept(TokenClass.RBRA)) {
			// do nothing
			return ret;
		} else if (accept(TokenClass.EOF)) {
			// we've been crashing after this recurses infinitely after hitting EOF
			return ret;
		} else {
			ret.add(parseStmt());
			ret.addAll(parseStmtRep());
		}
		return ret;
	}

	private Stmt parseStmt() {
		/*
		 * stmt ::= block | "while" "(" exp ")" stmt # while loop | "if" "(" exp ")"
		 * stmt maybeElse # if then else | "return" maybeExp ";" # return | exp (assign
		 * | ε) ";" # assignment vs expression statement, e.g. a function call
		 */
		Stmt ret = null;
		if (accept(TokenClass.LBRA)) {
			ret = parseBlock();
		} else if (accept(TokenClass.WHILE)) {
			expect(TokenClass.WHILE);
			expect(TokenClass.LPAR);
			Expr e = parseExp();
			expect(TokenClass.RPAR);
			Stmt c = parseStmt();
			ret = new While(e, c);
		} else if (accept(TokenClass.IF)) {
			expect(TokenClass.IF);
			expect(TokenClass.LPAR);
			Expr ex = parseExp();
			expect(TokenClass.RPAR);
			Stmt code = parseStmt();
			Stmt el = parseMaybeElse();
			ret = new If(ex, code, el);
		} else if (accept(TokenClass.RETURN)) {
			expect(TokenClass.RETURN);
			ret = new Return(parseMaybeExp());
			expect(TokenClass.SC);
		} else {
			Expr e = parseExp();
			Expr o = parseMaybeAssign();
			expect(TokenClass.SC);
			if (o == null) {
				ret = new ExprStmt(e);
			} else {
				ret = new Assign(e, o);
			}
		}
		return ret;

	}

	private Expr parseMaybeAssign() {
		// needs to be going here, but it doesn't
		// (assign | ε)
		if (accept(TokenClass.ASSIGN)) {
			// assign :: "=" exp
			expect(TokenClass.ASSIGN);
			return parseExp();
		}
		return null;
	}

	private Stmt parseMaybeElse() {
		// maybeElse ::= "else" stmt | ε
		if (accept(TokenClass.ELSE)) {
			expect(TokenClass.ELSE);
			return parseStmt();
		}
		return null;
	}

	private Expr parseMaybeExp() {
		// maybeExp ::= exp | ε
		// only place this is called from is stmt, our follow set is ";"
		if (accept(TokenClass.SC)) {
			return null;
		} else {
			return parseExp();
		}
	}

	private Block parseBlock() {
		// block ::= "{" vardeclRep stmtRep "}"
		expect(TokenClass.LBRA);
		List<VarDecl> varDecls = parseVarDeclRep();
		List<Stmt> code = parseStmtRep();
		expect(TokenClass.RBRA);
		return new Block(varDecls, code);
	}

	private Expr parseExp() {
		return parseExp(true);
	}

	private Expr parseExp(boolean binOps) {

		// the scoreboard has an issue where we fail to parse an expression, so we enter
		// an infinte recursion loop and crasyh
		// and this is different from a "normal" failure to parse
		try {

			// dunno if I'll need this try-catch anymore, but it's better to be safe I guess

			// exp ::= "(" exp ")" arrayOrFieldAccessOrBinaryOps
			// | identOrFunCall arrayOrFieldAccessOrBinaryOps
			// | INT_LITERAL arrayOrFieldAccessOrBinaryOps
			// |"-" exp arrayOrFieldAccessOrBinaryOps
			// | CHAR_LITERAL arrayOrFieldAccessOrBinaryOps
			// | STRING_LITERAL arrayOrFieldAccessOrBinaryOps
			// | valueat arrayOrFieldAccessOrBinaryOps
			// | funcall arrayOrFieldAccessOrBinaryOps
			// | sizeof arrayOrFieldAccessOrBinaryOps
			// | typecast arrayOrFieldAccessOrBinaryOps
			//
			Expr ret = null;
			if (accept(TokenClass.IDENTIFIER)) {
				ret = parseIdentOrFunCall();
			} else if (accept(TokenClass.INT_LITERAL)) {
				ret = new IntLiteral(Integer.parseInt(token.data));
				expect(TokenClass.INT_LITERAL);
			} else if (accept(TokenClass.CHAR_LITERAL)) {
				ret = new ChrLiteral(token.data.charAt(0));
				expect(TokenClass.CHAR_LITERAL);
			} else if (accept(TokenClass.STRING_LITERAL)) {
				ret = new StrLiteral(token.data);
				expect(TokenClass.STRING_LITERAL);
			} else if (accept(TokenClass.LPAR)) {
				ret = parseBracketOrTypeCast();
			} else if (accept(TokenClass.MINUS)) {
				expect(TokenClass.MINUS);
				ret = new BinOp(new IntLiteral(0), Op.SUB, parseExp());
			} else if (accept(TokenClass.ASTERIX)) {
				ret = parseValueAt();
			} else if (accept(TokenClass.SIZEOF)) {
				ret = parseSizeOf();
			} else {
				error(TokenClass.IDENTIFIER, TokenClass.INT_LITERAL, TokenClass.CHAR_LITERAL, TokenClass.STRING_LITERAL,
						TokenClass.LPAR, TokenClass.MINUS, TokenClass.ASTERIX, TokenClass.SIZEOF);
				nextToken();
				return null;// doesn't matter what we return for ther AST since there is a parsing error
			}

			ret = parseArrayOrFieldAccessOrBinaryOps(ret, binOps);
			return ret;
		} catch (StackOverflowError e) {
			System.out.println("Failed to parse Expression due to infinite recursion\n");
			error();

			nextToken();
			return null;
		}

	}

	private ValueAtExpr parseValueAt() {
		// valueat ::= "*" exp # Value at operator (pointer indirection)
		expect(TokenClass.ASTERIX);
		return new ValueAtExpr(parseExp());
	}

	private Expr parseIdentOrFunCall() {
		// identOrFunCall := IDENT (funCall | ε)
		String id = token.data;
		expect(TokenClass.IDENTIFIER);
		if (accept(TokenClass.LPAR)) {
			List<Expr> args = parseFunCall();
			return new FunCallExpr(id, args);
		}
		return new VarExpr(id);
	}

	private List<Expr> parseFunCall() {
		// funcall ::= "(" maybeArgs ")"
		expect(TokenClass.LPAR);
		List<Expr> args = parseMaybeArgs();
		expect(TokenClass.RPAR);
		return args;
	}

	private List<Expr> parseMaybeArgs() {
		// maybeArgs ::= exp extraArg |ε
		// if we encounter a closing paren, then we have reached the end of the args
		List<Expr> ret = new LinkedList<Expr>();
		if (!accept(TokenClass.RPAR)) {
			ret.add(parseExp());
			ret.addAll(parseExtraArg());
		}
		return ret;
	}

	private List<Expr> parseExtraArg() {
		// extrArg ::= "," exp extraArg | ε
		List<Expr> ret = new LinkedList<Expr>();
		if (accept(TokenClass.COMMA)) {
			expect(TokenClass.COMMA);
			ret.add(parseExp());
			ret.addAll(parseExtraArg());
		}
		return ret;
	}

	private SizeOfExpr parseSizeOf() {
		// sizeof ::= "sizeof" "(" type ")" # size of type
		expect(TokenClass.SIZEOF);
		expect(TokenClass.LPAR);
		SizeOfExpr ret = new SizeOfExpr(parseType());
		expect(TokenClass.RPAR);
		return ret;
	}

	private Expr parseBracketOrTypeCast() {
		// bracketsOrtypeCast := "(" ( brackets | typeCast)
		expect(TokenClass.LPAR);
		if (accept(TokenClass.INT, TokenClass.CHAR, TokenClass.VOID, TokenClass.STRUCT)) {
			return parseTypeCast();
		} else {
			return parseBrackets();
		}
	}

	private Expr parseBrackets() {
		// brackets := exp ")"
		Expr ret = parseExp();
		expect(TokenClass.RPAR);
		return ret;
	}

	private TypecastExpr parseTypeCast() {
		// typecast ::= type ")" exp # type casting
		Type t = parseType();
		expect(TokenClass.RPAR);
		Expr e = parseExp();
		return new TypecastExpr(t, e);
	}

	private Expr parseArrayOrFieldAccessOrBinaryOps(Expr previous, boolean binOps) {
		// arrayOrFieldAccessOrBinaryOps :: = (arrayAccess | fieldAccess | binaryOps |
		// ε)
		if (accept(TokenClass.LSBR)) {
			return parseArrayAccess(previous);
		} else if (accept(TokenClass.DOT)) {
			return parseFieldAccess(previous);
		} else if (accept(TokenClass.GT, TokenClass.LT, TokenClass.GE, TokenClass.LE, TokenClass.NE, TokenClass.EQ,
				TokenClass.PLUS, TokenClass.MINUS, TokenClass.DIV, TokenClass.ASTERIX, TokenClass.REM, TokenClass.OR,
				TokenClass.AND) && binOps == true) {
			// ">" | "<" | ">=" | "<=" | "!=" | "==" | "+" | "-" | "/" | "*" | "%" | "||" |
			// "&&"
			return parseBinaryOps(previous);
		}
		return previous;
	}

	private ArrayAccessExpr parseArrayAccess(Expr array) {
		// arrayaccess ::= "[" exp "]" # array access
		expect(TokenClass.LSBR);
		Expr idx = parseExp();
		expect(TokenClass.RSBR);
		return new ArrayAccessExpr(array, idx);
	}

	private FieldAccessExpr parseFieldAccess(Expr struct) {
		// fieldaccess ::= "." IDENT # structure field member access
		expect(TokenClass.DOT);
		String id = token.data;
		expect(TokenClass.IDENTIFIER);
		return new FieldAccessExpr(struct, id);
	}

	private Expr parseBinaryOps(Expr left) {
		// binaryOps := ( "||" |ε ) binaryOps2
		boolean isAnOr = false;
		if (accept(TokenClass.OR)) {
			expect(TokenClass.OR);
			isAnOr = true;
		}

		Expr right = parseBinaryOps2(left);
		if (isAnOr) {
			return new BinOp(left, Op.OR, right);
		} else {
			return right;
		}
	}

	private Expr parseBinaryOps2(Expr left) {
		// binaryOps2 := ("&&" | ε ) binaryOps3
		boolean isAnAnd = false;
		if (accept(TokenClass.AND)) {
			expect(TokenClass.AND);
			isAnAnd = true;
		}
		Expr right = parseBinaryOps3(left);
		if (isAnAnd) {
			return new BinOp(left, Op.AND, right);
		} else {
			return right;
		}
	}

	private Expr parseBinaryOps3(Expr left) {
		// binaryOps3 := ( "!=" | "==" | ε) binaryOps4

		boolean hasSym = false;
		Op op = null;
		if (accept(TokenClass.NE)) {
			expect(TokenClass.NE);
			op = Op.NE;
			hasSym = true;
		} else if (accept(TokenClass.EQ)) {
			expect(TokenClass.EQ);
			op = Op.EQ;
			hasSym = true;
		}
		Expr right = parseBinaryOps4(left);

		if (hasSym) {
			return new BinOp(left, op, right);
		} else {
			return right;
		}

	}

	private Expr parseBinaryOps4(Expr left) {
		// binaryOps4 := (">" | "<" | ">=" | "<=" | ε) binaryOps5

		boolean hasSym = false;
		Op op = null;
		if (accept(TokenClass.GT)) {
			expect(TokenClass.GT);
			op = Op.GT;
			hasSym = true;
		} else if (accept(TokenClass.LT)) {
			expect(TokenClass.LT);
			op = Op.LT;
			hasSym = true;
		} else if (accept(TokenClass.GE)) {
			expect(TokenClass.GE);
			op = Op.GE;
			hasSym = true;
		} else if (accept(TokenClass.LE)) {
			expect(TokenClass.LE);
			op = Op.LE;
			hasSym = true;
		}

		Expr right = parseBinaryOps5(left);

		if (hasSym) {
			return new BinOp(left, op, right);
		} else {
			return right;
		}

	}

	private Expr parseBinaryOps5(Expr left) {
		// := ( "+" | "-" | ε ) binaryOps6()

		boolean hasSym = false;
		Op op = null;
		if (accept(TokenClass.PLUS)) {
			expect(TokenClass.PLUS);
			op = Op.ADD;
			hasSym = true;
		} else if (accept(TokenClass.MINUS)) {
			expect(TokenClass.MINUS);
			op = Op.SUB;
			hasSym = true;
		}

		Expr right = parseBinaryOps6(left);

		if (hasSym) {
			return new BinOp(left, op, right);
		} else {
			return right;
		}

	}

	private Expr parseBinaryOps6(Expr left) {
		// binaryOps6 := ( "/" | "*" | "%" | ε ) exp

		boolean hasSym = false;
		Op op = null;
		if (accept(TokenClass.DIV)) {
			expect(TokenClass.DIV);
			op = Op.DIV;
			hasSym = true;
		} else if (accept(TokenClass.ASTERIX)) {
			expect(TokenClass.ASTERIX);
			op = Op.MUL;
			hasSym = true;
		} else if (accept(TokenClass.REM)) {
			expect(TokenClass.REM);
			op = Op.MOD;
			hasSym = true;
		}

		if (accept(TokenClass.SC)) {
			return left;
		}
		if (hasSym) {
			// these operators bind most tightly, so we want to bind to the next token
			// rather than the next expression...
			if (!accept(TokenClass.LPAR)) {

				Expr right = parseExp(false);
				// get the next *expression that is not a binary operation*
				Expr us = new BinOp(left, op, right);
				return parseBinaryOps(us);

			} else {
				Expr right = parseExp();
				return new BinOp(left, op, right);
			}

		} else {

			if (accept(TokenClass.RPAR)) {
				return left;
			} else {

				Expr right = parseExp();
				return right;
			}
		}

	}

}
