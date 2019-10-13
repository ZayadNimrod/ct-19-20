package ast;
import java.util.*;

public class StructTypeDecl implements ASTNode {


    public final StructType structDecl;
    public final List<VarDecl> variables;
    
    public StructTypeDecl(StructType decl, List<VarDecl> vars) {
    	structDecl = decl;
    	variables = vars;
    }	
	
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitStructTypeDecl(this);
    }

}
