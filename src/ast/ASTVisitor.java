package ast;

public interface ASTVisitor<T> {
    public T visitBaseType(BaseType bt);
    public T visitStructTypeDecl(StructTypeDecl st);
    public T visitBlock(Block b);
    public T visitFunDecl(FunDecl p);
    public T visitProgram(Program p);
    public T visitVarDecl(VarDecl vd);
    public T visitVarExpr(VarExpr v);
	public T visitIf(If i);
	public T visitReturn(Return r);
	public T visitAssign(Assign a);
	public T visitFieldAccessExpr(FieldAccessExpr fa);
	public T visitArrayAccessExpr(ArrayAccessExpr ae);
	public T visitBinOp(BinOp bo);
	public T visitFunCallExpr(FunCallExpr fc);
	public T visitPointerType(PointerType p);
	public T visitStmt(ExprStmt e);
	public T visitSizeOfExpr(SizeOfExpr so);
	public T visitStrLiteral(StrLiteral sl);
	public T visitIntLiteral(IntLiteral il);
	public T visitChrLiteral(ChrLiteral cl);
	public T visitArrayType(ArrayType a);
	public T visitStructType(StructType s);
	public T visitWhile(While w);
	public T visitTypecastExpr(TypecastExpr tc);
	

    // to complete ... (should have one visit method for each concrete AST node class)
}
