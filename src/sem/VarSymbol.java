package sem;

import ast.*;

public class VarSymbol extends Symbol {

	VarDecl variable;

	public VarSymbol(VarDecl v) {
		super(v.varName);
		variable = v;
	}

}
