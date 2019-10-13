package ast;

public class ExprStmt extends Stmt {
	public final Expr expr;
	
	public ExprStmt(Expr e) {
		expr = e;
	}
	
	   public <T> T accept(ASTVisitor<T> v) {
		    return v.visitStmt(this);
	    }
}
