package ast;

public class ArrayAccessExpr extends Expr {
	public final Expr array;
	public final Expr index;

	public ArrayAccessExpr(Expr a, Expr i) {
		array = a;
		index = i;
	}

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitArrayAccessExpr(this);
	}
}
