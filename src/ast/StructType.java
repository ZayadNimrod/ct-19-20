package ast;

public class StructType implements Type {

	public String structType;

	public StructType(String s) {
		structType = s;
	}

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitStructType(this);
	}

	@Override
	public boolean Equals(Type other) {
		if(other instanceof StructType) {
			StructType o = (StructType) other;
			return structType.equals(o.structType);
		}
		return false;
	}
}
