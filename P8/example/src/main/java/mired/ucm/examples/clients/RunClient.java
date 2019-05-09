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

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.ParserConfigurationException;

import mired.ucm.examples.server.ManageServer;
import mired.ucm.remote.client.SGClient;
import mired.ucm.remote.orders.RemoteSwitchOn;
import mired.ucm.remote.server.SGServer;
import mired.ucm.remote.server.RemoteSGServer;
import mired.ucm.simulator.GridlabException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import junit.framework.TestCase;

/**
 * Runs a client sample
 * 
 * @author Nuria Cuartero-Soler
 * @author Jorge J. Gomez-Sanz
 * 
 */
public class RunClient {
	private static Registry registry;
	private static Thread serverThread;

	public static void setup() throws RemoteException {

		try {
			registry = LocateRegistry.createRegistry(SGServer.getDefaultPort());
		} catch (RemoteException e) {
			registry = LocateRegistry.getRegistry(SGServer.getDefaultPort());
		}
	}

	public static Registry getRegistry() {
		return registry;
	};

	public static void teardown() throws RemoteException {
		// UnicastRemoteObject.unexportObject(registry, true);
	}

	public static void launchAndWaitForServerShutdown(SGClient client)
			throws Exception {
		setup();
		// Creates a client and tries to launch it a number of times
		// before giving up
		System.out.println("Launching client...");
		boolean failed = true;
		for (int k = 0; k < 20 && failed; k++)
			try {
				// Gets the remote server reference and registers the client
				tryLaunchingClient(client);
				System.out.println("Client launched");
				failed = false;
			} catch (Exception e) {
				e.printStackTrace();
				try {
					Thread.currentThread().sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		teardown();
	}

	private static void tryLaunchingClient(SGClient client)
			throws RemoteException, NotBoundException, AccessException,
			AlreadyBoundException, InterruptedException {

		System.out.println("Looking for the server");
		RemoteSGServer stub = (RemoteSGServer) getRegistry().lookup(
				SGServer.getDefaultRemoteServerName());
		System.out.println("Server found");
		// after creating it, it starts delivering orders
		client.serverStarted(stub);
		// Wait for simulation to end
		while (stub.isServerActive() && !client.isFinished()) {
			Thread.sleep(2000);
		}
		client.serverStopped();
	}

}
