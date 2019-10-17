package sem;

import ast.*;
import java.util.*;

public class NameAnalysisVisitor extends BaseSemanticVisitor<Void> {

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
		// paramaters exist in the functions scope
		scopeStack.add(new Scope());
		for (VarDecl vd : p.params) {
			vd.accept(this);
		}
		p.block.accept(this);
		scopeStack.pop();
		// TODO: what we have right now allows paramter shadwoing, which is probably not
		// ideal
		return null;
	}

	@Override
	public Void visitIntLiteral(IntLiteral il) {
		return null;
	}

	@Override
	public Void visitStrLiteral(StrLiteral sl) {
		return null;
	}

	@Override
	public Void visitChrLiteral(ChrLiteral cl) {
		return null;
	}

	@Override
	public Void visitVarExpr(VarExpr v) {
//		Symbol s = scopeStack.peek().lookup(v.name);
//		VarSymbol vs = (VarSymbol) s;
//		if (vs != null) {
//			v.vd = vs.variable;
//			v.type = v.vd.type;
//		}else {
//			error("Using non-variable identifier as a variable (possibly a function?)");
//		}

		Symbol s = scopeStack.peek().lookup(v.name);
		// if (s != new VarSymbol(v.vd)) {
		if (s != null) {
			error("Variable " + v.name + " has not been declared");
		}
		// v.vd.accept(this);
		return null;
	}

	@Override
	public Void visitFunCallExpr(FunCallExpr fc) {
		Symbol s = scopeStack.peek().lookup(fc.name);
		FunSymbol fs = (FunSymbol) s;
//		if (fs != null) {
//			fc.vd = fs.function;
//		}else {
//			error("Using non-function identifier as a function (possibly a variable?)");
//		}
		
		// if (s != new FunSymbol()) {
		if (s != null) {
			error("Function " + fc.name + " has not been declared");
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
		// TODO: check if array exist? but anything can be array-accssed...
		ae.array.accept(this);
		ae.index.accept(this);
		return null;
	}

	@Override
	public Void visitFieldAccessExpr(FieldAccessExpr fa) {
		// TODO: check struct exists. same problem as above...
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
		scopeStack.add(new Scope());
		for (VarDecl vd : b.vars) {
			vd.accept(this);
		}
		for (Stmt s : b.code) {
			s.accept(this);
		}
		scopeStack.pop();
		return null;
	}

	@Override
	public Void visitValueAtExpr(ValueAtExpr va) {
		va.expr.accept(this);
		return null;
	}

}
