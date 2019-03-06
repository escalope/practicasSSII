package ssii.p4.wedding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.Constraint;
import aima.core.search.csp.Variable;

public class PairsNotSameTable implements Constraint, Serializable {

	private Variable var1;
	private Variable var2;
	private List<Variable> scope;
	private Hashtable<String, List<Variable>> tables;
	private String p1;
	private String p2;

	public PairsNotSameTable(String p1,String p2, Hashtable<String, List<Variable>> tables) {
		this.tables=tables;
		scope = new ArrayList<Variable>();
		for (List<Variable> al:tables.values())
			for (Variable v:al)
				scope.add(v);
		this.p1=p1;
		this.p2=p2;
	}

	@Override
	public List<Variable> getScope() {
		return scope;
	}

	@Override
	public boolean isSatisfiedWith(Assignment assignment) {
		boolean allnull=true;
		String assignedtablev1=null;
		String assignedtablev2=null;
		
		for (String table:tables.keySet()) {
			List<Variable> al=tables.get(table);
			for (Variable v1:al) {
				if (assignment.getAssignment(v1)!=null && assignment.getAssignment(v1).equals(p1)) {
					assignedtablev1=table;																
				} else
					if (assignment.getAssignment(v1)!=null && assignment.getAssignment(v1).equals(p2)) {
						assignedtablev2=table;																
					} 
			}
		}
		
		return assignedtablev1==null || 
				assignedtablev2==null || 
				!assignedtablev1.equals(assignedtablev2);
	}

}
