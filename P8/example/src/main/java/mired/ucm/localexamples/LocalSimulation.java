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
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import javax.xml.parsers.ParserConfigurationException;

import mired.ucm.grid.ElementEMS;
import mired.ucm.monitor.ReadScenario;
import mired.ucm.price.Tariff;
import mired.ucm.properties.PATHS;
import mired.ucm.simulator.GridlabActions;
import mired.ucm.simulator.GridlabException;
import mired.ucm.simulator.SensorContainerNotFound;
import mired.ucm.simulator.SensorIDS;
import mired.ucm.simulator.SimulatorNotInitializedException;
import mired.ucm.simulator.clients.Client;
import mired.ucm.simulator.displayer.SimulatorDisplayer;

import org.xml.sax.SAXException;

/**
 * Server that launches the simulator
 * 
 * @author Nuria Cuartero-Soler
 * @author Jorge J. Gomez-Sanz
 * 
 */
public class LocalSimulation {

	private static final int DEFAULT_PORT = 60010;
	private static final String DEFAULT_REMOTE_SERVER_NAME = "Server";
	private static final String DEFAULT_TITLE = "Smart Grid Simulator UCM";
	private static Registry registry;
	private int port;
	private String name;
	private final PATHS paths;
	private ReadScenario readScenario;
	private GridlabActions gridlabActions;
	private SimulatorDisplayer sd;

