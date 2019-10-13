package ast;

public class SizeOfExpr extends Expr {
	public final Expr expr;

	public SizeOfExpr(Expr e) {
		expr = e;
	}

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitSizeOfExpr(this);
	}
}
