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

package ssii.p1.state;

import ssii.p1.actions.Location;

import java.util.HashSet;
import java.util.Hashtable;

import aima.core.agent.EnvironmentState;
import aima.core.agent.State;
import aima.core.search.framework.Problem;


public class VacuumState implements EnvironmentState, State{
	int cells[][];	
	Hashtable<Integer,Location> vacLoc=new Hashtable<Integer,Location>();
	final static int WIDTH=10;
	final static int HEIGHT=10;

	public VacuumState(int [][] world){
		
		this.cells=world;
	}
	
	
	public void addAgent(int aid, Location loc) {
		vacLoc.put(aid, loc);
	}

	public VacuumState(Hashtable<Integer,Location>  locations, int [][] world){		
		this.vacLoc=locations;
		this.cells=world;
	}

	public VacuumState clone(){
		int ncells[][]=new int[cells.length][cells[0].length];
		for (int x=0;x<cells.length;x++)	
			for (int y=0;y<cells[0].length;y++) 
				ncells[x][y]=cells[x][y];
		
		Hashtable<Integer,Location> nht=new Hashtable<Integer,Location>(vacLoc);
		for (Integer i:nht.keySet()) {
			nht.put(i, nht.get(i).clone());
		}
		return new VacuumState(nht, ncells);

	}

	public int getGlobalDirtCount(){
		int dcount=0;
		for (int x=0;x<cells.length;x++)	
			for (int y=0;y<cells[0].length;y++)
				dcount=dcount+cells[x][y];
		return dcount;
	}


	public Location getLocation(int agent){
		return vacLoc.get(agent);
	}
	
	public boolean noAgentsSameLocation(Location loc) {
		
		boolean someagent=false;
		for (Location agentloc:vacLoc.values()) {
			someagent=someagent || agentloc.equals(loc);
		}
		return !someagent;
	}


	public int getDirt(int agent){				
		return cells[vacLoc.get(agent).getX()][vacLoc.get(agent).getY()];
	}

	public void moveRight(int agent){
		vacLoc.get(agent).setX(Math.min(cells.length-1, vacLoc.get(agent).getX()+1));
	}

	public void moveLeft(int agent){
		vacLoc.get(agent).setX(Math.max(0, vacLoc.get(agent).getX()-1));
	}

	public void moveUp(int agent){
		vacLoc.get(agent).setY(Math.max(0, vacLoc.get(agent).getY()-1));
	}

	public void moveDown(int agent){
		vacLoc.get(agent).setY(Math.min(cells[0].length-1,vacLoc.get(agent).getY()+1));
	}
	
	public int getWidth(){
		return cells.length;
	}
	
	public int getHeight(){
		return cells[0].length;
	}

	public void suck(int aid){
		cells[vacLoc.get(aid).getX()][vacLoc.get(aid).getY()]=0;
	}
	public String toString(){
		String map="";
		for (int y=0;y<cells[0].length;y++){
			String row="";
			int printed=0;
			for (int x=0;x<cells.length;x++){
				boolean found=false;
				for (int agent:vacLoc.keySet()) {
				 if (x==vacLoc.get(agent).getX() 
						 && y==vacLoc.get(agent).getY()) {
					 row=row+"[*|"+cells[x][y]+"]";
					 found=true;
					 printed++;
				 }
				}	
				if (!found) {
				row=row+"[_|"+cells[x][y]+"]";
				printed++;
				}
				if (printed==6) {
					int k=0;
				}
			}				
			

			map=map+row+"\n";
		}


		return map;
	}


	public static void main(String args[]){
		
		VacuumState vs=new VacuumState(
				new int[][]{
			new int[]{0,0,0},
			new int[]{0,0,0},
			new int[]{0,5,0}});
		vs.addAgent(1, new Location(0,0));
		vs.getLocation(1).setX(2);
	
		VacuumState v1=vs.clone();
		v1.getLocation(1).setX(1);

	}

	public void addDirt() {
		int x=(int)(Math.random()*cells.length);
		int y=(int)(Math.random()*cells[0].length);
		cells[x][y]=5;
		
	}

	public int getDirtAt(int x, int y) {
		return cells[x][y];
	}


	public boolean agentOnEveryDirtExceptMe(int aid) {
		boolean agentonalldirt=true;
		for (int x=0;x<cells.length && agentonalldirt;x++)	
			for (int y=0;y<cells[0].length && agentonalldirt;y++) {
				if (cells[x][y]>0) {
					boolean someAgent=false;
				 	for (int caid:vacLoc.keySet()) {
				 		if (caid!=aid)
				 		someAgent=someAgent|| vacLoc.get(caid).getX()==x && vacLoc.get(caid).getY()==y;
				 	}
				 	agentonalldirt=agentonalldirt && someAgent;
				}
			}
		return agentonalldirt;
	}
		

}
