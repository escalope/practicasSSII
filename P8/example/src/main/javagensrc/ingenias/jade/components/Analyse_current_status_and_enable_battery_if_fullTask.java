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

import java.util.*;
import ingenias.jade.exception.*;
import ingenias.jade.comm.*;
import ingenias.jade.mental.*;
import ingenias.editor.entities.*;

/**
 * 
 * <p>
 * The Task Analyse_current_status_and_enable_battery_if_full has the following
 * inputs, sets of possible outputs, and available applications:
 * </p>
 * Inputs:
 * <ul>
 * <li>_TimeTick1</li>
 * <li>AssociatedUnit</li>
 * <li>OrdersHistory</li>
 * 
 * 
 * 
 * </ul>
 * Expected output:
 * <ul>
 * 
 * </ul>
 * Available apps:
 * <ul>
 * <li>SMClientApp</li>
 * 
 * </ul>
 */
public class Analyse_current_status_and_enable_battery_if_fullTask extends Task {

	public Analyse_current_status_and_enable_battery_if_fullTask(String id) {
		super(id, "Analyse_current_status_and_enable_battery_if_full");

	}

	public void execute() throws TaskException {
		YellowPages yp = null; // only available for initiators of interactions

		_TimeTick1 ei_TimeTick1 = (_TimeTick1) this
				.getFirstInputOfType("_TimeTick1");

		AssociatedUnit eiAssociatedUnit = (AssociatedUnit) this
				.getFirstInputOfType("AssociatedUnit");

		OrdersHistory eiOrdersHistory = (OrdersHistory) this
				.getFirstInputOfType("OrdersHistory");

		SMClientApp eaSMClient = (SMClientApp) this.getApplication("SMClient");

		Vector<TaskOutput> outputs = this.getOutputs();

		// sets the first possible alternative to be the default one
		if (!this.getOutputs().isEmpty())
			this.setFinalOutput(outputs.firstElement());

		// --------------------------------------------------------
		// End of automatically generated code
		// --------------------------------------------------------
		// Expected output summary:

		// Available apps:
		// SMClientApp</li>

		// Summary of alternatives available to this task
		// --------------------------------------------------------

		// Code Area
		// #start_node:ManagementIndividualBatteryCode <--- DO NOT REMOVE THIS
		// REPLACE THIS COMMENT WITH YOUR CODE
		System.out.println(getAgentID() + " executing -> " + getID() + ":"
				+ getType());
		// obtain attached elemetns
		System.out.println(getAgentID() + " executing -> " + getID() + ":"
				+ getType());
		// y otro cambio
		Hashtable<String, mired.ucm.grid.TypesElement> devices;
		try {
			String device = eiAssociatedUnit.getdevice();
			System.out.println("querying " + device);
			mired.ucm.remote.orders.RemoteBatteryOrder batteryCharge = new mired.ucm.remote.orders.RemoteBatteryOrder(
					"Battery_31", 10000);
			mired.ucm.remote.orders.RemoteBatteryOrder batteryDischarge = new mired.ucm.remote.orders.RemoteBatteryOrder(
					"Battery_31", 0);

			if (eaSMClient.getBatteryEnergy("Battery_31") <= 20000
					&& new Date(eaSMClient.getSimTime()).getHours() > 0
					&& new Date(eaSMClient.getSimTime()).getHours() <= 7
					&& mired.ucm.Utils.noOrderXAfterLastY(
							eiOrdersHistory.getHistory(), batteryDischarge,
							batteryCharge)
					&& (eiOrdersHistory.getHistory().isEmpty() || !eiOrdersHistory
							.getHistory().lastElement().getSecond()
							.equals(batteryCharge))) {
				System.out.println(getAgentID() + " charging");

				// Charges the battery
				eaSMClient.sendOrder(batteryCharge);
				eiOrdersHistory
						.getHistory()
						.add(new mired.ucm.Pair<Long, mired.ucm.remote.orders.RemoteOrder>(
								eaSMClient.getSimTime(), batteryCharge));
				System.out.println("sending switch on order to " + device);

			} else {
				if (eaSMClient.getBatteryEnergy("Battery_31") > 20000
						&& new Date(eaSMClient.getSimTime()).getHours() > 14
						&& new Date(eaSMClient.getSimTime()).getHours() < 18
						&& (!eiOrdersHistory.getHistory().isEmpty() || eiOrdersHistory
								.getHistory().lastElement().getSecond()
								.equals(batteryDischarge))) {
					System.out.println(getAgentID() + " charging");
					// Charges the battery
					eaSMClient.sendOrder(batteryDischarge);
					eiOrdersHistory
							.getHistory()
							.add(new mired.ucm.Pair<Long, mired.ucm.remote.orders.RemoteOrder>(
									eaSMClient.getSimTime(), batteryDischarge));
					System.out.println("sending switch on order to "
							+ "Battery_31");
				}
			}

		} catch (java.rmi.RemoteException e) { // TODO Auto-generated catch
			// block
			e.printStackTrace();
		}
		eaSMClient.tryItAgainAfter(10);

		// #end_node:ManagementIndividualBatteryCode <--- DO NOT REMOVE THIS

	}

}
