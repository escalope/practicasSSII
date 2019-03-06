package ssii.p4.wedding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.BacktrackingStrategy;
import aima.core.search.csp.CSP;
import aima.core.search.csp.CSPStateListener;
import aima.core.search.csp.Constraint;
import aima.core.search.csp.Domain;
import aima.core.search.csp.ImprovedBacktrackingStrategy;
import aima.core.search.csp.MinConflictsStrategy;
import aima.core.search.csp.NotEqualConstraint;
import aima.core.search.csp.SolutionStrategy;
import aima.core.search.csp.Variable;
import ssii.p4.StepCounter;



/**
 * Artificial Intelligence A Modern Approach (3rd Ed.): Figure 6.1, Page 204.<br>
 * <br>
 * The principal states and territories of Australia. Coloring this map can be
 * viewed as a constraint satisfaction problem (CSP). The goal is to assign
 * colors to each region so that no neighboring regions have the same color.
 * 
 * @author Ruediger Lunde
 * @author Mike Stampone
 */
public class  WeddingCSP extends CSP implements Serializable {
	Hashtable<String, List<Variable>> tables= new Hashtable<String, List<Variable>>();
	public List<Variable> chairs = new ArrayList<Variable>();


	// it is not defined for n-ary constraints
	@Override
	public Variable getNeighbor(Variable var, Constraint constraint) {
		List<Variable> scope = constraint.getScope();
		Variable selectedVar=null;
		if (scope.size() == 2) {
			if (var == scope.get(0))
				selectedVar= scope.get(1);
			else if (var == scope.get(1))
				selectedVar= scope.get(0);
		} else  {
			// selects another right sibling. Starts again from 0 if
			// reaching the end of the scope.
			int k=0;
			while (selectedVar==null && k<scope.size()) {
				Variable cvar=scope.get(k);
				if (cvar==var && k<scope.size()-1) {
					selectedVar=scope.get(k+1);			 	 
				} else
					if (cvar==var && k==scope.size()-1)
						selectedVar=scope.get(0);
					else
						k=k+1;
			}
		}
		return selectedVar;
	}


	private void collectVariables() {
		ArrayList<Variable> table1=new ArrayList<Variable>();
		table1.add(new Variable("chair1-1"));
		table1.add(new Variable("chair2-1"));
		table1.add(new Variable("chair3-1"));
		table1.add(new Variable("chair4-1"));
		table1.add(new Variable("chair5-1"));

		ArrayList<Variable> table2=new ArrayList<Variable>();
		table2.add(new Variable("chair1-2"));
		table2.add(new Variable("chair2-2"));
		table2.add(new Variable("chair3-2"));
		table2.add(new Variable("chair4-2"));
		table2.add(new Variable("chair5-2"));

		ArrayList<Variable> table3=new ArrayList<Variable>();
		table3.add(new Variable("chair1-3"));
		table3.add(new Variable("chair2-3"));
		table3.add(new Variable("chair3-3"));
		table3.add(new Variable("chair4-3"));
		table3.add(new Variable("chair5-3"));

		tables.put("table1", table1);
		tables.put("table2", table2);
		tables.put("table3", table3);
		for (List<Variable> al:tables.values())
			for (Variable v:al)
				addVariable(v);
	}
	
	public String prettyPrintAssignment(Assignment assignment) {
		if (assignment!=null) {
			String result="";
			for (String table:this.tables.keySet()) {
				List<Variable> chairs = tables.get(table);
				result=result+("Table "+table+"\n");
				for (Variable var:chairs) {
					if (assignment.hasAssignmentFor(var))
						result=result+(var.getName()+"="+assignment.getAssignment(var)+",");
					else
						result=result+(var.getName()+"= null"+",");
				}
				result=result+"\n";
			}
			return result;
		}
		return "not found";

	}	
	
	protected static class CounterThred extends Thread {
		boolean finish=false;
		private StepCounter sc;
		
		CounterThred(StepCounter sc){
			this.sc=sc;
		}
		public void finish() {
			finish=true;
		}
		public void run() {
			while (!finish) {
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(sc.getResults() + "\n");
			}
		}
	}
	


	/**
	 * Defines the wedding tables
	 */
	public WeddingCSP() {
		collectVariables();

		Domain people = new Domain(new Object[] { 
				"m1", "m2","m3","m4","m5","m6","m7",
				"h1","h2", "h3","h4","h5","h6","h7","h8"});

		List<Variable> vars = getVariables();
		for (Variable var : vars)
			setDomain(var, people);

		// every value can be assigned only once
		for (int k=0;k<vars.size();k++) {
			for (int j=k+1;j<vars.size();j++)
				addConstraint(new NotEqualConstraint(vars.get(k), vars.get(j)));
		}

		addConstraint(new PairsSameTable("m7","h7",tables));
		addConstraint(new PairsSameTable("m2","h2",tables));
		addConstraint(new PairsSameTable("m3","h3",tables));
		addConstraint(new PairsSameTable("m4","h4",tables));
		addConstraint(new PairsSameTable("m5","h5",tables));

		addConstraint(new PairsNotSameTable("h1","h2",tables));
		addConstraint(new PairsNotSameTable("m2","m3",tables));
		addConstraint(new PairsNotSameTable("m2","m4",tables));
		addConstraint(new PairsNotSameTable("m2","m5",tables));
	}

	
	
	
	public static void main(String[] args) {
		WeddingCSP csp = new WeddingCSP();
		StepCounter stepCounter = new StepCounter();
		SolutionStrategy solver;
		Date startTime=null;
		
		startTime=new Date();
		solver = new MinConflictsStrategy(1000);
		solver.addCSPStateListener(stepCounter);
		stepCounter.reset();
		System.out.println("Map Coloring (Minimum Conflicts)");
		System.out.println(csp.prettyPrintAssignment(solver.solve(csp.copyDomains())));
		System.out.println(stepCounter.getResults() + " in "+((new Date().getTime()-startTime.getTime())/(1000f*60f))+" minutes \n");
			
		// to track the progress of the computation
		startTime=new Date();
		CounterThred ct=new CounterThred(stepCounter);
		ct.start();
		solver = new ImprovedBacktrackingStrategy(true, true, true, true);
		solver.addCSPStateListener(stepCounter);
		stepCounter.reset();
		System.out.println("Map Coloring (Backtracking + MRV + DEG + AC3 + LCV)");
		// a copydomains cannot be done in this case because of the cloning operation. 
		// It cannot be serialized
		System.out.println(csp.prettyPrintAssignment(solver.solve(csp)));
		ct.finish();
		System.out.println(stepCounter.getResults() + " in "+((new Date().getTime()-startTime.getTime())/(1000f*60f))+" minutes \n");

	} 
}