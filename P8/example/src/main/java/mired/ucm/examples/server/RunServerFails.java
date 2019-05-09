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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import mired.ucm.monitor.Event;
import mired.ucm.price.Tariff;
import mired.ucm.remote.server.SGServer;
import mired.ucm.simulator.GridlabException;
import mired.ucm.simulator.NetworkElementsStatus;
import mired.ucm.simulator.NetworkListener;
import mired.ucm.simulator.NetworkStatus;
import mired.ucm.simulator.SensorIDS;
import mired.ucm.simulator.SimulatorNotInitializedException;
import mired.ucm.simulator.NetworkStatus.UnknownSensor;
import junit.framework.TestCase;

/**
 * Runs the server
 * 
 * @author Nuria Cuartero-Soler
 * @author Jorge J. Gomez-Sanz
 * 
 */
public class RunServerFails {

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

	public static SGServer runServerFails() throws IOException,
			java.rmi.AlreadyBoundException, GridlabException,
			ParserConfigurationException, SAXException, InterruptedException,
			RemoteException, NotBoundException {
		SGServer server = null;

		Tariff tarrif = new TariffExample();
		Calendar calendar = Calendar.getInstance();
		calendar.set(2014, 4, 15, 6, 0, 0); // Set the start time of
		// simulation. Parsing failure.
		String gridFile = "src/main/resources/griddef/wrongGrid.xml";
		String configFile = "src/main/resources/griddef/configuration.glm";
		String scenarioFile = "src/main/resources/griddef/scenario.csv";
		try {
			server = new SGServer("My simulation", calendar.getTime(),
					gridFile, configFile, scenarioFile,
					ManageServer.hoursOfSimulation,
					ManageServer.cycleTimeInMilliseconds,
					ManageServer.momentsToShow, tarrif, true,
					ManageServer.realTimeCycleLengthMillis);
			createFileStatsClient(server);
			server.runServer();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return server;
	}

	private static void createFileStatsClient(final SGServer server)
			throws IOException, FileNotFoundException,
			SimulatorNotInitializedException {
		if (!(new File("target").exists()))
			new File("target").mkdir();
		File output = File.createTempFile("record", ".csv", new File("target"));
		final FileOutputStream fos = new FileOutputStream(output);
		server.getSimulatorDisplayer().getGridLabDiscreteEventSimulator()
				.registerListener(new NetworkListener() {

					@Override
					public void processStatus(
							Hashtable<SensorIDS, Float> sensorValues,
							Date timestamp) {
					}

					@Override
					public void processEvent(Event event) {
						try {
							fos.write((";;" + event.getTimestamp() + ";"
									+ event.getDeviceName() + ";").getBytes());
							fos.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					Vector<String> sensorOrder = new Vector<String>();

					@Override
					public void processCycleStatus(NetworkStatus ns,
							NetworkElementsStatus nes, Date timestamp) {

						try {
							if (sensorOrder.isEmpty()) {
								// first iteration. Headers are written
								for (SensorIDS sensor : ns
										.getSubstationSensors().keySet()) {
									sensorOrder.add(sensor.toString());
								}
								fos.write(("timestamp;overvoltage;").getBytes());
								for (String sid : sensorOrder) {
									fos.write(("" + sid + ";").getBytes());
								};

								fos.write("executedOrders\n".getBytes());

							}
							// sensor data is written in the same order
							// everytime
							fos.write((timestamp + ";"
									+ ns.thereIsOvervoltage() + ";").getBytes());
							double[] values = new double[sensorOrder.size()];
							for (SensorIDS sensor : ns.getSubstationSensors()
									.keySet()) {
								values[sensorOrder.indexOf(sensor.toString())] = ns
										.getSubstationSensor(sensor);
							}
							for (int k = 0; k < values.length; k++) {
								fos.write(("" + values[k] + ";").getBytes());
							}
							fos.write(server.getAppliedOrdersSince(timestamp)
									.toString().getBytes());
							fos.write("\n".getBytes());

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (UnknownSensor e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void overvoltageEvent(Date timestamp) {

					}
				});
	}

	public static void main(String args[]) throws Exception {
		setup();
		SGServer server;
		try {
			server = runServerFails();
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
