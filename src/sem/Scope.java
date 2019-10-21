package sem;

import java.util.Map;
import java.util.HashMap;

public class Scope {
	private Scope outer;
	private Map<String, Symbol> symbolTable;

	public Scope(Scope outer) {
		this.outer = outer;
		symbolTable = new HashMap<String,Symbol>();
	}

	public Scope() {
		this(null);
	}

	public Symbol lookup(String name) {
		if(lookupCurrent(name) != null) {return lookupCurrent(name);}
		if(outer!=null) {return outer.lookup(name);}
		return null;
	}

	public Symbol lookupCurrent(String name) {
		if (symbolTable.containsKey(name)) {
			return symbolTable.get(name);
		}
		return null;
	}

	public void put(Symbol sym, BaseSemanticVisitor b) {
		if (lookupCurrent(sym.name) != null) {
			b.error("Declared symbol with same name as symbol declared in the same scope;"+sym.name);
		}
		symbolTable.put(sym.name, sym);
	}
}
