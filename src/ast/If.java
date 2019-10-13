package ast;

public class If extends Stmt {
	public final Expr expr;
	public final Stmt code;
	public final Stmt elseCode;

	//TODO: checks for null?
	public If(Expr e,Stmt c, Stmt el) {
		expr = e;
		code = c;
		elseCode = el;
	}

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitIf(this);
	}
}