	/**
	 * Constructor of the class.
	 * 
	 * @param title
	 *            title for the simulation
	 * @param starttime
	 *            start time of simulation
	 * @param netXmlFile
	 *            network file
	 * @param configurationFile
	 *            configuration file
	 * @param scenarioFile
	 *            scenario file
	 * @param hoursOfSimulation
	 *            number of hours of simulation
	 * @param cycleTimeInMinutes
	 *            time cycle considered to advance in the simulation
	 * @param nMoments
	 *            number of moments to show in the simulation chart
	 * @param tariff
	 *            electric tariff considered
	 * @param activeClients
	 *            if true, it is assumed that clients will work independently.
	 *            If false, the simulator will ask clients for orders to execute
	 *            every cycle of simulation
	 * @throws IOException
	 * @throws AlreadyBoundException
	 * @throws GridlabException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public LocalSimulation(String title, Date starttime, String netXmlFile,
			String configurationFile, String scenarioFile,
			int hoursOfSimulation, int cycleTimeInMilliseconds, int nMoments,
			Tariff tariff, boolean activeClients) throws IOException,
			AlreadyBoundException, GridlabException,
			ParserConfigurationException, SAXException {

		PATHS.debugMode = false;
		paths = new PATHS(configurationFile, netXmlFile, scenarioFile,
				"target/resources/");
		boolean intelligence = true;

		String stitle = DEFAULT_TITLE;
		if (title != null && !title.isEmpty()) {
			stitle += ": " + title;
		}
		sd = new SimulatorDisplayer(
				stitle,
				starttime,
				cycleTimeInMilliseconds,
				paths,
				intelligence,
				hoursOfSimulation,
				tariff,
				nMoments,
				null,
				null,
				activeClients,
				mired.ucm.examples.server.ManageServer.realTimeCycleLengthMillis);
		readScenario = sd.getReadScenario();
		gridlabActions = sd.getGridlabActions();
	}

	/**
	 * Constructor of the class.
	 * 
	 * @param title
	 *            title for the simulation
	 * @param netXmlFile
	 *            network file
	 * @param configurationFile
	 *            configuration file
	 * @param scenarioFile
	 *            scenario file
	 * @param cycleTimeInMinutes
	 *            time cycle considered to advance in the simulation
	 * @param nMoments
	 *            number of moments to show in the simulation chart
	 * @param tariff
	 *            electric tariff considered
	 * @param activeClients
	 *            if true, it is assumed that clients will work independently.
	 *            If false, the simulator will ask clients for orders to execute
	 *            every cycle of simulation
	 * @throws IOException
	 * @throws AlreadyBoundException
	 * @throws GridlabException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public LocalSimulation(String title, String netXmlFile,
			String configurationFile, String scenarioFile,
			int cycleTimeInMinutes, int nMoments, Tariff tariff,
			boolean activeClients) throws IOException, AlreadyBoundException,
			GridlabException, ParserConfigurationException, SAXException {

		PATHS.debugMode = false;
		paths = new PATHS(configurationFile, netXmlFile, scenarioFile,
				"target/resources/");
		boolean intelligence = true;

		String stitle = DEFAULT_TITLE;
		if (title != null && !title.isEmpty()) {
			stitle += ": " + title;
		}
		sd = new SimulatorDisplayer(stitle, cycleTimeInMinutes, paths,
				intelligence, tariff, nMoments, null, null, activeClients);
		readScenario = sd.getReadScenario();
		gridlabActions = sd.getGridlabActions();
	}

	public Date getTime() {
		return sd.getGridLabDiscreteEventSimulator().getSimTime();
	}

	public void addClient(Client c) throws SimulatorNotInitializedException {
		this.sd.addClient(c);
	}

	public double getSystemConsumption() {
		return readScenario.getConsumption(getSimulatorDisplayer().getNetwork()
				.getElements(), getTime());
	}

	public Hashtable<SensorIDS, Float> getSubstationSensors() {
		return gridlabActions.getSubstationSensors(getTime());
	}

	public float getLossesSensor() {
		return gridlabActions.getLossesSensor(getTime());
	}

	public Hashtable<SensorIDS, Float> getTransformerSensors(
			String transformerName, Date timestamp) throws RemoteException,
			SensorContainerNotFound {
		return gridlabActions.getTransformerSensors(transformerName, timestamp);
	}

	public Hashtable<SensorIDS, Float> getTransformerSensors(
			String transformerName) throws RemoteException,
			SensorContainerNotFound {
		return gridlabActions.getTransformerSensors(transformerName, getTime());
	}

	public Hashtable<SensorIDS, Float> getDeviceSensors(String transformerName,
			String deviceName, Date timestamp) throws RemoteException {
		return gridlabActions.getDeviceSensors(deviceName, timestamp);
	}

	public Hashtable<SensorIDS, Float> getDeviceSensors(String transformerName,
			String deviceName) throws RemoteException {
		return gridlabActions.getDeviceSensors(deviceName, getTime());
	}

	public double getCurrentSun(Date currentTime) {
		return readScenario.getSunPercentage(currentTime);
	}

	public double getCurrentWind(Date currentTime) {
		return readScenario.getWindPercentage(currentTime);
	}

	public double getCurrentEnergy(String batteryName, Date currentTime)
			throws RemoteException {
		String energy = gridlabActions.readBatteryEnergy(batteryName,
				currentTime);
		if (energy == null) {
			return 0;
		} else {
			return Double.parseDouble(energy);
		}
	}

	public void start() {
		sd.start();
	}

	public void stop() throws TimeoutException, InterruptedException {
		sd.stopServer();
		sd.join(5000);
	}

	public double getCurrentEnergy(String batteryName) throws RemoteException {
		String energy = gridlabActions
				.readBatteryEnergy(batteryName, getTime());
		if (energy == null) {
			return 0;
		} else {
			return Double.parseDouble(energy);
		}
	}

	public boolean isServerActive() {
		return !getSimulatorDisplayer().getGridLabDiscreteEventSimulator()
				.isSimulationEnded();
	}

	public boolean isSimulatorBusy() {
		return !sd.getGridLabDiscreteEventSimulator().getSimulator()
				.getPendingOrders().isEmpty();
	}

	/**
	 * Get the simulation displayer frame instance
	 * 
	 * @return
	 */
	public SimulatorDisplayer getSimulatorDisplayer() {
		return sd;
	}

	private void printCopyright() {
		System.out
				.println("SGSim Copyright (C) 2014 N. Cuartero-Soler, S. Garcia-Rodriguez, J.J Gomez-Sanz\n"
						+ "This program comes with ABSOLUTELY NO WARRANTY; for details type 'show w'.\n"
						+ "This is free software, and you are welcome to redistribute it\n"
						+ "under certain conditions; type `show c' for details.\n");
	}

	public Set<String> getControllableDevices(String transformerName) {
		Set<ElementEMS> controlledDevices = getSimulatorDisplayer()
				.getNetwork().getControllableElementsByTransformer(
						transformerName);
		Set<String> controlledDevNames = new HashSet<String>();
		for (ElementEMS el : controlledDevices) {
			controlledDevNames.add(el.getName());
		}
		return controlledDevNames;
	}
}
