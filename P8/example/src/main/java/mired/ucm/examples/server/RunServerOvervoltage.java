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
package mired.ucm.examples.server;

import java.io.IOException;
import java.nio.channels.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import mired.ucm.price.Tariff;
import mired.ucm.remote.orders.RemoteSwitchOff;
import mired.ucm.remote.orders.RemoteSwitchOn;
import mired.ucm.remote.server.SGServer;
import mired.ucm.simulator.GridlabException;
import junit.framework.TestCase;

/**
 * Runs the server
 * 
 * @author Nuria Cuartero-Soler
 * @author Jorge J. Gomez-Sanz
 * 
 */
public class RunServerOvervoltage {

	private static Registry registry;

	public static void setup() throws RemoteException {

		try {
			registry = LocateRegistry.createRegistry(SGServer.getDefaultPort());
		} catch (RemoteException e) {
			registry = LocateRegistry.getRegistry(SGServer.getDefaultPort());
		}
		System.err.println(registry);
	}

	public Registry getRegistry() {
		return registry;
	};

	public static void teardown() throws RemoteException {
		UnicastRemoteObject.unexportObject(registry, true);
	}

	public static SGServer runServerOvervoltage() throws IOException,
			java.rmi.AlreadyBoundException, GridlabException,
			ParserConfigurationException, SAXException, InterruptedException,
			RemoteException, NotBoundException {
		SGServer server = null;

		Tariff tarrif = new TariffExample();
		Calendar calendar = Calendar.getInstance();
		calendar.set(2014, 4, 15, 10, 30, 0); // Set the start time of
		// simulation
		String gridFile = "src/main/resources/griddef/overvoltageGrid.xml";
		String configFile = "src/main/resources/griddef/configuration.glm";
		String scenarioFile = "src/main/resources/griddef/scenario.csv";
		try {
			server = new SGServer("My simulation", calendar.getTime(),
					gridFile, configFile, scenarioFile,
					ManageServer.hoursOfSimulation,
					ManageServer.cycleTimeInMilliseconds * 10,
					ManageServer.momentsToShow, tarrif, true,
					ManageServer.realTimeCycleLengthMillis);
			RunServer1.createFileStatsClient(server);
			server.runServer();

		} catch (Throwable t) {
			t.printStackTrace();
		}
		return server;
	}

	public static void main(String args[]) throws Exception {
		setup();
		SGServer server;
		try {
			server = runServerOvervoltage();
			server.executeOrder("transformerCT1",
					new RemoteSwitchOn("Solar_11"));
			Thread.currentThread().sleep(3000);
			server.executeOrder("transformerCT1", new RemoteSwitchOff(
					"Solar_11"));
			Thread.currentThread().sleep(3000);
			server.executeOrder("transformerCT1", new RemoteSwitchOff(
					"Solar_11"));
			server.waitForSimulationToFinish();

		} catch (GridlabException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		} catch (java.rmi.AlreadyBoundException e) {
			e.printStackTrace();
		}
		teardown();
	}

}
