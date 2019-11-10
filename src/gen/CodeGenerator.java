package gen;

import ast.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.LinkedList;

public class CodeGenerator implements ASTVisitor<Register> {

	/*
	 * Simple register allocator.
	 */

	// TODO: comment each block?
	// contains all the free temporary registers
	private Stack<Register> freeRegs = new Stack<Register>();

	private Map<String, StructTypeDecl> structs = new HashMap<String, StructTypeDecl>();
	private int functionVarOffsets;

	private FunDecl currentFunDecl;

	int uidGen = 0;

	private int uid() {
		return ++uidGen;
	}

	int alloced = 0;

	public CodeGenerator() {
		freeRegs.addAll(Register.tmpRegs);
	}

	private class RegisterAllocationError extends Error {
	}

	private Register getRegister() {
		try {
			Register r = freeRegs.pop();
			// System.out.println("checked out "+r);
			return r;
		} catch (EmptyStackException ese) {
			System.out.println("WEEP WOOP NO REGISTERS");
			throw new RegisterAllocationError(); // no more free registers, bad luck!
		}
	}

	private void freeRegister(Register reg) {
		if (reg == null) {
			throw new RegisterAllocationError();
		}
		// System.out.println("checked in "+reg);
		freeRegs.push(reg);
	}

	private PrintWriter writer; // use this writer to output the assembly instructions

	public void emitProgram(Program program, File outputFile) throws FileNotFoundException {
		writer = new PrintWriter(outputFile);

		visitProgram(program);

		System.out.println("All registers returned: " + (freeRegs.size() == Register.tmpRegs.size()));
		writer.close();
	}

	private void writeLine(String line) {
		writer.write(line + "\n");
	}

	@Override
	public Register visitProgram(Program p) {
		writeLine(".data");
		for (StructTypeDecl s : p.structTypeDecls) {
			s.accept(this);
		}

		for (VarDecl v : p.varDecls) {
			// v.accept(this);
			// in this case, variable declaration is done on the heap
			visitGlobal(v);
		}
		// create spaces for all our string literals, since I don't fancy creating them
		// at runtime
		// for some reason if we do this before the global variable adreses it breaks
		StringLiteralVisitor stringLiteralAllocator = new StringLiteralVisitor();

		stringLiteralAllocator.writeStrings(p, writer);

		writeLine(".text");

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

		// TODO return values?
		// TODO what if a void function has no return stmt

		// exit properly from main
		writeLine("li $v0, 10");
		writeLine("syscall");

		for (FunDecl f : funDecls) {
			f.accept(this);
		}

		return null;
	}

	private void visitGlobal(VarDecl v) {
		// TODO check the type of the variable.

		int size = getSizeOf(v.type);
		size = (int) Math.ceil(size / 4.0) * 4;
		v.offset = -1;
		writeLine(v.varName + ": .space " + size);

	}

	private int getSizeOf(Type type) {

		// if it is a pointer, allocate 1 word of memory
		// if it is an array, fine the underlying type, and allocate its size * the
		// length words of memory
		// TODO struct type
		// primitive type gets 1 word of memory

		if (type == BaseType.INT || type instanceof PointerType) {
			return 4;
		} else if (type == BaseType.CHAR) {
			return 1;
		} else if (type instanceof ArrayType) {
			return ((ArrayType) type).length * getSizeOf(((ArrayType) type).type);
		} else if (type instanceof StructType) {
			int cumulative = 0;

			StructTypeDecl s = structs.get(((StructType) type).structType);
			for (VarDecl v : s.variables) {
				cumulative += getSizeOf(v.type);
			}
			return cumulative;
		}

		System.out.println("WARNING: OBJECT HAS NO SIZE");
		return 0;

	}

	@Override
	public Register visitBlock(Block b) {
		int size = 0;

		for (VarDecl v : b.vars) {
			v.accept(this);
			size += (int) (Math.ceil(getSizeOf(v.type) / 4.0) * 4);
		}
		// functionVarOffsets += size;
		// TODO; is that bit even necessary?
		Register ret = null;
		for (Stmt s : b.code) {
			Register r = s.accept(this);
			if (r != null) {
				ret = r;
			}
		}

		// free up those vars
		writeLine("addi $sp $sp " + size);
		functionVarOffsets -= size;
		return ret;
	}

