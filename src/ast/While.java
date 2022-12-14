package ast;

public class While extends Stmt {
	public final Expr expr;
	public final Stmt code;

	public While(Expr e, Stmt c) {
		expr = e;
		code = c;
	}

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitWhile(this);
	}
}
