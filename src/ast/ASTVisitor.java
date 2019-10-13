package ast;

public interface ASTVisitor<T> {
	public T visitProgram(Program p);

	public T visitBaseType(BaseType bt);

	public T visitPointerType(PointerType p);

	public T visitStructType(StructType s);

	public T visitArrayType(ArrayType a);

	public T visitStructTypeDecl(StructTypeDecl st);

	public T visitVarDecl(VarDecl vd);

	public T visitFunDecl(FunDecl p);

	public T visitIntLiteral(IntLiteral il);

	public T visitStrLiteral(StrLiteral sl);

	public T visitChrLiteral(ChrLiteral cl);

	public T visitVarExpr(VarExpr v);

	public T visitFunCallExpr(FunCallExpr fc);

	public T visitBinOp(BinOp bo);

	public T visitArrayAccessExpr(ArrayAccessExpr ae);

	public T visitFieldAccessExpr(FieldAccessExpr fa);

	public T visitSizeOfExpr(SizeOfExpr so);

	public T visitTypecastExpr(TypecastExpr tc);

	public T visitExprStmt(ExprStmt e);

	public T visitWhile(While w);

	public T visitIf(If i);

	public T visitAssign(Assign a);

	public T visitReturn(Return r);

	public T visitBlock(Block b);

	// to complete ... (should have one visit method for each concrete AST node
	// class)
}
