package ast;

public class PointerType implements Type {

	public Type pointerToType;

	public PointerType(Type p) {
		pointerToType = p;
	}

	public <T> T accept(ASTVisitor<T> v) {
		return v.visitPointerType(this);
	}

	@Override
	public boolean CheckIfTypesAreEqualThisFunctionHasALongName(Type other) {
		
		if(other instanceof PointerType) {
			PointerType o = (PointerType) other;
			return (pointerToType.CheckIfTypesAreEqualThisFunctionHasALongName(o.pointerToType));
		}
		return false;
	}
	

    
    public String toString() {
    	String build = super.toString();
    	build = build+"Pointer("+pointerToType+")";
    	return build;
    	
    }
}
