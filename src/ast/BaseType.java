package ast;

public enum BaseType implements Type {
	INT, CHAR, VOID;

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitBaseType(this);
	}

	@Override
	public boolean Equals(Type other) {
		return other == this;
	}
}
