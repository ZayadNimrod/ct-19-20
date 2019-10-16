package ast;

public class ChrLiteral extends Expr {

	public final char lit;

	public ChrLiteral(char l) {
		//TODO: should this assignment be made in the type analysis visitor stage?
		type = BaseType.CHAR;
		lit = l;
	}

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitChrLiteral(this);
	}

}
