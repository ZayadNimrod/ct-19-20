package ast;

public class StrLiteral extends Expr {

	public final String lit;

	public String id;
	
	public StrLiteral(String l) {
		lit = l;
	}

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitStrLiteral(this);
	}

}
