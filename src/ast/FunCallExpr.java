package ast;

import java.util.*;

public class FunCallExpr extends Expr {
	public final String name;
	public FunDecl vd; // to be filled in by the name analyser
	public List<Expr> args;

	public FunCallExpr(String name, List<Expr> a) {
		this.name = name;
		args = a;
	}

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitFunCallExpr(this);
	}
}
