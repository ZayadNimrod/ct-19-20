package sem;

import ast.*;

public class FunSymbol extends Symbol {

	FunDecl function;

	public FunSymbol(FunDecl f) {
		super(f.name);
		function = f;
	}

}
