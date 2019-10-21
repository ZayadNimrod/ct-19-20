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

	@Override
	public boolean CheckIfTypesAreEqualThisFunctionHasALongName(Type other) {
		if(other instanceof ArrayType) {
			ArrayType o = (ArrayType) other;
			//TODO: should we really be checking for length?
			if(o.length!=length) {return false;}
			return (type.CheckIfTypesAreEqualThisFunctionHasALongName(o.type));
		}
		return false;
	}
}
