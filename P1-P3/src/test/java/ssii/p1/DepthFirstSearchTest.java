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

package ssii.p1;

import java.util.HashSet;
import java.util.LinkedHashSet;
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
import ssii.p1.state.VacuumState;
import aima.core.agent.Action;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.GoalTest;
import aima.core.search.framework.Problem;
import aima.core.search.framework.QueueSearch;
import aima.core.search.framework.ResultFunction;
import aima.core.search.framework.TreeSearch;
import aima.core.search.uninformed.DepthFirstSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;



public class DepthFirstSearchTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DepthFirstSearchTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( DepthFirstSearchTest.class );
    }
  
    public List<Action> getActions(int aid, VacuumState initialState){
    	QueueSearch qs=new TreeSearch();
    	DepthFirstSearch dfs=new DepthFirstSearch(qs);
		ActionsFunction actionsFunction = new ActionsFunction(){			
			public Set<Action> actions(Object state) {				
				VacuumState vs=(VacuumState)state;
				HashSet<Action> possibleActions=new HashSet<Action>();
				Vector<OOAction> candidates=new Vector<OOAction>();
				candidates.add(new Left(1));
				candidates.add(new Right(1));
				candidates.add(new Up(1));
				candidates.add(new Down(1));
				candidates.add(new Suck(1));
				candidates.add(new Noop(1));				
				for (OOAction ooa:candidates)
					if (ooa.valid(vs))
						possibleActions.add(ooa);
				return possibleActions;
			}			
		};
		
		ResultFunction resultFunction = new ResultFunction(){
			public Object result(Object state, Action a) {		
				VacuumState vs=(VacuumState)state;
				OOAction oa=(OOAction)a;
				return oa.perform(vs);
			}
			
		};
		
		GoalTest goalTest = new GoalTest(){
			public boolean isGoalState(Object state) {
				VacuumState vs=(VacuumState)state;				
				return vs.getGlobalDirtCount()==0;
			}			
		};
		
		
		Problem vacuumProblem=new Problem(initialState,actionsFunction,resultFunction,goalTest);
    	List<Action> actions=null;
		try {
			actions = dfs.search(vacuumProblem);

		} catch (Exception e) {
			e.printStackTrace();
		}
    	return actions;
    }

    public void runApp(int [][] world) {
    	VacuumState initialState = new VacuumState( world);
    	initialState.addAgent(1, new Location(0,0));
    	List<Action> actions = getActions(1,initialState);
    	Object previousState=initialState;
    	for (Action action:actions){
    		OOAction oaction=(OOAction)action;    		
    		previousState= oaction.perform(previousState);
    	}    	
    	org.junit.Assert.assertTrue("There should be no dirt and there is some in this map \n"+previousState+ " after executing "+actions, 
    			((VacuumState)previousState).getGlobalDirtCount() == 0);    	
    }
    
    public void testApp( )
    {
    	runApp( new int[][]{
    		new int[]{0,0,0},
    		new int[]{0,0,0},
    		new int[]{0,5,0}});
    }
        
   

	
}
