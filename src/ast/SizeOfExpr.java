package ast;

public class SizeOfExpr extends Expr {
	public Type baseType;
	public SizeOfExpr(Type t) {
		baseType = t;
	}

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitSizeOfExpr(this);
	}
}
