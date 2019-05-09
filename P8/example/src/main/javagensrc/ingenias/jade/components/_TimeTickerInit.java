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

public class _TimeTickerInit {

	private static _TimeTickerAppImp instance = null;

	private static java.lang.String semaphore = "_TimeTicker";

	public static void initialize(_TimeTickerAppImp app) {
		// #start_node:_codefaketimer <--- DO NOT REMOVE THIS
		Vector<org.javatuples.Pair<String, Long>> mes;
		Hashtable<String, Vector<org.javatuples.Pair<String, Long>>> notifications = new Hashtable<String, Vector<org.javatuples.Pair<String, Long>>>();
		mes = new Vector<org.javatuples.Pair<String, Long>>();
		mes.add(org.javatuples.Pair.with("ingenias.jade.mental._TimeTick4", 3l));
		notifications.put("PV_Controller", mes);
		mes = new Vector<org.javatuples.Pair<String, Long>>();
		mes.add(org.javatuples.Pair.with("ingenias.jade.mental._TimeTick1", 9l));
		notifications.put("Battery_Controller", mes);
		mes = new Vector<org.javatuples.Pair<String, Long>>();
		mes.add(org.javatuples.Pair.with("ingenias.jade.mental._TimeTick2", 5l));
		mes.add(org.javatuples.Pair.with("ingenias.jade.mental._TimeTick3", 3l));
		notifications.put("PV_Token_based_controller", mes);
		ingenias.jade.components.TimeTicking.createMentalEntities(app,
				notifications);

		// #end_node:_codefaketimer <--- DO NOT REMOVE THIS
	}

	public static void shutdown(_TimeTickerAppImp app) {
		// #start_node: <--- DO NOT REMOVE THIS

		// #end_node: <--- DO NOT REMOVE THIS
	}

	public static void shutdown() {
		synchronized (semaphore) {

			if (instance != null) {
				shutdown(instance);
			}

		}
	}

	public static _TimeTickerApp getInstance() {
		synchronized (semaphore) {
			if (instance == null) {
				instance = new _TimeTickerAppImp();
				initialize(instance);
			}

			return instance;
		}
	}
	public static _TimeTickerApp getInstance(JADEAgent owner) {
		synchronized (semaphore) {
			if (instance == null) {
				instance = new _TimeTickerAppImp();
				initialize(instance);
			}
			instance.registerMultipleOwners(owner);

			return instance;
		}
	}

}