	@Override
	public Register visitBaseType(BaseType bt) {

		System.out.println("NOT IMPLEMENTED BASETYPE");
		return null;
	}

	@Override
	public Register visitPointerType(PointerType p) {

		System.out.println("NOT IMPLEMENTED POINTERTYPE");
		return null;
	}

	@Override
	public Register visitStructType(StructType s) {

		System.out.println("NOT IMPLEMENTED STRUCTYPE");
		return null;
	}

	@Override
	public Register visitArrayType(ArrayType a) {

		System.out.println("NOT IMPLEMENTED ARRAYTYPE");
		return null;
	}

	@Override
	public Register visitStructTypeDecl(StructTypeDecl st) {
		int offset = 0;
		for (VarDecl v : st.variables) {
			v.offset = offset;
			offset += getSizeOf(v.type);
		}
		structs.put(st.structDecl.structType, st);
		return null;
	}

	@Override
	public Register visitVarDecl(VarDecl vd) {

		// allocate a *local* variable (globals have thier own function)
		int size = getSizeOf(vd.type);
		int effSize = (int) (Math.ceil(size / 4.0) * 4);// normalise to 4-byte boundary
		functionVarOffsets += effSize;
		vd.offset = functionVarOffsets;

		// advance the stack pointer to make space for the new variable?
		writeLine("addi " + Register.sp + ", " + Register.sp + ", -" + effSize);
		return null;
	}

	@Override
	public Register visitFunDecl(FunDecl p) {
		// useing the currentFunDecl is awful and annihiliates encapsulation, but I
		// can't get p into the epilogue otherwise...

		functionVarOffsets = 0;
		currentFunDecl = p;
		writeLine("function_" + p.name + ":");

		prologue(p);

		p.block.accept(this);

		if (p.type == BaseType.VOID) {
			// it is possible to have a void function not have an explicit RETURN stmt
			// so we will write it here just in case.
			if (currentFunDecl.name.equals("main")) {
				// return from main
				writeLine("#returning from main");

			} else {
				// any "normal" function
				writeLine("#returning from function");

				epilogue();
				return Register.v0;
			}
		}

		return Register.v0;
	}

	private void prologue(FunDecl p) {
		writeLine("#prologue start");
		// do the function prologue

		// save frame pointer
		writeLine("addi $sp $sp -4");
		writeLine("sw $fp 0($sp)");

		// set frame pointer to stack pointer
		writeLine("move $fp $sp");
		// place arg registers on the stack - NOTE: the overflow args are just under the
		// SP

		for (int i = 0; i < p.params.size(); i++) {
			if (i < 4) {
				writeLine("addi $sp $sp -4");
				writeLine("sw " + Register.paramRegs[i] + " 0($sp)");
				p.params.get(i).offset = (i + 1) * 4;
				functionVarOffsets += 4;
			} else {
				p.params.get(i).offset = -4 - (p.params.size() - 5) * 4;// Return Address, depth of argument
				functionVarOffsets += 4;
			}
		}

		// TODO save function registers to restore later

		// TODO find all function registers that are in use, save them (and free the
		// registers, to reallocate later?)

		// TODO: inUse in the precall/postcall also includes these...

		writeLine("#prologue end");
	}

	private void epilogue() {
		writeLine("#epilogue start");
		// do the function epilogue

		// TODO restore function variables

		// these have all been popped off by the return stms
		// pop arg registers off the stack

		// throw away the locals
		writeLine("move $sp $fp");

//		for (int i = currentFunDecl.params.size() - 1; i >= 0; i--) {
//			if (i < 4) {
//				//writeLine("addi $sp $sp 4");
//			} else {
//
//			}
//		}

		// restore frame pointer
		writeLine("lw $fp 0($sp)");
		writeLine("addi $sp $sp 4");

		writeLine("#epilogue end");

		writeLine("jr $ra");
	}

	@Override
	public Register visitIntLiteral(IntLiteral il) {
		Register ret = getRegister();
		writeLine("li " + ret + ", " + il.lit);
		return ret;
	}

