package ast;

public class IntLiteral extends Expr {

	public final int lit;

	public IntLiteral(int l) {
		lit = l;
	}

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitIntLiteral(this);
	}

}
