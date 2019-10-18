package sem;

import java.util.Stack;

import ast.*;

public class TypeCheckVisitor extends BaseSemanticVisitor<Type> {

	private Stack<Scope> scopeStack = new Stack<Scope>();

	private Void putSymbol(Symbol s) {
		scopeStack.peek().put(s, this);
		return null;
	}

	@Override
	public Type visitBaseType(BaseType bt) {
		return bt;
	}

	@Override
	public Type visitStructTypeDecl(StructTypeDecl st) {
		return st.structDecl.accept(this);
	}

	@Override
	public Type visitBlock(Block b) {
		scopeStack.add(new Scope());
		for (VarDecl v : b.vars) {
			v.accept(this);
		}
		for (Stmt s : b.code) {
			s.accept(this);
		}
		scopeStack.pop();
		return null;
	}

	@Override
	public Type visitFunDecl(FunDecl p) {
		// TODO: not sure how scoping will interact with params here;
		// we make a new scope, so params can be shadowed?
		putSymbol(new FunSymbol(p));
		for (VarDecl v : p.params) {
			v.accept(this);
		}
		p.block.accept(this);
		return p.type;
	}

	@Override
	public Type visitProgram(Program p) {
		scopeStack.add(new Scope());
		for (VarDecl v : p.varDecls) {
			v.accept(this);
		}
		for (StructTypeDecl v : p.structTypeDecls) {
			v.accept(this);
		}
		for (FunDecl v : p.funDecls) {
			v.accept(this);
		}
		return null;
	}

	@Override
	public Type visitVarDecl(VarDecl vd) {
		vd.type.accept(this);
		putSymbol(new VarSymbol(vd));
		return vd.type;
	}

	@Override
	public Type visitVarExpr(VarExpr v) {
		v.type = v.vd.accept(this);
		return v.type;
	}

	@Override
	public Type visitPointerType(PointerType p) {
		return p;
	}

	@Override
	public Type visitStructType(StructType s) {
		return s;
	}

	@Override
	public Type visitArrayType(ArrayType a) {
		return a;
	}

	@Override
	public Type visitIntLiteral(IntLiteral il) {
		return BaseType.INT;
	}

	@Override
	public Type visitStrLiteral(StrLiteral sl) {
		return new PointerType(BaseType.CHAR);
	}

	@Override
	public Type visitChrLiteral(ChrLiteral cl) {
		return BaseType.CHAR;
	}

	@Override
	public Type visitFunCallExpr(FunCallExpr fc) {
		fc.type = fc.fd.accept(this);
		return fc.type;
	}

	@Override
	public Type visitBinOp(BinOp bo) {
		Type left = bo.type.accept(this);
		Type right = bo.type.accept(this);

		if (left != right) {
			error("operands of binary operation must match,(" + left.toString() + "," + right.toString() + ")");
			return null;
		}

		switch (bo.op) {
		case ADD:
			return left;
		case AND://TODO: check for boolean types
			return BaseType.INT; //TODO: not sure, is this our equivalent for a boolean?
		case DIV:
			if(left!=BaseType.INT) {
				error("Division can only be done on operands of type Int, not "+left.toString());
			}
			return BaseType.INT; 
		case EQ:
			return BaseType.INT; //TODO: not sure, is this our equivalent for a boolean?
		case GE:
			return BaseType.INT; //TODO: not sure, is this our equivalent for a boolean?
		case GT:
			return BaseType.INT; //TODO: not sure, is this our equivalent for a boolean?
		case LE:
			return BaseType.INT; //TODO: not sure, is this our equivalent for a boolean?
		case LT:
			return BaseType.INT; //TODO: not sure, is this our equivalent for a boolean?
		case MOD:
			if(left!=BaseType.INT) {
				error("Remainder division can only be done on operands of type Int, not "+left.toString());
			}
			return BaseType.INT; 
		case MUL:
			if(left!=BaseType.INT) {
				error("Multiplication can only be done on operands of type Int, not "+left.toString());
			}
			return BaseType.INT; 
		case NE:
			return BaseType.INT; //TODO: not sure, is this our equivalent for a boolean?
		case OR:
			return BaseType.INT; //TODO: not sure, is this our equivalent for a boolean?
		case SUB:
			if(left!=BaseType.INT) {
				error("Subtraction can only be done on operands of type Int, not "+left.toString());
			}
			return BaseType.INT; 
		default:
			return null;
		}
	}

	@Override
	public Type visitArrayAccessExpr(ArrayAccessExpr ae) {
		ae.type = new PointerType(ae.array.accept(this));
		return ae.type;
	}

	@Override
	public Type visitFieldAccessExpr(FieldAccessExpr fa) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visitSizeOfExpr(SizeOfExpr so) {
		so.type = BaseType.INT;
		return so.type;
	}

	@Override
	public Type visitTypecastExpr(TypecastExpr tc) {
		tc.toConvert.accept(this);
		tc.type = tc.to;
		return tc.type;
	}

	@Override
	public Type visitExprStmt(ExprStmt e) {
		return e.accept(this);
	}

	@Override
	public Type visitWhile(While w) {
		w.expr.accept(this);
		// TODO: check that the expression is a truth value?
		w.code.accept(this);
		return null;
	}

	@Override
	public Type visitIf(If i) {
		i.expr.accept(this);
		// TODO: check that the expression is a truth value?
		i.code.accept(this);
		if (i.elseCode != null) {
			i.elseCode.accept(this);
		}
		return null;
	}

	@Override
	public Type visitAssign(Assign a) {
		if(!(a.left instanceof VarExpr || a.left instanceof FieldAccessExpr || a.left instanceof ArrayAccessExpr || a.left instanceof ValueAtExpr)) {
			error("Cannot assign to a "+a.toString()+", must be a variable, field, array element, or pointed to by pointer");
			return null;
		}
		
		Type left = a.left.accept(this);
		Type right = a.right.accept(this);
		if (left != right) {
			error("assignment attempts to assign an expression of type " + right.toString() + " to expression of type"
					+ left.toString());
		}
		// TODO: return a truth value? left side type?
		return null;
	}

	@Override
	public Type visitReturn(Return r) {
		// TODO Auto-generated method stub

		// TODO check return type consistency with function it is in
		return null;
	}

	@Override
	public Type visitValueAtExpr(ValueAtExpr va) {
		// TODO Auto-generated method stub
		return null;
	}

}