	@Override
	public Register visitStrLiteral(StrLiteral sl) {
		// String literals are char pointers
		Register addr = getRegister();
		writeLine("la " + addr + " " + sl.id);
		return addr;
	}

	@Override
	public Register visitChrLiteral(ChrLiteral cl) {
		Register ret = getRegister();
		writeLine("li " + ret + ", " + (int) (cl.lit));
		return ret;
	}

	@Override
	public Register visitVarExpr(VarExpr v) {
		// TODO check with sizes?

		// check this is not a global
		Register value = null;
		if (v.vd.offset == -1) {

			// this is a global
			value = getRegister();
			writeLine("la " + value + ", " + v.name);			
			writeLine("lw " + value + ", 0(" + value + ")");
		} else {
			// get address of variable, which is our current frame pointer+offset
			Register addrRegister = getRegister();

			// System.out.println("Variable " + vd.varName + ": offset " + vd.offset);

			writeLine("move " + addrRegister + ", $fp");
			// load variable from address
			writeLine("lw " + addrRegister + ", " + (-v.vd.offset) + "(" + addrRegister + ")");
			value = addrRegister;
		}
		return value;
	}

	

	@Override
	public Register visitFunCallExpr(FunCallExpr fc) {
		if (fc.name.equals("print_i")) {
			return visitPrint_i(fc);
		} else if (fc.name.equals("print_s")) {
			return visitPrint_s(fc);
		} else if (fc.name.equals("read_i")) {
			return visitRead_i(fc);
		} else if (fc.name.equals("read_c")) {
			return visitRead_c(fc);
		} else if (fc.name.equals("print_c")) {
			return visitPrint_c(fc);
		}

		// TODO: what about struct arguments, can't put those in an arg

		precall(fc);

		writeLine("jal function_" + fc.name);

		Register ret = getRegister();
		writeLine("move " + ret + ", " + Register.v0);

		postcall(fc, ret);

		return ret;
	}

	private void precall(FunCallExpr fc) {
		writeLine("#precall begins");

		// save temporaries

		// to get used registers, diff freeRegs with tmpRegs; this is OK, since
		// the funCall DEFINTION has already been written, so register usage is STATIC!
		// We can replciate this on the other end too, since the defintion is NOT being
		// .accepted-ed, so THE REGISTERS IN USE IS IDENTICAL ACROSS THE FUNCTION CALL

		// did I mention I hate java? this is supposed a simple difference function
		List<Register> inUse = (new LinkedList<Register>(Register.tmpRegs));
		inUse.removeAll(new LinkedList<Register>(freeRegs));

		for (int i = 0; i < inUse.size(); i++) {
			Register r = inUse.get(i);
			writeLine("addi $sp $sp -4");
			writeLine("sw " + r + " 0($sp)");
		}

		// save old args?
		for (int i = 0; i < Register.paramRegs.length; i++) {

			Register r = Register.paramRegs[i];
			writeLine("addi $sp $sp -4");
			writeLine("sw " + r + " 0($sp)");
		}

		// save value registers (v0,v1)
		writeLine("addi $sp $sp -4");
		writeLine("sw " + Register.v0 + " 0($sp)");

		writeLine("addi $sp $sp -4");
		writeLine("sw $v1 0($sp)");

		// put return address on stack
		writeLine("addi $sp $sp -4");
		writeLine("sw $ra 0($sp)");

		// put args on stack

		for (int i = 0; i < fc.args.size(); i++) {
			Register r = fc.args.get(i).accept(this);
			// System.out.println(fc.args.get(i));
			if (i < 4) {
				writeLine("move " + Register.paramRegs[i] + ", " + r);

			} else {
				// write arg to stack
				writeLine("addi $sp $sp -4");
				writeLine("sw " + r + " 0($sp)");
			}
			freeRegister(r);
		}

		writeLine("#precall ends");

	}

