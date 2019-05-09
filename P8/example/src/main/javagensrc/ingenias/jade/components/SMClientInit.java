/**
 *
 *
 *  Description of the task /
 *
 * 
 *@author     Jorge J. Gomez
 *@version    1.0
 */

package ingenias.jade.components;

import java.util.*;
import ingenias.jade.exception.*;
import ingenias.jade.JADEAgent;
import ingenias.jade.mental.*;
import ingenias.jade.components.*;
import ingenias.jade.smachines.*;

public class SMClientInit {

	private static java.lang.String semaphore = "SMClient";

	private static Vector<SMClientAppImp> appsinitialised = new Vector<SMClientAppImp>();

	public static void initialize(SMClientAppImp app) {
		// #start_node: <--- DO NOT REMOVE THIS

		// #end_node: <--- DO NOT REMOVE THIS
	}

	public static void shutdown(SMClientAppImp app) {
		// #start_node: <--- DO NOT REMOVE THIS

		// #end_node: <--- DO NOT REMOVE THIS
	}

	public static void shutdown() {
		synchronized (semaphore) {

			for (int k = 0; k < appsinitialised.size(); k++) {
				shutdown(appsinitialised.elementAt(k));
			}

		}
	}

	public static Vector<SMClientAppImp> getAppsInitialised() {
		return appsinitialised;
	}
	public static SMClientApp createInstance() {
		synchronized (semaphore) {
			SMClientAppImp app = new SMClientAppImp();
			initialize(app);
			appsinitialised.add(app);

			return app;
		}
	}
	public static SMClientApp createInstance(JADEAgent owner) {
		synchronized (semaphore) {
			SMClientAppImp app = new SMClientAppImp();
			app.registerOwner(owner);
			initialize(app);
			appsinitialised.add(app);

			return app;
		}
	}

}
