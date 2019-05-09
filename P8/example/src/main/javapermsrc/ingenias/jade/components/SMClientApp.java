/*
 Copyright (C) 2005 Jorge Gomez Sanz

 This file is part of INGENIAS Agent Framework, an agent infrastructure linked
 to the INGENIAS Development Kit, and availabe at http://grasia.fdi.ucm.es/ingenias or
 http://ingenias.sourceforge.net. 

 INGENIAS Agent Framework is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 INGENIAS Agent Framework is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with INGENIAS Agent Framework; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 */

package ingenias.jade.components;

import java.rmi.RemoteException;
import java.util.*;

import mired.ucm.grid.TypesElement;
import mired.ucm.remote.orders.RemoteOrder;
import mired.ucm.simulator.SensorIDS;
import ingenias.jade.exception.*;
import ingenias.jade.mental.*;
import ingenias.jade.components.*;
import ingenias.jade.smachines.*;

public abstract class SMClientApp extends Application {

	public SMClientApp() {
		super();
	}

	public abstract void sendOrder(RemoteOrder order) throws RemoteException;

	public abstract Hashtable<SensorIDS, Float> getSubstationSensors()
			throws RemoteException;

	public abstract Hashtable<SensorIDS, Float> getTransformationCentreSensors()
			throws RemoteException;

	public abstract void tryItAgainAfter(long seconds);

	public abstract boolean isReady();

	public abstract double getBatteryEnergy(String batName)
			throws RemoteException;

	public abstract Hashtable<String, TypesElement> getControllableDevices()
			throws RemoteException;

	public abstract Long getSimTime() throws RemoteException;

}
