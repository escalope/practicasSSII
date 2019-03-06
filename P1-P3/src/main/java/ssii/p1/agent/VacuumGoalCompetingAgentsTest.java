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

import ssii.p1.state.VacuumState;
import aima.core.search.framework.GoalTest;

/**
 * It hangs because, if there is one dirt and the other agent has reached
 * the dirt, the first agent will never achieve the goal of zero-dirt: there is
 * dirt that is unreachable because the other agent is preventing the first agent
 * to reach that cell. 
 * @author superjj
 *
 */
public class VacuumGoalCompetingAgentsTest implements GoalTest {

	private int aid;
	public VacuumGoalCompetingAgentsTest(int aid) {
		this.aid=aid;
	}
	public boolean isGoalState(Object state) {
		VacuumState vs=(VacuumState)state;				
		return vs.getGlobalDirtCount()==0 || vs.agentOnEveryDirtExceptMe(aid);
	}

}
