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
import mired.ucm.remote.server.SGServer;
import mired.ucm.simulator.GridlabException;
import junit.framework.TestCase;

/**
 * Manage server configuration
 * 
 * @author Jorge J. Gomez-Sanz
 * @author Nuria Cuartero-Soler
 * 
 */
public class ManageServer {

	public static final int hoursOfSimulation = 100; // Hours of simulation
	public static final int cycleTimeInMilliseconds = 60000; // Setting cycle
																// minutes
	public static final long realTimeCycleLengthMillis = 100; // Setting cycle
																// minutes
	public static final int momentsToShow = 30; // Moments to show in the chart

}