	private void postcall(FunCallExpr fc, Register doNotRestore) {

		writeLine("#postcall begins");
		// remove args from stack
		for (int i = fc.args.size(); i >= 0; i--) {
			if (i < 4) {
				// put back into register?
				// writeLine("sw " + Register.paramRegs[i] + " 0($sp)");
				// writeLine("addi $sp $sp 4");
			} else {
				// pop arg off the stack
				writeLine("addi $sp $sp 4");
			}
		}

		// restore return address from stack
		writeLine("lw $ra 0($sp)");
		writeLine("addi $sp $sp 4");

		// restore value registers
		writeLine("lw $v1 0($sp)");
		writeLine("addi $sp $sp 4");

		writeLine("lw $v0 0($sp)");
		writeLine("addi $sp $sp 4");

		// restore old args?
		for (int i = Register.paramRegs.length - 1; i >= 0; i--) {
			Register r = Register.paramRegs[i];
			writeLine("lw " + r + " 0($sp)");
			writeLine("addi $sp $sp 4");
		}

		// restore temporaries
		List<Register> inUse = (new LinkedList<Register>(Register.tmpRegs));
		inUse.removeAll(new LinkedList<Register>(freeRegs));
		inUse.remove(doNotRestore);

		for (int i = inUse.size() - 1; i >= 0; i--) {
			Register r = inUse.get(i);
			writeLine("lw " + r + " 0($sp)");
			writeLine("addi $sp $sp 4");
		}

		writeLine("#postcall ends");
	}

	private Register visitPrint_i(FunCallExpr fc) {

		writeLine("#print_i");
		// save registers that are to be used

		writeLine("addi $sp $sp -4");
		writeLine("sw $a0 0($sp)");
		writeLine("addi $sp $sp -4");
		writeLine("sw $v0 0($sp)");
		Register printThis = fc.args.get(0).accept(this);
		writeLine("move $a0 " + printThis);
		writeLine("li $v0 1");
		writeLine("syscall");
		freeRegister(printThis);

		// restore them

		writeLine("lw $v0 0($sp)");
		writeLine("addi $sp $sp 4");
		writeLine("lw $a0 0($sp)");
		writeLine("addi $sp $sp 4");
		writeLine("#print_i over");
		return null;
	}

	private Register visitPrint_s(FunCallExpr fc) {
		writeLine("#print_s begins");
		writeLine("addi $sp $sp -4");
		writeLine("sw $a0 0($sp)");
		writeLine("addi $sp $sp -4");
		writeLine("sw $v0 0($sp)");
		Register printThis = fc.args.get(0).accept(this);
		writeLine("move $a0, " + printThis);
		writeLine("li $v0, 4");
		writeLine("syscall");
		freeRegister(printThis);
		writeLine("lw $v0 0($sp)");
		writeLine("addi $sp $sp 4");
		writeLine("lw $a0 0($sp)");
		writeLine("addi $sp $sp 4");
		writeLine("#print_s ends");
		return null;
	}

	private Register visitRead_i(FunCallExpr fc) {
		writeLine("#read_i");
		writeLine("addi $sp $sp -4");
		writeLine("sw $v0 0($sp)");
		writeLine("li $v0, 5");
		writeLine("syscall");
		Register ret = getRegister();
		writeLine("move " + ret + ", " + Register.v0);
		writeLine("lw $v0 0($sp)");
		writeLine("addi $sp $sp 4");
		writeLine("#read_i ends");
		return ret;
	}

	private Register visitRead_c(FunCallExpr fc) {
		writeLine("#read_c");
		writeLine("addi $sp $sp -4");
		writeLine("sw $v0 0($sp)");

		writeLine("li $v0, 12");
		// get buffer to write to

		writeLine("syscall");
		Register ret = getRegister();
		writeLine("move " + ret + ", " + Register.v0);
		
		writeLine("lw $v0 0($sp)");
		writeLine("addi $sp $sp 4");
		writeLine("#read_c ends");
		return ret;
	}

	private Register visitPrint_c(FunCallExpr fc) {

		writeLine("#print_c");
		// save registers that are to be used

		writeLine("addi $sp $sp -4");
		writeLine("sw $a0 0($sp)");
		writeLine("addi $sp $sp -4");
		writeLine("sw $v0 0($sp)");

		Register printThis = fc.args.get(0).accept(this);
		writeLine("addi $sp $sp -4");
		writeLine("sw " + printThis + " 0($sp)");

		writeLine("move $a0 $sp");
		writeLine("li $v0 4");
		writeLine("syscall");
		freeRegister(printThis);

		// restore them
		writeLine("addi $sp $sp 4");

		writeLine("lw $v0 0($sp)");
		writeLine("addi $sp $sp 4");
		writeLine("lw $a0 0($sp)");
		writeLine("addi $sp $sp 4");
		writeLine("#print_c over");
		return null;
	}

