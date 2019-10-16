package ast;

public class StrLiteral extends Expr {

	public final String lit;

	public StrLiteral(String l) {
		lit = l;
	}

	public <T> T accept(ASTVisitor<T> v) {
		//TODO: should this assignment be made in the type analysis visitor stage?
		type = new PointerType(BaseType.CHAR);
		return v.visitStrLiteral(this);
	}

}
