package ast;

public class PointerType implements Type {

	public Type pointerToType;

	public PointerType(Type p) {
		pointerToType = p;
	}

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitPointerType(this);
	}
}
