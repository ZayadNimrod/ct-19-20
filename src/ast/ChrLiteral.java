package ast;

public class ChrLiteral extends Expr {

	public final char lit;

	public ChrLiteral(char l) {
		lit = l;
	}

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitChrLiteral(this);
	}

}
