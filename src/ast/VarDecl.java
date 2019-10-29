package ast;

public class VarDecl implements ASTNode {
    public final Type type;
    public final String varName;
    
    
    //offset to frame pointer or struct in which it is declared
    public int offset=-1;

    public VarDecl(Type type, String varName) {
	    this.type = type;
	    this.varName = varName;
    }

     public <T> T accept(ASTVisitor<T> v) {
	return v.visitVarDecl(this);
    }
}
