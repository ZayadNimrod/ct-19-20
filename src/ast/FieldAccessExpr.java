package ast;

public class FieldAccessExpr extends Expr {
	public final Expr struct;
	public final String field;

	public FieldAccessExpr(Expr s, String f) {
		struct = s;
		field = f;
	}

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitFieldAccessExpr(this);
	}
}
