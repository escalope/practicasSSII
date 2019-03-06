package ssii.p1;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import aima.core.agent.Action;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.GoalTest;
import aima.core.search.framework.Problem;
import aima.core.search.framework.QueueSearch;
import aima.core.search.framework.ResultFunction;
import aima.core.search.framework.TreeSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthFirstSearch;
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
import ssii.p1.agent.VacuumGoalTest;
import ssii.p1.state.VacuumState;

public class ExampleDFS {


	public List<Action> getActions(int aid, VacuumState initialState){

		QueueSearch qs=new TreeSearch();		

		DepthFirstSearch bfs=new aima.core.search.uninformed.DepthFirstSearch(qs);   	    	

		ActionsFunction actionsFunction = new VacuumActionsFunction(aid);

		ResultFunction resultFunction = new VacuumResultFunction();

		GoalTest goalTest = new VacuumGoalTest();

		Problem vacuumProblem=new Problem(initialState,actionsFunction,resultFunction,goalTest);

		List<Action> actions = bfs.search(vacuumProblem);    

		return actions;    	
	}

	public static void main(String args[]){
		ExampleDFS dfst=new ExampleDFS();
		int[][] world=new int[][]{
			new int[]{0,0,0},
			new int[]{0,0,0},
			new int[]{0,5,0}};
			VacuumState initialState = new VacuumState( 
					world);
			initialState.addAgent(1, new Location(0,0));
			List<Action> actions = dfst.getActions(1,initialState);
			System.out.println("solution:" + actions);      
			Utils.animate(actions,initialState);

	}
}
