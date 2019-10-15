package ast;

import java.io.PrintWriter;

public class ASTPrinter implements ASTVisitor<Void> {

	private PrintWriter writer;

	public ASTPrinter(PrintWriter writer) {
		this.writer = writer;
	}

	@Override
	public Void visitBlock(Block b) {
		writer.print("Block(");
		// what about first argument?
		boolean firstArg = true;
		for (VarDecl vd : b.vars) {
			if (!firstArg) {
				writer.print(",");
			} else {
				firstArg = false;
			}
			vd.accept(this);
		}
		for (Stmt s : b.code) {
			if (!firstArg) {
				writer.print(",");
			} else {
				firstArg = false;
			}
			s.accept(this);
		}
		writer.print(")");
		return null;
	}

	@Override
	public Void visitFunDecl(FunDecl fd) {
		writer.print("FunDecl(");
		fd.type.accept(this);
		writer.print("," + fd.name + ",");
		for (VarDecl vd : fd.params) {
			vd.accept(this);
			writer.print(",");
		}
		fd.block.accept(this);
		writer.print(")");
		return null;
	}

	@Override
	public Void visitProgram(Program p) {
		writer.print("Program(");
		String delimiter = "";
		for (StructTypeDecl std : p.structTypeDecls) {
			writer.print(delimiter);
			delimiter = ",";
			std.accept(this);
		}
		for (VarDecl vd : p.varDecls) {
			writer.print(delimiter);
			delimiter = ",";
			vd.accept(this);
		}
		for (FunDecl fd : p.funDecls) {
			writer.print(delimiter);
			delimiter = ",";
			fd.accept(this);
		}
		writer.print(")");
		writer.flush();
		return null;
	}

	@Override
	public Void visitVarDecl(VarDecl vd) {
		writer.print("VarDecl(");
		vd.type.accept(this);
		writer.print("," + vd.varName);
		writer.print(")");
		return null;
	}

	@Override
	public Void visitVarExpr(VarExpr v) {
		writer.print("VarExpr(");
		writer.print(v.name);
		writer.print(")");
		return null;
	}

	@Override
	public Void visitBaseType(BaseType bt) {
		//writer.print("BaseType(");
		writer.print(bt);
		//writer.print(")");
		return null;
	}

	@Override
	public Void visitStructTypeDecl(StructTypeDecl st) {
		writer.print("StructTypeDecl(");
		st.structDecl.accept(this);
		for (VarDecl vd : st.variables) {
			writer.print(",");
			vd.accept(this);
		}
		writer.print(")");
		return null;
	}

	@Override
	public Void visitPointerType(PointerType p) {
		writer.print("PointerType(");
		p.pointerToType.accept(this);
		writer.print(")");
		return null;
	}

	@Override
	public Void visitStructType(StructType s) {
		writer.print("StructType(");
		writer.print(s.structType);
		writer.print(")");
		return null;
	}

	@Override
	public Void visitArrayType(ArrayType a) {
		writer.print("ArrayType(");
		a.type.accept(this);
		writer.print(",");
		writer.print(a.length);
		writer.print(")");
		return null;
	}

	@Override
	public Void visitIntLiteral(IntLiteral il) {
		writer.print("IntLiteral(");
		writer.print(il.lit);
		writer.print(")");
		return null;
	}

	@Override
	public Void visitStrLiteral(StrLiteral sl) {
		writer.print("StrLiteral(");
		writer.print(sl.lit);
		writer.print(")");
		return null;
	}

	@Override
	public Void visitChrLiteral(ChrLiteral cl) {
		writer.print("ChrLiteral(");
		writer.print(cl.lit);
		writer.print(")");
		return null;
	}

	@Override
	public Void visitFunCallExpr(FunCallExpr fc) {
		writer.print("FunCallLiteral(");
		writer.print(fc.name);
		for (Expr vd : fc.args) {
			writer.print(",");
			vd.accept(this);
		}
		writer.print(")");
		return null;
	}

	@Override
	public Void visitBinOp(BinOp op) {
		writer.print("BinOp(");
		op.left.accept(this);
		writer.print(",");
		writer.print(op.op);
		writer.print(",");
		op.right.accept(this);
		writer.print(")");
		return null;
	}

	@Override
	public Void visitArrayAccessExpr(ArrayAccessExpr ae) {
		writer.print("ArrayAccessExpr(");
		ae.array.accept(this);
		writer.print(",");
		ae.index.accept(this);
		writer.print(")");
		return null;
	}

	@Override
	public Void visitFieldAccessExpr(FieldAccessExpr fa) {
		writer.print("FieldAccessExpr(");
		fa.struct.accept(this);
		writer.print(",");
		writer.print(fa.field);
		writer.print(")");
		return null;
	}

	@Override
	public Void visitSizeOfExpr(SizeOfExpr so) {
		writer.print("SizeOfExpr(");
		so.type.accept(this);
		writer.print(")");
		return null;
	}

	@Override
	public Void visitTypecastExpr(TypecastExpr tc) {
		writer.print("TypecastExpr(");
		tc.to.accept(this);
		writer.print(",");
		tc.toConvert.accept(this);
		writer.print(")");
		return null;
	}

	@Override
	public Void visitExprStmt(ExprStmt e) {
		writer.print("ExprStmt(");
		e.expr.accept(this);
		writer.print(")");
		return null;
	}

	@Override
	public Void visitWhile(While w) {
		writer.print("While(");
		w.expr.accept(this);
		writer.print(",");
		w.code.accept(this);
		writer.print(")");
		return null;
	}

	@Override
	public Void visitIf(If i) {
		writer.print("If(");
		i.expr.accept(this);
		writer.print(",");
		i.code.accept(this);
		if (i.elseCode != null) {
			writer.print(",");
			i.elseCode.accept(this);
		}
		writer.print(")");
		return null;
	}

	@Override
	public Void visitAssign(Assign a) {
		writer.print("Assign(");
		a.left.accept(this);
		writer.print(",");
		a.right.accept(this);
		writer.print(")");
		return null;
	}

	@Override
	public Void visitReturn(Return r) {
		writer.print("Return(");
		if (r.expr != null) {
			writer.print(",");
			r.expr.accept(this);
		}
		writer.print(")");
		return null;
	}

	@Override
	public Void visitValueAtExpr(ValueAtExpr va) {
		writer.print("ValueAtExpr(");
		va.expr.accept(this);
		writer.print(")");
		return null;
	}

}
