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
package ssii.p1.world;

import java.util.HashSet;
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
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractEnvironment;

public class VacuumWorld extends AbstractEnvironment {

	private VacuumState currentState;

	public VacuumWorld( int [][] world) {
		currentState = new VacuumState(world);
	}

	@Override
	public EnvironmentState getCurrentState() {
		return currentState;
	}

	@Override
	public void addAgent(Agent a) {
		throw new RuntimeException("Unsupported");
	}
	
	public void addAgent(Location loc, Agent a) {
		// TODO Auto-generated method stub
		this.currentState.addAgent(a.hashCode(), loc);
		super.addAgent(a);
	}

	@Override
	public EnvironmentState executeAction(Agent agent, Action action) {
		if (action instanceof OOAction && ((OOAction)action).valid(this.currentState)){
			OOAction oa=(OOAction) action;
			this.currentState=(VacuumState) oa.perform(currentState);
		} 
		return currentState;

	}

	@Override
	public Percept getPerceptSeenBy(Agent anAgent) {
		return new ssii.p1.agent.ObservableVacuumPercept(
				currentState);
	}
		

}
