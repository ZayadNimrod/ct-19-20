package gen;

import ast.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.EmptyStackException;
import java.util.Stack;

import java.util.List;
import java.util.Optional;
import java.util.LinkedList;

public class CodeGenerator implements ASTVisitor<Register> {

	/*
	 * Simple register allocator.
	 */

	// contains all the free temporary registers
	private Stack<Register> freeRegs = new Stack<Register>();

	public CodeGenerator() {
		freeRegs.addAll(Register.tmpRegs);
	}

	private class RegisterAllocationError extends Error {
	}

	private Register getRegister() {
		try {
			return freeRegs.pop();
		} catch (EmptyStackException ese) {
			throw new RegisterAllocationError(); // no more free registers, bad luck!
		}
	}

	private void freeRegister(Register reg) {
		freeRegs.push(reg);
	}

	private PrintWriter writer; // use this writer to output the assembly instructions

	public void emitProgram(Program program, File outputFile) throws FileNotFoundException {
		writer = new PrintWriter(outputFile);

		visitProgram(program);
		writer.close();
	}

	private void writeLine(String line) {
		writer.write(line + "\n");
	}

	@Override
	public Register visitBlock(Block b) {
		// TODO: to complete
		return null;
	}

	@Override
	public Register visitProgram(Program p) {
		writeLine("data:");
		for (StructTypeDecl s : p.structTypeDecls) {
			s.accept(this);
		}

		for (VarDecl v : p.varDecls) {
			v.accept(this);
		}
		writeLine("text:");

		// get the entrypoint main() function first

		List<FunDecl> funDecls = new LinkedList<FunDecl>(p.funDecls);
		Optional<FunDecl> maybeMain = funDecls.stream().filter(x -> x.name.equals("main")).findFirst();
		if (!maybeMain.isPresent()) {
			System.out.println("program does not have a main function");
			return null;// ? will this even ever happen, we've already checked for this
		}
		FunDecl main = maybeMain.get();
		// TODO: check for correct function signature (i.e no args)
		funDecls.remove(main);

		main.accept(this);

		// exit properly from main
		writeLine("li $v0, 10");
		writeLine("syscall");

		for (FunDecl f : funDecls) {
			f.accept(this);
		}

		return null;
	}

