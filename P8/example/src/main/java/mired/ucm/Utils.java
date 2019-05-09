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
package mired.ucm;

import java.util.Vector;

import mired.ucm.remote.orders.RemoteOrder;
import mired.ucm.remote.orders.RemoteSwitchOff;
import mired.ucm.remote.orders.RemoteSwitchOn;

public class Utils {

	/**
	 * Returns true if there is no instance of X issued after the last instance
	 * of Y or there hasn't been any Y, false i.o.c.
	 * 
	 * @param history
	 *            the orders history
	 * @param X
	 * @param Y
	 * @return true if there is no instance of X issued after the last instance
	 *         of Y, false i.o.c.
	 */
	public static boolean noOrderXAfterLastY(
			Vector<Pair<Long, RemoteOrder>> history, RemoteOrder X,
			RemoteOrder Y) {

		int indexX = -1;
		int indexY = -1;
		for (int k = history.size() - 1; k >= 0
				&& (indexX == -1 || indexY == -1); k--) {
			if (history.elementAt(k).getSecond().equals(X) && indexX == -1) {
				indexX = k;
			}
			if (history.elementAt(k).getSecond().equals(Y) && indexY == -1) {
				indexY = k;
			}
		}
		return indexX <= indexY || indexY == -1;
	}

	public static boolean thereExistsXWithinYMillis(
			Vector<Pair<Long, RemoteOrder>> history, RemoteOrder X,
			long maxValue) {

		int indexX = -1;
		for (int k = history.size() - 1; k >= 0 && (indexX == -1); k--) {
			if (history.elementAt(k).getSecond().equals(X)
					&& indexX == -1
					&& history.lastElement().getFirst()
							- history.elementAt(k).getFirst() < maxValue) {
				indexX = k;
			}
		}
		return indexX != -1;
	}

	public static boolean lastOrderWasXAndIsYMillisOld(
			Vector<Pair<Long, RemoteOrder>> history, RemoteOrder X,
			long maxValue) {
		return history.size() > 0
				&& history.lastElement().getSecond().equals(X)
				&& history.lastElement().getFirst() < maxValue;
	}

}
