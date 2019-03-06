package ssii.p2;

import ssii.p1.actions.Location;
import ssii.p1.world.VacuumWorld;

/**
 * It hangs because, if there is one dirt and the other agent has reached
 * the dirt, the first agent will never achieve the goal of zero-dirt: there is
 * dirt that is unreachable because the other agent is preventing the first agent
 * to reach that cell. 
 * @author superjj
 *
 */
public class CompetingRoomba {
	public static void main(String args[]) throws InterruptedException {
		int[][] world=new int[][]{
				new int[]{0,0,0,0,0},
				new int[]{0,0,0,0,0},
				new int[]{0,0,5,0,0},
				new int[]{0,0,0,0,0},
				new int[]{0,0,0,0,0}};
	
		ssii.p1.world.VacuumWorld env = new VacuumWorld(world);
		ssii.p2.agent.VacuumSearchAgent vsA1 = new ssii.p2.agent.VacuumSearchAgent();		
		ssii.p2.agent.VacuumSearchAgent vsA2 = new ssii.p2.agent.VacuumSearchAgent();
		
		env.addAgent(new Location(2, 1),vsA1);
		env.addAgent(new Location(0, 4),vsA2);
		
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