	@Override
	public Register visitBinOp(BinOp bo) {
		// TODO: try to do depth-first to use less registers
		// TODO: char comparisions..?

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
			invertBool(result);
			return result;
		}
		case GT:
			return visitGT(bo);
		case LE: {
			// inverse operation of GREATER THAN
			Register result = visitGT(bo);
			invertBool(result);
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
			invertBool(result);
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
			writeLine("li " + result + ", " + value);
			return result;
		} else if (bo.left instanceof IntLiteral && !(bo.right instanceof IntLiteral)) {
			// literal on left side
			int lit = ((IntLiteral) (bo.left)).lit;
			Register rightReg = bo.right.accept(this);
			// use the right register as destination
			writeLine("addi " + rightReg + ", " + rightReg + ", " + lit);
			return rightReg;
		} else if (!(bo.left instanceof IntLiteral) && bo.right instanceof IntLiteral) {
			// literal on right side
			int lit = ((IntLiteral) (bo.right)).lit;
			Register leftReg = bo.left.accept(this);
			// use the left register as destination
			writeLine("addi " + leftReg + ", " + leftReg + ", " + lit);
			return leftReg;
		} else {
			// both left and right are expressions of their own...
			Register left = bo.left.accept(this);
			Register right = bo.right.accept(this);
			// use left register as the destination

			writeLine("add " + left + ", " + left + ", " + right);

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
			writeLine("li " + result + ", " + value);
			return result;

		} else if (bo.left instanceof IntLiteral && !(bo.right instanceof IntLiteral)) {
			// literal on left side
			int lit = ((IntLiteral) (bo.left)).lit;
			Register rightReg = bo.right.accept(this);
			// use the right register as destination
			writeLine("negu " + rightReg + ", " + rightReg);
			writeLine("addi " + rightReg + ", " + rightReg + ", " + lit);
			return rightReg;
		} else if (!(bo.left instanceof IntLiteral) && bo.right instanceof IntLiteral) {
			// literal on right side
			int lit = ((IntLiteral) (bo.right)).lit;
			Register leftReg = bo.left.accept(this);
			// use the left register as destination
			writeLine("addi " + leftReg + ", " + leftReg + ", -" + lit);
			return leftReg;
		} else {
			// no subi command exists, so we have to create this expression manually, as at
			// least one operand is not literal
			Register left = bo.left.accept(this);
			Register right = bo.right.accept(this);
			// use left register as the destination

			writeLine("addi " + left + ", " + left + ", " + right);

			freeRegister(right);
			return left;
		}

	}

	protected Register visitDiv(BinOp bo) {

		Register left = bo.left.accept(this);
		Register right = bo.right.accept(this);
		// use left register as the destination

		writeLine("div " + left + ", " + right);
		// div value is stored in lo, hi has mod
		// move divided value to left register
		writeLine("mflo " + left);

		freeRegister(right);
		return left;

	}

	protected Register visitMul(BinOp bo) {
		// check this is an immediate operation

		if (bo.left instanceof IntLiteral && bo.right instanceof IntLiteral) {
			// both are int literals
			Register result = getRegister();
			int value = ((IntLiteral) (bo.left)).lit * ((IntLiteral) (bo.right)).lit;

			writeLine("li " + result + ", " + value);
			return result;
		} else {
			Register left = bo.left.accept(this);
			Register right = bo.right.accept(this);
			// use left register as the destination

			writeLine("mul " + left + ", " + left + ", " + right);

			freeRegister(right);
			return left;
		}

	}

	protected Register visitMod(BinOp bo) {

		Register left = bo.left.accept(this);
		Register right = bo.right.accept(this);
		// use left register as the destination

		writeLine("div " + left + ", " + right);
		// div value is stroed in lo, hi has mod
		// move divided value to left register
		writeLine("mfhi " + left);

		freeRegister(right);
		return left;

	}

	protected Register visitAnd(BinOp bo) {

		// TODO check short-circuiting works
		// ok this has to be done with control flow
		// evaluate the left hand side
		Register left = bo.left.accept(this);
		// if it is false, skip the rhs
		String endIdent = "endand_" + uid();
		writeLine("beqz " + left + " " + endIdent);
		Register right = bo.right.accept(this);
		// put right into left and free the register; we know left is already True
		writeLine("move " + left + ", " + right);
		freeRegister(right);
		writeLine(endIdent + ":");
		// left is the result of our AND

		return left;
	}

	protected Register visitOr(BinOp bo) {
		// ok this has to be done with control flow
		// evaluate the left hand side
		Register left = bo.left.accept(this);
		// if it is true, skip the rhs
		String endIdent = "endor_" + uid();
		writeLine("beqz " + left + " " + endIdent);
		Register right = bo.right.accept(this);
		// put right into left and free the register; we know left is already false
		writeLine("move " + left + ", " + right);
		freeRegister(right);
		writeLine(endIdent + ":");
		// left is the result of our OR

		return left;

	}

	protected Register visitLT(BinOp bo) {
		// check this is an immediate operation

		if (bo.left instanceof IntLiteral && bo.right instanceof IntLiteral) {
			// both are int literals
			Register result = getRegister();
			boolean value = ((IntLiteral) (bo.left)).lit < ((IntLiteral) (bo.right)).lit;
			if (value) {
				writeLine("li " + result + ", 1");
			} else {
				writeLine("li " + result + ", 0");
			}
			return result;
//		} else if (bo.left instanceof IntLiteral && !(bo.right instanceof IntLiteral)) {
//			// literal on left side
//			int lit = ((IntLiteral) (bo.left)).lit;
//			Register rightReg = bo.right.accept(this);
//			// use the right register as destination
//			writeLine("slti " + rightReg + ", " + rightReg + ", " + lit);
//			// this returns true if the left side is larger than right side, which is wrong
//			// so we flip it
//			invertBool(rightReg);
//			//TODO no, this is greater than OR EQUAL TO right now
//			return rightReg;
		} else if (!(bo.left instanceof IntLiteral) && bo.right instanceof IntLiteral) {
			// literal on right side
			int lit = ((IntLiteral) (bo.right)).lit;
			Register leftReg = bo.left.accept(this);
			// use the left register as destination
			writeLine("slti " + leftReg + ", " + leftReg + ", " + lit);
			return leftReg;
		} else {
			// both left and right are expressions of their own...
			Register left = bo.left.accept(this);
			Register right = bo.right.accept(this);
			// use left register as the destination

			writeLine("slt " + left + ", " + left + ", " + right);

			freeRegister(right);
			return left;
		}

	}

	protected Register visitGT(BinOp bo) {
		// check this is an immediate operation
		System.out.println("GT BEGIN");
		if (bo.left instanceof IntLiteral && bo.right instanceof IntLiteral) {
			// both are int literals
			Register result = getRegister();
			boolean value = ((IntLiteral) (bo.left)).lit > ((IntLiteral) (bo.right)).lit;
			if (value) {
				writeLine("li " + result + ", 1");
			} else {
				writeLine("li " + result + ", 0");
			}
			return result;
		} else if (bo.left instanceof IntLiteral && !(bo.right instanceof IntLiteral)) {
			// literal on left side
			int lit = ((IntLiteral) (bo.left)).lit;
			Register rightReg = bo.right.accept(this);
			writeLine("slti " + rightReg + ", " + rightReg + ", " + lit);
			return rightReg;
//		} else if (!(bo.left instanceof IntLiteral) && bo.right instanceof IntLiteral) {
//			// literal on right side
//			int lit = ((IntLiteral) (bo.right)).lit;
//			Register leftReg = bo.left.accept(this);
//			writeLine("slti " + leftReg + ", " + leftReg + ", " + lit);
//			
//			invertBool(leftReg);
//
//			//TODO: no, this is greather than or equal to
//			
//			return leftReg;
		} else {
			// both left and right are expressions of their own...
			writeLine("#gt last case");
			Register left = bo.left.accept(this);
			Register right = bo.right.accept(this);
			// use left register as the destination
			// TODO this is wrong?
			writeLine("slt " + left + ", " + right + ", " + left);
			writeLine("#gt last case end");
			freeRegister(right);

			System.out.println("GT END");
			return left;
		}

	}

	protected Register visitEqual(BinOp bo) {
		Register left = bo.left.accept(this);
		Register right = bo.right.accept(this);
		// use left register as the destination

		writeLine("seq " + left + ", " + left + ", " + right);

		freeRegister(right);
		return left;
	}

	@Override
	public Register visitArrayAccessExpr(ArrayAccessExpr ae) {
		// TODO does not work for struct arrays
		// get base address of array
		Register baseAddress = ae.array.accept(this);
		// get offset;
		if (ae.index instanceof IntLiteral) {
			// get the literal offset
			int offset = ((IntLiteral) (ae.index)).lit;
			// becuase we have 4-byte words
			offset *= 4;
			writeLine("lw " + baseAddress + ", " + offset + "(" + baseAddress + ")");
			// we have the value!
		} else {
			Register offset = ae.index.accept(this);
			writeLine("sll " + offset + ", " + offset + ", 2");
			writeLine("add " + baseAddress + ", " + baseAddress + ", " + offset);
			freeRegister(offset);
			writeLine("lw " + baseAddress + ", 0(" + baseAddress + ")");
		}
		System.out.println("NOT FULLY IMPLEMENTED ARRAYACCESS");
		return baseAddress;
	}

	@Override
	public Register visitFieldAccessExpr(FieldAccessExpr fa) {

		FieldAccessExpr fae = (FieldAccessExpr) (fa.struct);

		// get base address
		Register baseAddrRegister = getRegister();
		writeLine("move " + baseAddrRegister + ", $fp");

		// get offset of the field

		int offset = -((VarExpr) (fae.struct)).vd.offset; // get addr of struct

		// I hate java
		offset -= structs.get(((StructType) (((VarExpr) (fae.struct)).vd.type)).structType).variables.stream()
				.filter(x -> x.varName.equals(fae.field)).findFirst().get().offset; // find fields offset

		writeLine("lw " + baseAddrRegister + ", " + offset + "(" + baseAddrRegister + ")");

		return baseAddrRegister;
	}

	@Override
	public Register visitSizeOfExpr(SizeOfExpr so) {
		int size = getSizeOf(so.baseType);
		Register reg = getRegister();

		writeLine("li " + reg + ", " + size);

		return reg;
	}

	@Override
	public Register visitTypecastExpr(TypecastExpr tc) {
		// we don't ned to care about type anymore!
		return tc.toConvert.accept(this);
	}

	@Override
	public Register visitExprStmt(ExprStmt e) {
		Register ret = e.expr.accept(this);
		if (ret != null) {
			freeRegister(ret);
		}
		return null;
	}

	@Override
	public Register visitWhile(While w) {
		int id = uid();
		String startLine = "while_start_" + id;
		String endLine = "while_end_" + id;
		writeLine(startLine + ":");
		Register check = w.expr.accept(this);
		// if the statement is false, jump to the end
		writeLine("beqz " + check + ", " + endLine);
		freeRegister(check);
		w.code.accept(this);
		// loop
		writeLine("j " + startLine);
		// endpoint of the loop
		writeLine(endLine + ":");
		return null;
	}

	@Override
	public Register visitIf(If i) {
		int id = uid();
		// String positiveLine = "if_if_"+id;
		String negativeLine = "if_else_" + id;
		String endLine = "if_end_" + id;
		Register condition = i.expr.accept(this);
		if (i.elseCode != null) {
			writeLine("beqz " + condition + ", " + negativeLine);
		} else {
			writeLine("beqz " + condition + ", " + endLine);
		}
		freeRegister(condition);
		// if-code starts here
		i.code.accept(this);
		writeLine("j " + endLine);
		// else code starts here
		if (i.elseCode != null) {
			writeLine(negativeLine + ":");
			i.elseCode.accept(this);
		}
		// end of if statement here
		writeLine(endLine + ":");

		return null;
	}

	@Override
	public Register visitAssign(Assign a) {
		// writeLine("START_ASSIGN:");

		Register toAssign = a.right.accept(this);

		Register assignTo = getRegister();// a.left.accept(this);

		writeLine("move " + assignTo + ", " + toAssign);

		freeRegister(toAssign);

		// write to memory

		// left is either a variable, a pointer, or an array or field access
		if (a.left instanceof VarExpr) {

			VarExpr v = (VarExpr) (a.left);

			// System.out.println("assignment to: " + v.name + ", to " + a.right.toString()
			// + " in function " + currentFunDecl.name);

			if (v.vd.offset == -1) {
				// global variable
				Register addr = getRegister();
				writeLine("la " + addr + " " + v.vd.varName);
				writeLine("sw " + assignTo + " 0(" + addr + ")");
				freeRegister(addr);
			} else {
				// local variable

				Register addrRegister = getRegister();
				writeLine("move " + addrRegister + ", $fp");
				writeLine("sw " + assignTo + ", " + (-v.vd.offset) + "(" + addrRegister + ")");
				freeRegister(addrRegister);
			}

		} else if (a.left instanceof FieldAccessExpr) {
			// TODO what about nested structs

			FieldAccessExpr fae = (FieldAccessExpr) (a.left);
			Register addrRegister = getRegister();
			writeLine("move " + addrRegister + ", $fp");
			int offset = -((VarExpr) (fae.struct)).vd.offset; // get addr of struct

			// I hate java
			offset -= structs.get(((StructType) (((VarExpr) (fae.struct)).vd.type)).structType).variables.stream()
					.filter(x -> x.varName.equals(fae.field)).findFirst().get().offset; // find fields offset

			writeLine("sw " + assignTo + ", " + offset + "(" + addrRegister + ")");
			freeRegister(addrRegister);

		} else if (a.left instanceof ArrayAccessExpr) {

			// address = sp - array offset - (idx)*elemsize

			ArrayAccessExpr aae = (ArrayAccessExpr) (a.left);
			int elemsize = getSizeOf(((ArrayType) (aae.array).type).type);
			Register addrRegister = aae.index.accept(this);
			Register temp = getRegister();

			writeLine("li " + temp + ", -" + elemsize);
			writeLine("mul " + addrRegister + ", " + addrRegister + ", " + temp);
			freeRegister(temp); // we have got -idx*elemsize

			writeLine("add " + addrRegister + ", " + addrRegister + ", " + Register.fp);
			// sp- idx*elemSize

			VarDecl vd = ((VarExpr) (aae.array)).vd;
			writeLine("addi " + addrRegister + ", " + addrRegister + ", -" + vd.offset);
			// we have the address, now write to it
			writeLine("sw " + assignTo + ", " + 0 + "(" + addrRegister + ")");
			freeRegister(addrRegister);
		} else {
			// a.left is a pointer

			// get address to write to
			ValueAtExpr va = (ValueAtExpr) (a.left);
			Register address = va.expr.accept(this);
			writeLine("sw " + assignTo + ", " + 0 + "(" + address + ")");
			freeRegister(address);

		}
		freeRegister(assignTo);

		// writeLine("END_ASSIGN:");
		return null;
	}

	@Override
	public Register visitReturn(Return r) {

		// but what about return from the base function?
		// absolutely disgusting solution but whatever
		if (currentFunDecl.name.equals("main")) {
			// return from main
			writeLine("#returning from main");
			Register reg = r.expr.accept(this);
			freeRegister(reg);
			return reg;
		} else {
			// any "normal" function
			Register reg = r.expr.accept(this);
			writeLine("move " + Register.v0 + ", " + reg);
			freeRegister(reg);
			writeLine("#returning from function");

			epilogue();
			return Register.v0;
		}
	}

	@Override
	public Register visitValueAtExpr(ValueAtExpr va) {
		Register address = va.expr.accept(this);
		writeLine("lw " + address + "0(" + address + ")");
		return address;
	}

	private void invertBool(Register r) {
		writeLine("addi " + r + ", " + r + ", -1");
		writeLine("negu " + r + ", " + r);
	}

//	private void popToStack() {
//		incrementStack();
//	}
//	
//	private void incrementStack() {
//		
//	}

}
