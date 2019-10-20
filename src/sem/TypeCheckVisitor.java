package sem;

import java.util.*;

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
		List<Type> returnVals = new LinkedList<Type>();
		for (Stmt s : b.code) {
			// the only stmt to return types are the return statement; we must check these
			// are the same, this is the return type of the block (which is only used in
			// functions, and thus allows us to check that the declared type and actual
			// return type are the same)
			Type n = s.accept(this);
			if (n != null) {
				returnVals.add(n);
			}
		}
		// converting the list to set and back removes all duplicate items
		returnVals = new LinkedList<Type>(new HashSet<Type>(returnVals));
		Type returnType = null;
		if (returnVals.size() != 1) {
			String e = "Multiple return types from block: ";
			for (Type t : returnVals) {
				e += t.toString();
				e += ",";
			}
			error(e);
		} else {
			returnType = returnVals.get(0);
		}

		scopeStack.pop();
		return returnType;
	}

	@Override
	public Type visitFunDecl(FunDecl p) {
		// TODO: not sure how scoping will interact with params here;
		// we make a new scope, so params can be shadowed?
		putSymbol(new FunSymbol(p));
		for (VarDecl v : p.params) {
			v.accept(this);
		}
		Type returnTypeActual = p.block.accept(this);
		if (p.type != returnTypeActual) {
			error("Function " + p.name
					+ " has inconsistency between declared return type and actual return type, must be "
					+ p.type.toString());
		}
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
		il.type = BaseType.INT;
		return BaseType.INT;
	}

	@Override
	public Type visitStrLiteral(StrLiteral sl) {
		return new PointerType(BaseType.CHAR);
	}

	@Override
	public Type visitChrLiteral(ChrLiteral cl) {
		cl.type = BaseType.CHAR;
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
		Type ret = null;
		switch (bo.op) {
		case ADD:
			ret = left;
			break;
		case AND:
			if (left != BaseType.INT) {
				error("Logical AND only be done on operands of type Int, not " + left.toString());
			}
			ret = BaseType.INT; // INTs are our substitute for proper booleans
			break;
		case DIV:
			if (left != BaseType.INT) {
				error("Division can only be done on operands of type Int, not " + left.toString());
			}

			ret = BaseType.INT;
			break;
		case EQ:
		case GE:
		case GT:
		case LE:
		case LT:
			if (left != BaseType.INT) {
				error("Comparison can only be done on operands of type Int, not " + left.toString());
			}
			ret = BaseType.INT;
			break;
		case MOD:
			if (left != BaseType.INT) {
				error("Remainder division can only be done on operands of type Int, not " + left.toString());
			}
			ret = BaseType.INT;
			break;
		case MUL:
			if (left != BaseType.INT) {
				error("Multiplication can only be done on operands of type Int, not " + left.toString());
			}
			ret = BaseType.INT;
			break;
		case NE:
			if (left != BaseType.INT) {
				error("NOT EQUAL can only be done on operands of type Int, not " + left.toString());
			}
			ret = BaseType.INT;
			break;
		case OR:
			if (left != BaseType.INT) {
				error("Logical OR can only be done on operands of type Int, not " + left.toString());
			}
			ret = BaseType.INT;
			break;
		case SUB:
			if (left != BaseType.INT) {
				error("Subtraction can only be done on operands of type Int, not " + left.toString());
			}
			ret = BaseType.INT;
			break;
		}
		bo.type = ret;
		return ret;
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
		Type e = w.expr.accept(this);
		if (e != BaseType.INT) {
			error("WHILE condition expression must evaluate to type Int, not " + e.toString());
		}
		w.code.accept(this);
		return null;
	}

	@Override
	public Type visitIf(If i) {
		Type e = i.expr.accept(this);
		if (e != BaseType.INT) {
			error("IF condition expression must evaluate to type Int, not " + e.toString());
		}
		i.code.accept(this);
		if (i.elseCode != null) {
			i.elseCode.accept(this);
		}
		return null;
	}

	@Override
	public Type visitAssign(Assign a) {
		if (!(a.left instanceof VarExpr || a.left instanceof FieldAccessExpr || a.left instanceof ArrayAccessExpr
				|| a.left instanceof ValueAtExpr)) {
			error("Cannot assign to a " + a.toString()
					+ ", must be a variable, field, array element, or pointed to by pointer");
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
		Type ret = r.expr.accept(this);

		return ret;
	}

	@Override
	public Type visitValueAtExpr(ValueAtExpr va) {
		// TODO Auto-generated method stub
		return null;
	}

}
