package ast;

public class SizeOfExpr extends Expr {
	public final Expr struct;

	public SizeOfExpr(Expr s) {
		struct = s;
	}

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitSizeOfExpr(this);
	}
}
