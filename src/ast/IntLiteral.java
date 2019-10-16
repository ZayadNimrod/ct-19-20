package ast;

public class IntLiteral extends Expr {

	public final int lit;

	public IntLiteral(int l) {
		// TODO: should this assignment be made in the type analysis visitor stage?
		type = BaseType.INT;
		lit = l;
	}

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitIntLiteral(this);
	}

}
