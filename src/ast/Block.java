package ast;

import java.util.*;

public class Block extends Stmt {

	public final List<VarDecl> expr;
	public final List<Stmt> code;

	public Block(List<VarDecl> e, List<Stmt> c) {
		expr = e;
		code = c;
	}

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitBlock(this);
	}
}
