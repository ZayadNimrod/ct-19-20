package lexer;

import lexer.Token.TokenClass;

import java.io.EOFException;
import java.io.IOException;

/**
 * @author cdubach
 */
public class Tokeniser {

	private Scanner scanner;

	private int error = 0;

	public int getErrorCount() {
		return this.error;
	}

	public Tokeniser(Scanner scanner) {
		this.scanner = scanner;
	}

	private void error(char c, int line, int col) {
		System.out.println("Lexing error: unrecognised character (" + c + ") at " + line + ":" + col);
		error++;
	}

	public Token nextToken() {
		Token result;
		try {
			result = next();
		} catch (EOFException eof) {
			// end of file, nothing to worry about, just return EOF token
			return new Token(TokenClass.EOF, scanner.getLine(), scanner.getColumn());
		} catch (IOException ioe) {
			ioe.printStackTrace();
			// something went horribly wrong, abort
			System.exit(-1);
			return null;
		}
		return result;
	}

	/*
	 * To be completed
	 */
	private Token next() throws IOException {

		int line = scanner.getLine();
		int column = scanner.getColumn();

		// get the next character
		char c = scanner.next();

		// skip white spaces
		if (Character.isWhitespace(c)) {
			return next();
		}

		if (c == '#') {
			for (int i = 0; i < 7; i++) {
				if (scanner.peek() == "include".charAt(i)) {
					scanner.next();
				} else {
					error(c, line, column);
					return new Token(TokenClass.INVALID, line, column);
				}
			}
			while (scanner.peek() != '\n') {
				scanner.next();
			}
			return new Token(TokenClass.INCLUDE, line, column);
		}

		// recognise operators
		// recognises the plus operator
		if (c == '+') {
			return new Token(TokenClass.PLUS, line, column);
		}

		// recognise minus
		if (c == '-') {
			return new Token(TokenClass.MINUS, line, column);
		}

		// recognise star
		if (c == '*') {
			return new Token(TokenClass.ASTERIX, line, column);
		}

		// recognise divide
		if (c == '/') {
			if (scanner.peek() == '/') {// this is a simple-line comment, skip to the next line
				while (scanner.peek() != '\n') {
					scanner.next();
				}
				return next();

			} else if (scanner.peek() == '*') {
				// multiline comment, skip until ending
				scanner.next();
				while (true) {
					scanner.next();
					if (scanner.peek() == '*') {
						scanner.next();
						if (scanner.peek() == '/') {
							scanner.next();
							return next();
						}
					}

				}

			} else {

				return new Token(TokenClass.DIV, line, column);
			}
		}

		// recognise modulo
		if (c == '%') {
			return new Token(TokenClass.REM, line, column);
		}

		// comparisions
		// recognise greater than
		// recognise greater or equal
		if (c == '>') {
			if (scanner.peek() == '=') {
				scanner.next();
				return new Token(TokenClass.GE, line, column);
			} else {
				return new Token(TokenClass.GT, line, column);
			}
		}

		// recognise smaller
		// recognise smaller or equal
		if (c == '<') {
			if (scanner.peek() == '=') {
				scanner.next();
				return new Token(TokenClass.LE, line, column);
			} else {
				return new Token(TokenClass.LT, line, column);
			}
		}

		// recognise equality operator
		if (c == '=') {
			if (scanner.peek() == '=') {
				scanner.next();
				return new Token(TokenClass.EQ, line, column);
			} else {
				return new Token(TokenClass.ASSIGN, line, column);
			}
		}

		// recognise inequality operator
		if (c == '!') {
			if (scanner.peek() == '=') {
				scanner.next();
				return new Token(TokenClass.NE, line, column);
			}
		}

		// logical operators
		// recognise NOT

		// recognise AND
		if (c == '&') {
			if (scanner.peek() == '&') {
				scanner.next();
				return new Token(TokenClass.AND, line, column);
			}
		}
		// recognise OR
		if (c == '|') {
			if (scanner.peek() == '|') {
				scanner.next();
				return new Token(TokenClass.OR, line, column);
			}
		}

		// recognise delimiters
		if (c == '{') {
			return new Token(TokenClass.LBRA, line, column);
		}
		if (c == '}') {
			return new Token(TokenClass.RBRA, line, column);
		}
		if (c == '(') {
			return new Token(TokenClass.LPAR, line, column);
		}
		if (c == ')') {
			return new Token(TokenClass.RPAR, line, column);
		}
		if (c == '[') {
			return new Token(TokenClass.LSBR, line, column);
		}
		if (c == ']') {
			return new Token(TokenClass.RSBR, line, column);
		}
		if (c == ',') {
			return new Token(TokenClass.COMMA, line, column);
		}
		if (c == ';') {
			return new Token(TokenClass.SC, line, column);
		}
		if (c == '.') {
			return new Token(TokenClass.DOT, line, column);
		}

		if (c == '"') {
			// string literal
			String lit = "";
			while (scanner.peek() != '"') {
				if (scanner.peek() == '\\') {
					// this is an escape character
					scanner.next();
					char e = scanner.peek();
					if (e == 'n') {
						lit += '\n';
					}
					if (e == 't') {
						lit += '\t';
					}
					if (e == '\\') {
						lit += '\\';
					}
					if (e == '"') {
						lit += '"';
					}
					if (e == 'b') {
						lit += '\b';
					}
					if (e == 'r') {
						lit += '\r';
					}
					if (e == 'f') {
						lit += '\f';
					}
					if (e == '0') {
						lit += '\0';
					}
				} else {

					lit += scanner.peek();
				}
				scanner.next();
			}
			scanner.next();
			return new Token(TokenClass.STRING_LITERAL, lit, line, column);

		}

		if (Character.isDigit(c)) {
			// int literal
			String lit = "";
			while (Character.isDigit(scanner.peek())) {
				lit += scanner.peek();
				scanner.next();
			}
			return new Token(TokenClass.INT_LITERAL, lit, line, column);
		}

		if (c == '\'') {
			// char literal
			char a = scanner.next();
			if (a == '\'') {
				return new Token(TokenClass.CHAR_LITERAL, "", line, column);
			} else {

				if (scanner.peek() == '\'') {
					scanner.next();
					return new Token(TokenClass.CHAR_LITERAL, Character.toString(a), line, column);
				} else if (a == '\\') {
					char e = scanner.next();
					scanner.next();
					if (e == 'n') {
						return new Token(TokenClass.CHAR_LITERAL, "\n", line, column);
					}
					if (e == 't') {
						return new Token(TokenClass.CHAR_LITERAL, "\t", line, column);
					}
					if (e == '\\') {
						return new Token(TokenClass.CHAR_LITERAL, "\\", line, column);
					}
					if (e == '\'') {
						return new Token(TokenClass.CHAR_LITERAL, "'", line, column);
					}
					if (e == '\b') {
						return new Token(TokenClass.CHAR_LITERAL, "\b", line, column);
					}
					if (e == 'r') {
						return new Token(TokenClass.CHAR_LITERAL, "\r", line, column);
					}
					if (e == 'f') {
						return new Token(TokenClass.CHAR_LITERAL, "\f", line, column);
					}
					if (e == '\0') {
						return new Token(TokenClass.CHAR_LITERAL, "\0", line, column);
					}

				}
			}

		}

		if (Character.isAlphabetic(c) || c == '_') {
			// assuming we are down here and have not returned a keyword, then this is an
			// identifier
			String id = Character.toString(c);
			while ((Character.isLetterOrDigit(scanner.peek()) || scanner.peek() == '_')) {
				id += scanner.peek();
				scanner.next();
			}
			switch (id) {
			case ("int"):
				return new Token(TokenClass.INT, line, column);
			case ("void"):
				return new Token(TokenClass.VOID, line, column);
			case ("char"):
				return new Token(TokenClass.CHAR, line, column);
			case ("if"):
				return new Token(TokenClass.IF, line, column);

			case ("else"):
				return new Token(TokenClass.ELSE, line, column);
			case ("while"):
				return new Token(TokenClass.WHILE, line, column);
			case ("return"):
				return new Token(TokenClass.RETURN, line, column);

			case ("struct"):
				return new Token(TokenClass.STRUCT, line, column);
			case ("sizeof"):
				return new Token(TokenClass.SIZEOF, line, column);
			}

			return new Token(TokenClass.IDENTIFIER, id, line, column);

		}

		// if we reach this point, it means we did not recognise a valid token
		error(c, line, column);
		return new Token(TokenClass.INVALID, line, column);
	}

}
