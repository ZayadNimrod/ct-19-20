package ast;

public class SizeOfExpr extends Expr {
	public Type type;

	public SizeOfExpr(Type t) {
		type = t;
	}

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitSizeOfExpr(this);
	}
}
