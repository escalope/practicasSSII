/*
	This file is part of SGSim framework, a simulator for distributed smart electrical grid.

    Copyright (C) 2014 N. Cuartero-Soler, S. Garcia-Rodriguez, J.J Gomez-Sanz

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
package mired.ucm.examples.clients;

import java.rmi.RemoteException;

import mired.ucm.remote.client.SGClient;
import mired.ucm.remote.orders.RemoteSwitchOff;
import mired.ucm.remote.orders.RemoteSwitchOn;
import mired.ucm.remote.server.RemoteSGServer;
import mired.ucm.simulator.SensorContainerNotFound;
import mired.ucm.simulator.SensorIDS;

public class SampleRunClientImp {
	public static void main(String args[]) throws Exception {
		RunClient.launchAndWaitForServerShutdown(new SGClient() {

			boolean finished = false;

			@Override
			public String getName() throws RemoteException {
				return "transformerCT1";
			}

			@Override
			public void serverStopped() throws RemoteException {
				// invoked when the server has stopped
			}

			@Override
			public void serverStarted(RemoteSGServer server)
					throws RemoteException {

				try {
					// we print the substation kW demand
					System.out.println(server.getSubstationSensors().get(
							SensorIDS.MEASURED_ENERGY));
					// we print the kW at the transformation centre
					System.out.println(server.getTransformerSensors(
							this.getName()).get(SensorIDS.MEASURED_ENERGY));
					System.out.println(server.getControllableDevices(this
							.getName()));
					server.executeOrder(this.getName(), new RemoteSwitchOn(
							"Solar_11"));
					server.executeOrder(this.getName(), new RemoteSwitchOn(
							"Solar_311"));
					server.executeOrder(this.getName(), new RemoteSwitchOn(
							"Solar_221"));
					server.executeOrder(this.getName(), new RemoteSwitchOn(
							"Solar_222"));
					server.executeOrder(this.getName(), new RemoteSwitchOn(
							"Solar_21"));
					server.executeOrder(this.getName(), new RemoteSwitchOn(
							"Solar_312"));

					try {
						Thread.currentThread().sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					finished = true;
				} catch (SensorContainerNotFound e) {
					e.printStackTrace();
				}
			}

			@Override
			public boolean isFinished() {
				return finished;
			}
		});
	}
}
