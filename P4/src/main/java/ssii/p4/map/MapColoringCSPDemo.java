package ssii.p4.map;

import aima.core.search.csp.BacktrackingStrategy;
import aima.core.search.csp.CSP;
import aima.core.search.csp.ImprovedBacktrackingStrategy;
import aima.core.search.csp.MinConflictsStrategy;
import aima.core.search.csp.SolutionStrategy;
import ssii.p4.StepCounter;

/**
 * Demonstrates the performance of different constraint solving strategies.
 * The map coloring problem from the textbook is used as CSP.
 * 
 * @author Ruediger Lunde
 */

public class MapColoringCSPDemo {
	public static void main(String[] args) {
		CSP csp = new MapCSP();
		StepCounter stepCounter = new StepCounter();
		SolutionStrategy solver;
		
		solver = new MinConflictsStrategy(1000);
		solver.addCSPStateListener(stepCounter);
		stepCounter.reset();
		System.out.println("Map Coloring (Minimum Conflicts)");
		System.out.println(solver.solve(csp.copyDomains()));
		System.out.println(stepCounter.getResults() + "\n");
		
		solver = new ImprovedBacktrackingStrategy(true, true, true, true);
		solver.addCSPStateListener(stepCounter);
		stepCounter.reset();
		System.out.println("Map Coloring (Backtracking + MRV + DEG + AC3 + LCV)");
		System.out.println(solver.solve(csp.copyDomains()));
		System.out.println(stepCounter.getResults() + "\n");
		
		solver = new BacktrackingStrategy();
		solver.addCSPStateListener(stepCounter);
		stepCounter.reset();
		System.out.println("Map Coloring (Backtracking)");
		System.out.println(solver.solve(csp.copyDomains()));
		System.out.println(stepCounter.getResults() + "\n");
	}
}
