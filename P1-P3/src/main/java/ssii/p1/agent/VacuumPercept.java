package ssii.p1.agent;

import ssii.p1.actions.Location;
import aima.core.agent.Percept;

public class VacuumPercept implements Percept {
	private Location loc=null;
	private int dirt=0;
	
	public VacuumPercept(Location loc, int dirt) {		
		this.loc=loc;
		this.dirt=dirt;
	}
	
	public Location getLoc() {
		return loc;
	}
	public int getDirt() {
		return dirt;
	}
	
}
