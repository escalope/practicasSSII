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
package mired.ucm.localexamples;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import mired.ucm.examples.server.ManageServer;
import mired.ucm.examples.server.TariffExample;
import mired.ucm.monitor.Event;
import mired.ucm.price.Tariff;
import mired.ucm.remote.server.SGServer;
import mired.ucm.simulator.GridlabException;
import mired.ucm.simulator.NetworkListener;
import mired.ucm.simulator.SimulatorNotInitializedException;
import mired.ucm.simulator.clients.Client;
import mired.ucm.simulator.orders.Order;

import org.xml.sax.SAXException;

public class LaunchLocalSimulation {
	public static LocalSimulation setupServer() {
		LocalSimulation server = null;

		Tariff tarrif = new TariffExample();
		Calendar calendar = Calendar.getInstance();
		calendar.set(2014, 4, 15, 6, 0, 0); // Set the start time of the
		// simulation
		// simulation configuration files
		String configFile = "src/main/resources/griddef/configuration.glm"; // tech
		// params
		// of
		// grid
		// elements
		String gridFile = "src/main/resources/griddef/grid.xml"; // grid
		// definition
		String scenarioFile = "src/main/resources/griddef/scenario.csv"; // weather
		// and
		// load
		// per
		// hour
		try {
			server = new LocalSimulation("My simulation", calendar.getTime(),
					gridFile, configFile, scenarioFile,
					ManageServer.hoursOfSimulation,
					ManageServer.cycleTimeInMilliseconds,
					ManageServer.momentsToShow, tarrif, true); // The last
			// parameter
			// indicates
			// clients will
			// not be binded
			// to the
			// simulation
			// cycle

		} catch (Throwable t) {
			t.printStackTrace();
		}
		return server;
	}

	public static void main(String args[])
			throws SimulatorNotInitializedException {
		LocalSimulation ls = setupServer();
		ls.addClient(new Client() {

			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean registerListener(NetworkListener listener) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean unregisterListener(NetworkListener listener) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Vector<NetworkListener> getListeners() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Vector<Order> runCycle(Calendar simtime) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Vector<Order> processEvent(Event event) {
				// TODO Auto-generated method stub
				return null;
			}

		});
	}

}
