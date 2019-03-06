package ssii.p3;

import java.util.List;

import aima.core.agent.Action;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.GoalTest;
import aima.core.search.framework.Problem;
import aima.core.search.framework.QueueSearch;
import aima.core.search.framework.ResultFunction;
import aima.core.search.framework.TreeSearch;
import aima.core.search.local.Scheduler;
import ssii.p1.Utils;
import ssii.p1.actions.Location;
import ssii.p1.actions.VacuumActionsFunction;
import ssii.p1.actions.VacuumResultFunction;
import ssii.p1.agent.VacuumGoalTest;
import ssii.p1.state.VacuumState;

public class EjemploSimulatedAnnealing {

    public List<Action> getActions(int aid, VacuumState initialState) throws Exception{
    	QueueSearch qs=new TreeSearch();
    	aima.core.search.framework.HeuristicFunction hf=new 
    			aima.core.search.framework.HeuristicFunction(){
					@Override
					public double h(Object state) {
						VacuumState vs=(VacuumState)state;						
						return (vs.getGlobalDirtCount());
					}
    		
    	};
    	
    	// determines how the temperature is going to decrease
    	Scheduler myscheduler=new Scheduler(){ 
    		int limit=10; // 10 steps to cool down
    		@Override
    		public double getTemp(int t) {
    			if (t < this.limit) {    				
    				return limit-t;
    			} else {
    				return 0.0;
    			}
    		}    		
    	};
    	
    	aima.core.search.local.SimulatedAnnealingSearch hcs=
    			new aima.core.search.local.SimulatedAnnealingSearch(hf,myscheduler );   	    	
   		
		ActionsFunction actionsFunction = new VacuumActionsFunction(aid);
		
		ResultFunction resultFunction = new VacuumResultFunction();
		
		GoalTest goalTest = new VacuumGoalTest();
		
		Problem vacuumProblem=new Problem(initialState,actionsFunction,resultFunction,goalTest);
    	List<Action> actions = hcs.search(vacuumProblem);    
    	
    	return actions;
    }
    
    public static void main(String args[]) throws Exception{
    	EjemploSimulatedAnnealing dfst=new EjemploSimulatedAnnealing();
    	int[][] world=new int[][]{
    			new int[]{0,0,0},
    			new int[]{0,0,0},
    			new int[]{0,5,0}};
    	    	
    	VacuumState initialState = new VacuumState(world);
    	initialState.addAgent(1, new Location(0,0));
    	List<Action> actions = dfst.getActions(1,initialState);
    	System.out.println("solution:" + actions);       	
    	Utils.animate(actions,initialState);
    	
    }
}
