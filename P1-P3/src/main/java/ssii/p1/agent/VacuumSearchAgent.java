/*Custom Vacuum World Example 
Copyright (C) 2015  Jorge J. Gomez-Sanz

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */	
package ssii.p1.agent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import ssii.p1.actions.Down;
import ssii.p1.actions.Left;
import ssii.p1.actions.Location;
import ssii.p1.actions.Noop;
import ssii.p1.actions.OOAction;
import ssii.p1.actions.Right;
import ssii.p1.actions.Suck;
import ssii.p1.actions.Up;
import ssii.p1.actions.VacuumActionsFunction;
import ssii.p1.actions.VacuumResultFunction;
import ssii.p1.state.VacuumState;
import aima.core.agent.Action;
import aima.core.agent.Percept;
import aima.core.agent.State;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.GoalTest;
import aima.core.search.framework.Problem;
import aima.core.search.framework.QueueSearch;
import aima.core.search.framework.ResultFunction;
import aima.core.search.framework.SimpleProblemSolvingAgent;
import aima.core.search.framework.TreeSearch;
import aima.core.search.uninformed.BreadthFirstSearch;

public class VacuumSearchAgent extends SimpleProblemSolvingAgent {
	VacuumState ms=new VacuumState(new int[][]{new int[]{0,0,0},new int[]{0,5,0}});
		
	public VacuumSearchAgent() {
		ms.addAgent(1,new Location(0,0));
	}

	public VacuumSearchAgent(int maxGoalsToFormulate) {
		super(maxGoalsToFormulate);		
	}

	@Override
	protected State updateState(Percept p) {
		ObservableVacuumPercept vp=(ObservableVacuumPercept)p;
		this.ms=vp.getState();
		return ms;
	}

	@Override
	protected Object formulateGoal() {
		return "SearchDirt";
	}

	@Override
	protected Problem formulateProblem(Object goal) {	 		 	
		ActionsFunction actionsFunction = 
				new VacuumActionsFunction(this.hashCode());

		ResultFunction resultFunction = new VacuumResultFunction();

		GoalTest goalTest = new VacuumGoalTest();		

		Problem vacuumProblem=new Problem(
				ms,
				actionsFunction,
				resultFunction,
				goalTest);

		return vacuumProblem;
	}

	@Override
	protected List<Action> search(Problem problem) {
		QueueSearch qs=new TreeSearch();
		BreadthFirstSearch bfs=new aima.core.search.uninformed.BreadthFirstSearch(qs);   	    	
		List<Action> actions = bfs.search(problem);    	
		return actions;
	}

	@Override
	protected void notifyViewOfMetrics() {
		// TODO Auto-generated method stub

	}


}