	@Override
	public Register visitBaseType(BaseType bt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitPointerType(PointerType p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitStructType(StructType s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitArrayType(ArrayType a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitStructTypeDecl(StructTypeDecl st) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitVarDecl(VarDecl vd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitFunDecl(FunDecl p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitIntLiteral(IntLiteral il) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitStrLiteral(StrLiteral sl) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitChrLiteral(ChrLiteral cl) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitVarExpr(VarExpr v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitFunCallExpr(FunCallExpr fc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitBinOp(BinOp bo) {
		
		//TODO: they want us to do comparision with control flow?
		switch (bo.op) {
		case ADD:
			return visitAdd(bo);
		case AND:
			return visitAnd(bo);
		case DIV:
			return visitDiv(bo);
		case EQ:
			return visitEqual(bo);
		case GE: {
			// this is simply the inverse operation of LESS THAN
			Register result = visitLT(bo);
			// yes, this can just result in two flips in a row
			// oh no 4 whole instructions, wasted
			invertRegister(result);
			return result;
		}
		case GT:
			return visitGT(bo);
		case LE: {
			// inverse operation of GREATER THAN
			Register result = visitGT(bo);
			invertRegister(result);
			return result;
		}
		case LT:
			return visitLT(bo);
		case MOD:
			return visitMod(bo);
		case MUL:
			return visitMul(bo);
		case NE: {
			Register result = visitEqual(bo);
			invertRegister(result);
			return result;
		}
		case OR:
			return visitOr(bo);
		case SUB:
			return visitSub(bo);
		default:
			return null;

		}

	}

	/// TODO a lot of repeated code, perhaps one function that takes in the proper
	/// command, and its immediate version if appropriate?
	// no, becuase then literal calculation needs some sort of delegate/lambda

	protected Register visitAdd(BinOp bo) {
		// check this is an immediate operation

		if (bo.left instanceof IntLiteral && bo.right instanceof IntLiteral) {
			// both are int literals (ADD does not operate on anything else here)
			Register result = getRegister();/// yes we could just evaluate all the literal operations beforehand but
											/// doing more than depth 1 seems to be more complex...
			int value = ((IntLiteral) (bo.left)).lit + ((IntLiteral) (bo.right)).lit;
			writeLine("li " + result.toString() + ", " + value);
			return result;
		} else if (bo.left instanceof IntLiteral && !(bo.right instanceof IntLiteral)) {
			// literal on left side
			int lit = ((IntLiteral) (bo.left)).lit;
			Register rightReg = bo.right.accept(this);
			// use the right register as destination
			writeLine("addi " + rightReg.toString() + ", " + rightReg.toString() + ", " + lit);
			return rightReg;
		} else if (!(bo.left instanceof IntLiteral) && bo.right instanceof IntLiteral) {
			// literal on right side
			int lit = ((IntLiteral) (bo.right)).lit;
			Register leftReg = bo.left.accept(this);
			// use the left register as destination
			writeLine("addi " + leftReg.toString() + ", " + leftReg.toString() + ", " + lit);
			return leftReg;
		} else {
			// both left and right are expressions of their own...
			Register left = bo.left.accept(this);
			Register right = bo.right.accept(this);
			// use left register as the destination

			writeLine("add " + left.toString() + ", " + left.toString() + ", " + right.toString());

			freeRegister(right);
			return left;
		}

	}

	protected Register visitSub(BinOp bo) {
		// check this is an immediate operation

		if (bo.left instanceof IntLiteral && bo.right instanceof IntLiteral) {
			// both are int literals
			Register result = getRegister();/// yes we could just evaluate all the literal operations beforehand but
											/// doing more than depth 1 seems to be more complex...
			int value = ((IntLiteral) (bo.left)).lit - ((IntLiteral) (bo.right)).lit;
			writeLine("li " + result.toString() + ", " + value);
			return result;

		} else if (bo.left instanceof IntLiteral && !(bo.right instanceof IntLiteral)) {
			// literal on left side
			int lit = ((IntLiteral) (bo.left)).lit;
			Register rightReg = bo.right.accept(this);
			// use the right register as destination
			writeLine("addi " + rightReg.toString() + ", " + rightReg.toString() + ", -" + lit);
			return rightReg;
		} else if (!(bo.left instanceof IntLiteral) && bo.right instanceof IntLiteral) {
			// literal on right side
			int lit = ((IntLiteral) (bo.right)).lit;
			Register leftReg = bo.left.accept(this);
			// use the left register as destination
			writeLine("addi " + leftReg.toString() + ", " + leftReg.toString() + ", -" + lit);
			return leftReg;
		} else {
			// no subi command exists, so we have to create this expression manually, as at
			// least one operand is not literal
			Register left = bo.left.accept(this);
			Register right = bo.right.accept(this);
			// use left register as the destination

			writeLine("addi " + left.toString() + ", " + left.toString() + ", " + right.toString());

			freeRegister(right);
			return left;
		}

	}

	protected Register visitDiv(BinOp bo) {

		Register left = bo.left.accept(this);
		Register right = bo.right.accept(this);
		// use left register as the destination

		writeLine("div " + left.toString() + ", " + right.toString());
		// div value is stroed in lo, hi has mod
		// move divided value to left register
		writeLine("mflo " + left.toString());

		freeRegister(right);
		return left;

	}

	protected Register visitMul(BinOp bo) {
		// check this is an immediate operation

		if (bo.left instanceof IntLiteral && bo.right instanceof IntLiteral) {
			// both are int literals (ADD does not operate on anything else here)
			Register result = getRegister();/// yes we could just evaluate all the literal operations beforehand but
											/// doing more than depth 1 seems to be more complex...
			int value = ((IntLiteral) (bo.left)).lit * ((IntLiteral) (bo.right)).lit;
			writeLine("li " + result.toString() + ", " + value);
			return result;
		} else {
			Register left = bo.left.accept(this);
			Register right = bo.right.accept(this);
			// use left register as the destination

			writeLine("mul " + left.toString() + ", " + left.toString() + ", " + right.toString());

			freeRegister(right);
			return left;
		}

	}

	protected Register visitMod(BinOp bo) {

		Register left = bo.left.accept(this);
		Register right = bo.right.accept(this);
		// use left register as the destination

		writeLine("div " + left.toString() + ", " + right.toString());
		// div value is stroed in lo, hi has mod
		// move divided value to left register
		writeLine("mfhi " + left.toString());

		freeRegister(right);
		return left;

	}

	protected Register visitAnd(BinOp bo) {
		// check this is an immediate operation

		if (bo.left instanceof IntLiteral && !(bo.right instanceof IntLiteral)) {
			// literal on left side
			int lit = ((IntLiteral) (bo.left)).lit;
			Register rightReg = bo.right.accept(this);
			// use the right register as destination
			writeLine("andi " + rightReg.toString() + ", " + rightReg.toString() + ", " + lit);
			return rightReg;
		} else if (!(bo.left instanceof IntLiteral) && bo.right instanceof IntLiteral) {
			// literal on right side
			int lit = ((IntLiteral) (bo.right)).lit;
			Register leftReg = bo.left.accept(this);
			// use the left register as destination
			writeLine("andi " + leftReg.toString() + ", " + leftReg.toString() + ", " + lit);
			return leftReg;
		} else {
			// both left and right are expressions of their own, or both literals
			// I feel bad about not precalculating the 2-literal AND but oh well
			// TODO precalculate 2-literal ANDs
			Register left = bo.left.accept(this);
			Register right = bo.right.accept(this);
			// use left register as the destination

			writeLine("and " + left.toString() + ", " + left.toString() + ", " + right.toString());

			freeRegister(right);
			return left;
		}

	}

	protected Register visitOr(BinOp bo) {
		// check this is an immediate operation

		if (bo.left instanceof IntLiteral && !(bo.right instanceof IntLiteral)) {
			// literal on left side
			int lit = ((IntLiteral) (bo.left)).lit;
			Register rightReg = bo.right.accept(this);
			// use the right register as destination
			writeLine("ori " + rightReg.toString() + ", " + rightReg.toString() + ", " + lit);
			return rightReg;
		} else if (!(bo.left instanceof IntLiteral) && bo.right instanceof IntLiteral) {
			// literal on right side
			int lit = ((IntLiteral) (bo.right)).lit;
			Register leftReg = bo.left.accept(this);
			// use the left register as destination
			writeLine("ori " + leftReg.toString() + ", " + leftReg.toString() + ", " + lit);
			return leftReg;
		} else {
			// both left and right are expressions of their own, or both literals
			// I feel bad about not precalculating the 2-literal AND but oh well
			// TODO precalculate 2-literal ANDs
			Register left = bo.left.accept(this);
			Register right = bo.right.accept(this);
			// use left register as the destination

			writeLine("or " + left.toString() + ", " + left.toString() + ", " + right.toString());

			freeRegister(right);
			return left;
		}

	}

	protected Register visitLT(BinOp bo) {
		// check this is an immediate operation

		if (bo.left instanceof IntLiteral && bo.right instanceof IntLiteral) {
			// both are int literals
			Register result = getRegister();
			boolean value = ((IntLiteral) (bo.left)).lit < ((IntLiteral) (bo.right)).lit;
			if (value) {
				writeLine("li " + result.toString() + ", 1");
			} else {
				writeLine("li " + result.toString() + ", 0");
			}
			return result;
		} else if (bo.left instanceof IntLiteral && !(bo.right instanceof IntLiteral)) {
			// literal on left side
			int lit = ((IntLiteral) (bo.left)).lit;
			Register rightReg = bo.right.accept(this);
			// use the right register as destination
			writeLine("slti " + rightReg.toString() + ", " + rightReg.toString() + ", " + lit);
			// this returns true if the left side is larger than right side, which is wrong
			// so we flip it
			invertRegister(rightReg);
			return rightReg;
		} else if (!(bo.left instanceof IntLiteral) && bo.right instanceof IntLiteral) {
			// literal on right side
			int lit = ((IntLiteral) (bo.right)).lit;
			Register leftReg = bo.left.accept(this);
			// use the left register as destination
			writeLine("slti " + leftReg.toString() + ", " + leftReg.toString() + ", " + lit);
			return leftReg;
		} else {
			// both left and right are expressions of their own...
			Register left = bo.left.accept(this);
			Register right = bo.right.accept(this);
			// use left register as the destination

			writeLine("slt " + left.toString() + ", " + left.toString() + ", " + right.toString());

			freeRegister(right);
			return left;
		}

	}

	protected Register visitGT(BinOp bo) {
		// check this is an immediate operation

		if (bo.left instanceof IntLiteral && bo.right instanceof IntLiteral) {
			// both are int literals
			Register result = getRegister();
			boolean value = ((IntLiteral) (bo.left)).lit > ((IntLiteral) (bo.right)).lit;
			if (value) {
				writeLine("li " + result.toString() + ", 1");
			} else {
				writeLine("li " + result.toString() + ", 0");
			}
			return result;
		} else if (bo.left instanceof IntLiteral && !(bo.right instanceof IntLiteral)) {
			// literal on left side
			int lit = ((IntLiteral) (bo.left)).lit;
			Register rightReg = bo.right.accept(this);
			writeLine("slti " + rightReg.toString() + ", " + rightReg.toString() + ", " + lit);
			return rightReg;
		} else if (!(bo.left instanceof IntLiteral) && bo.right instanceof IntLiteral) {
			// literal on right side
			int lit = ((IntLiteral) (bo.right)).lit;
			Register leftReg = bo.left.accept(this);
			writeLine("slti " + leftReg.toString() + ", " + leftReg.toString() + ", " + lit);
			invertRegister(leftReg);
			return leftReg;
		} else {
			// both left and right are expressions of their own...
			Register left = bo.left.accept(this);
			Register right = bo.right.accept(this);
			// use left register as the destination

			writeLine("slt " + left.toString() + ", " + right.toString() + ", " + left.toString());

			freeRegister(right);
			return left;
		}

	}

	protected Register visitEqual(BinOp bo) {
		Register left = bo.left.accept(this);
		Register right = bo.right.accept(this);
		// use left register as the destination

		writeLine("seq " + left.toString() + ", " + left.toString() + ", " + right.toString());

		freeRegister(right);
		return left;
	}

	@Override
	public Register visitArrayAccessExpr(ArrayAccessExpr ae) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitFieldAccessExpr(FieldAccessExpr fa) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitSizeOfExpr(SizeOfExpr so) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitTypecastExpr(TypecastExpr tc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitExprStmt(ExprStmt e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitWhile(While w) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitIf(If i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitAssign(Assign a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitReturn(Return r) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitValueAtExpr(ValueAtExpr va) {
		// TODO Auto-generated method stub
		return null;
	}

	private void invertRegister(Register r) {
		writeLine("addi " + r.toString() + ", " + r.toString() + ", -1");
		writeLine("negu " + r.toString());
	}

}
