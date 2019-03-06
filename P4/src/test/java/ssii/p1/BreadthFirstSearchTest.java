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
import ssii.p1.agent.VacuumGoalTest;
import ssii.p1.state.VacuumState;
import aima.core.agent.Action;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.GoalTest;
import aima.core.search.framework.Problem;
import aima.core.search.framework.QueueSearch;
import aima.core.search.framework.ResultFunction;
import aima.core.search.framework.TreeSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthFirstSearch;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;




public class BreadthFirstSearchTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public BreadthFirstSearchTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( BreadthFirstSearchTest.class );
    }
    
 
    public List<Action> getActions(int aid, VacuumState initialState){
    	QueueSearch qs=new TreeSearch();
    	BreadthFirstSearch bfs=new aima.core.search.uninformed.BreadthFirstSearch(qs);   	    	
   		
		ActionsFunction actionsFunction = new VacuumActionsFunction(aid);
		
		ResultFunction resultFunction = new VacuumResultFunction();
		
		GoalTest goalTest = new VacuumGoalTest();
		
		Problem vacuumProblem=new Problem(initialState,actionsFunction,resultFunction,goalTest);
    	List<Action> actions = bfs.search(vacuumProblem);    
    	return actions;
    }
  
    
    public void runApp(int [] [] world) {
    	VacuumState initialState = new VacuumState(
    			world); 			
    	initialState.addAgent(1, new Location(0,0));
    	List<Action> actions = getActions(1,initialState);    	
    	Object previousState=initialState;
    	for (Action action:actions){
    		OOAction oaction=(OOAction)action;    		
    		previousState= oaction.perform(previousState);
    	}    	
    	org.junit.Assert.assertTrue("There should be no "
    			+ "dirt and there is some in this map \n"+
    			previousState+ " after executing "+
    			actions, ((VacuumState)previousState).getGlobalDirtCount()==0);
    }
    
    
    public void testApp( )
    {
            	runApp(new int[][]{
        			new int[]{0,0,0},
        			new int[]{0,5,0}});
    }
       

	
}
