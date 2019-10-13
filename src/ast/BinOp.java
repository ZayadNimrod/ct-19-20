package ast;

public class BinOp extends Expr {
	public final Expr left;
	public final Expr right;
	public Op op;

	public BinOp(Expr l, Op o, Expr r) {
		left = l;
		op = o;
		right = r;
	}

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitBinOp(this);
	}
}
