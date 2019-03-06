package ssii.p4.wedding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.Constraint;
import aima.core.search.csp.Variable;

/**
 * Represents a binary constraint which forbids equal values.
 * 
 * @author Ruediger Lunde
 */
public class PairsInTableOrNone implements Constraint, Serializable {

	private Variable var1;
	private Variable var2;
	private List<Variable> scope;
	private String p1;
	private String p2;

	public PairsInTableOrNone(String p1,String p2, List<Variable> chairs) {

		scope = new ArrayList<Variable>(chairs);

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
		boolean assignedtablev1=false;
		boolean assignedtablev2=false;
		boolean someunassigned=false;


		for (Variable v1:getScope()) {
			if (assignment.getAssignment(v1)==null)
				someunassigned=true;
			if (assignment.getAssignment(v1)!=null && assignment.getAssignment(v1).equals(p1)) {
				assignedtablev1=true;																
			} else
				if (assignment.getAssignment(v1)!=null && assignment.getAssignment(v1).equals(p2)) {
					assignedtablev2=true;																
				} 
		}


		boolean result=(assignedtablev2 && someunassigned) ||
				(assignedtablev1 && someunassigned) ||
				(!assignedtablev1 && !assignedtablev2) 
				|| (assignedtablev1&& assignedtablev2);

		return result;
	}
}
