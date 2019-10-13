package ast;

public class TypecastExpr extends Expr {
	
	public final Type to;
	public final Expr toConvert;
	

	public TypecastExpr(Type t, Expr e) {
		to=t;
		toConvert  =e;
	}

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitTypecastExpr(this);
	}
}
