package ast;

public class ArrayType implements Type {

	public final Type type;
	public final int length;

	public ArrayType(Type t, int len) {
		type = t;
		length = len;
	}

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitArrayType(this);
	}
}
