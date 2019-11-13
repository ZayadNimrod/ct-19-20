package gen;

import java.io.PrintWriter;

import ast.*;

//This class uses .asciiz in the .data segment to create space for all the string literals
public class StringLiteralVisitor implements ASTVisitor<Void> {
	PrintWriter writer;
	int id = 0;

	private int uid() {
		return ++id;
	}

	public void writeStrings(Program program, PrintWriter writer) {
		this.writer = writer;
		visitProgram(program);
	}

	@Override
	public Void visitProgram(Program p) {
		for (StructTypeDecl s : p.structTypeDecls) {
			s.accept(this);
		}
		for (VarDecl v : p.varDecls) {
			v.accept(this);
		}
		for (FunDecl f : p.funDecls) {
			f.accept(this);
		}

		return null;

	}

	@Override
	public Void visitBaseType(BaseType bt) {
		return null;
	}

	@Override
	public Void visitPointerType(PointerType p) {
		return null;
	}

	@Override
	public Void visitStructType(StructType s) {
		return null;
	}

	@Override
	public Void visitArrayType(ArrayType a) {
		// TODO: Do we actually need to visit Types? probably not, but it's also not a
		// particularly important thing to optimise during running, I guess
		a.type.accept(this);
		return null;
	}

	@Override
	public Void visitStructTypeDecl(StructTypeDecl st) {
		st.structDecl.accept(this);
		for (VarDecl v : st.variables) {
			v.accept(this);
		}
		return null;
	}

	@Override
	public Void visitVarDecl(VarDecl vd) {
		// I guess we'll just do this to *really* make sure, just in case we run into a
		// string when literal parsing types

		// hell, if your program managed to pass all the other checks with strings in
		// your types, you probably know better than me what should be done
		vd.type.accept(this);
		return null;
	}

	@Override
	public Void visitFunDecl(FunDecl p) {
		p.type.accept(this);
		for (VarDecl v : p.params) {
			v.accept(this);
		}

		p.block.accept(this);
		return null;
	}

	@Override
	public Void visitIntLiteral(IntLiteral il) {
		return null;
	}

	@Override
	public Void visitStrLiteral(StrLiteral sl) {
		// Literally the only point of this visitor
		String sid = "string_" + uid();
		sl.id = sid;
		writer.write(sid + ": .asciiz \"");
		for (char i : sl.lit.toCharArray()) {
			switch (i) {
			case ('\n'):
				writer.write("\\n");
				break;
			case ('\t'):
				writer.write("\\t");
				break;
			case ('\\'):
				writer.write("\\");
				break;
			case ('\"'):
				writer.write("\\\"");
				break;
			case ('\b'):
				writer.write("\\b");
				break;
			case ('\r'):
				writer.write("\\r");
				break;
			case ('\f'):
				writer.write("\\f");
				break;
			case ('\0'):
				writer.write("\\0");

				break;
			default:
				writer.write(i);
				break;
			}
		}

		writer.write("\"\n");
		return null;
	}

	@Override
	public Void visitChrLiteral(ChrLiteral cl) {
		// TODO what if this is an escape character

		return null;
	}

	@Override
	public Void visitVarExpr(VarExpr v) {
		v.type.accept(this);
		v.vd.accept(this);
		return null;
	}

	@Override
	public Void visitFunCallExpr(FunCallExpr fc) {
		fc.type.accept(this);
		// Don't need to do that, or we'll allocate each string in a function every time
		// it's called.
		// fc.fd.accept(this);
		for (Expr v : fc.args) {
			v.accept(this);
		}
		return null;
	}

	@Override
	public Void visitBinOp(BinOp bo) {
		bo.left.accept(this);
		bo.right.accept(this);
		return null;
	}

	@Override
	public Void visitArrayAccessExpr(ArrayAccessExpr ae) {
		ae.array.accept(this);
		ae.index.accept(this);
		ae.type.accept(this);
		return null;
	}

	@Override
	public Void visitFieldAccessExpr(FieldAccessExpr fa) {
		fa.struct.accept(this);
		fa.type.accept(this);
		return null;
	}

	@Override
	public Void visitSizeOfExpr(SizeOfExpr so) {
		so.baseType.accept(this);
		so.type.accept(this);
		return null;
	}

	@Override
	public Void visitTypecastExpr(TypecastExpr tc) {
		tc.to.accept(this);
		tc.toConvert.accept(this);
		tc.type.accept(this);
		return null;
	}

	@Override
	public Void visitExprStmt(ExprStmt e) {
		e.expr.accept(this);
		return null;
	}

	@Override
	public Void visitWhile(While w) {
		w.expr.accept(this);
		w.code.accept(this);
		return null;
	}

	@Override
	public Void visitIf(If i) {
		i.expr.accept(this);
		i.code.accept(this);
		if (i.elseCode != null) {
			i.elseCode.accept(this);
		}
		return null;
	}

	@Override
	public Void visitAssign(Assign a) {
		a.left.accept(this);
		a.right.accept(this);
		return null;
	}

	@Override
	public Void visitReturn(Return r) {
		if (r.expr != null) {
			r.expr.accept(this);
		}
		return null;
	}

	@Override
	public Void visitBlock(Block b) {
		for (VarDecl v : b.vars) {
			v.accept(this);
		}
		for (Stmt s : b.code) {
			s.accept(this);
		}
		return null;
	}

	@Override
	public Void visitValueAtExpr(ValueAtExpr va) {
		va.type.accept(this);
		va.expr.accept(this);
		return null;
	}

}
