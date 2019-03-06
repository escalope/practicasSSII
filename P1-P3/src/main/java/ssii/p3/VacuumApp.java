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
package ssii.p3;

import ssii.p1.actions.Location;
import ssii.p1.state.VacuumState;
import ssii.p1.world.VacuumWorld;

public class VacuumApp {


	public static void main(String args[]) throws InterruptedException {
		int[][] world=new int[][]{
    			new int[]{0,0,0},
    			new int[]{0,0,0},
    			new int[]{0,5,0}};
	
		ssii.p1.world.VacuumWorld env = new VacuumWorld(world);
		ssii.p3.agent.VacuumSearchAgent vsa = new ssii.p3.agent.VacuumSearchAgent();
		env.addAgent(new Location(0,0),vsa);
		while (true) {
			env.step();
			System.out.println(env.getCurrentState());			
			Thread.currentThread().sleep(2000);
			if (((ssii.p1.state.VacuumState) env.getCurrentState())
					.getGlobalDirtCount() == 0) {
				((ssii.p1.state.VacuumState) env.getCurrentState()).addDirt();

			}
		}
	}

}
