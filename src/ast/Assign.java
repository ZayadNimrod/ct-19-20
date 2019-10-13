package ast;

public class Assign extends Stmt {
	public final Expr left;
	public final Expr right;

	public Assign(Expr l, Expr r) {
		left = l;
		right = r;
	}

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitAssign(this);
	}
}
