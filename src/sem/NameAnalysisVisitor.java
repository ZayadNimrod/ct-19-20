package sem;

import ast.*;
import java.util.*;

public class NameAnalysisVisitor extends BaseSemanticVisitor<Void> {

	
	//TODO: no local scoping whatsoever
	//TODO: type analysis on all Exprs not done
	
	private Stack<Scope> scopeStack;

	private Void putSymbol(Symbol s) {
		scopeStack.peek().put(s, this);
		return null;
	}

	@Override
	public Void visitProgram(Program p) {
		scopeStack = new Stack<Scope>();
		scopeStack.add(new Scope());
		for (StructTypeDecl std : p.structTypeDecls) {
			std.accept(this);
		}
		for (VarDecl vd : p.varDecls) {
			vd.accept(this);
		}
		for (FunDecl fd : p.funDecls) {
			fd.accept(this);
		}
		return null;
	}

	@Override
	public Void visitBaseType(BaseType bt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitPointerType(PointerType p) {
		p.pointerToType.accept(this);
		return null;
	}

	@Override
	public Void visitStructType(StructType s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitArrayType(ArrayType a) {
		a.type.accept(this);
		return null;
	}

	@Override
	public Void visitStructTypeDecl(StructTypeDecl st) {
		st.structDecl.accept(this);
		for (VarDecl vd : st.variables) {
			vd.accept(this);
		}
		return null;
	}

	@Override
	public Void visitVarDecl(VarDecl vd) {
		putSymbol(new VarSymbol(vd));
		vd.type.accept(this);
		return null;
	}

	@Override
	public Void visitFunDecl(FunDecl p) {
		putSymbol(new FunSymbol(p));
		p.type.accept(this);
		//TODO: new scope for parameters and internal block?
		for (VarDecl vd: p.params) {
			vd.accept(this);
		}		
		p.block.accept(this);
		return null;
	}

	@Override
	public Void visitIntLiteral(IntLiteral il) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitStrLiteral(StrLiteral sl) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitChrLiteral(ChrLiteral cl) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitVarExpr(VarExpr v) {
		Symbol s = scopeStack.peek().lookup(v.name);
		VarSymbol vs = (VarSymbol) s;
		if (vs != null) {
			v.vd = vs.variable;
		}
		return null;
	}

	@Override
	public Void visitFunCallExpr(FunCallExpr fc) {
		Symbol s = scopeStack.peek().lookup(fc.name);
		FunSymbol fs = (FunSymbol) s;
		if (fs != null) {
			fc.vd = fs.function;
		}
		for (Expr e : fc.args) {
			e.accept(this);
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
		return null;
	}

	@Override
	public Void visitFieldAccessExpr(FieldAccessExpr fa) {
		fa.struct.accept(this);
		return null;
	}

	@Override
	public Void visitSizeOfExpr(SizeOfExpr so) {
		so.type.accept(this);
		return null;
	}

	@Override
	public Void visitTypecastExpr(TypecastExpr tc) {
		tc.to.accept(this);
		tc.toConvert.accept(this);
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
		i.elseCode.accept(this);
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
		r.expr.accept(this);
		return null;
	}

	@Override
	public Void visitBlock(Block b) {
		for (VarDecl vd : b.vars) {
			vd.accept(this);
		}for (Stmt s : b.code) {
			s.accept(this);
		}
		return null;
	}

	@Override
	public Void visitValueAtExpr(ValueAtExpr va) {
		va.expr.accept(this);
		return null;
	}

}
