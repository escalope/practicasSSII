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
package ssii.p1.actions;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Vector;

import ssii.p1.state.VacuumState;
import aima.core.agent.Action;
import aima.core.search.framework.ActionsFunction;

public class VacuumActionsFunction implements ActionsFunction {
	private int aid;
	
	public VacuumActionsFunction(int aid) {this.aid=aid;}
	
	public Set<Action> actions(Object state) {				
		VacuumState vs=(VacuumState)state;
		HashSet<Action> possibleActions=new LinkedHashSet<Action>();
		Vector<OOAction> candidates=new Vector<OOAction>();
		candidates.add(new Left(aid));
		candidates.add(new Right(aid));
		candidates.add(new Up(aid));
		candidates.add(new Down(aid));
		candidates.add(new Suck(aid));
		candidates.add(new Noop(aid));				
		for (OOAction ooa:candidates)
			if (ooa.valid(vs))
				possibleActions.add(ooa);
		return possibleActions;
	}			

}
