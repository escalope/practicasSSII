/*
 Copyright (C) 2005 Jorge Gomez Sanz

 This file is part of INGENIAS Agent Framework, an agent infrastructure linked
 to the INGENIAS Development Kit, and availabe at http://grasia.fdi.ucm.es/ingenias or
 http://ingenias.sourceforge.net. 

 INGENIAS Agent Framework is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 INGENIAS Agent Framework is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with INGENIAS Agent Framework; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 */

package ingenias.jade.smachines;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.core.behaviours.*;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;

import jade.core.*;
import jade.core.behaviours.*;

import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.domain.DFService;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import ingenias.jade.*;
import java.util.*;

import ingenias.jade.mental.*;
import ingenias.jade.comm.ActiveConversation;
import ingenias.jade.comm.ConversationManagement;
import ingenias.jade.comm.StateBehavior;
import ingenias.jade.components.*;
import ingenias.jade.exception.*;
import ingenias.jade.graphics.AgentGraphics;

import javax.swing.*;

import ingenias.jade.components.*;
import java.awt.event.*;

import java.awt.*;

import ingenias.editor.entities.Entity;
import ingenias.editor.entities.FrameFact;
import ingenias.editor.entities.Interaction;
import ingenias.editor.entities.MentalEntity;
import ingenias.editor.entities.MentalStateProcessor;
import ingenias.editor.entities.Slot;
import ingenias.editor.entities.StateGoal;
import ingenias.editor.entities.RuntimeConversation;
import ingenias.editor.*;
import ingenias.jade.comm.DefaultCommControl;

import javax.swing.border.*;

import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.graph.DefaultGraphCell;
import ingenias.jade.comm.AgentProtocols;
import ingenias.jade.comm.LocksWriter;

public class PV_Token_based_controllerProtocol implements AgentProtocols {

	interface AdapterYP {

		public DFAgentDescription[] getAgents(String role) throws FIPAException;

	}

	public PV_Token_based_controllerProtocol() {
	};

	public ActiveConversation initialiseProtocols(String agentName,
			RuntimeConversation conv, MentalStateReader msr,
			MentalStateUpdater msu, ingenias.jade.comm.LocksRemover lr,
			AgentExternalDescription[] actors) throws NoAgentsFound,
			WrongInteraction {
		// Adds a protocol to the protocol queue
		StateBehavior sb = null;
		// Interactions in which this agent appears as collaborator

		if (conv.getInteraction().getId().equals("TokenProvidingInt0")
				&& conv.getPlayedRole().equals("TokenRequester")) {
			DefaultCommControl dcc = new TokenRequesterTokenProvidingInt0DefaultCommControl(
					conv.getConversationID(), msr, lr);
			sb = new TokenRequesterTokenProvidingInt0StateBehavior(agentName,
					msr, msu, conv, conv.getPlayedRole(), actors, dcc, conv
							.getInteraction().getId());
			ActiveConversation aconv = new ActiveConversation(sb, dcc, conv);
			return aconv;
		}

		// Interactions in which this agent appears as initiator

		if (conv.getInteraction().getId().equals("TokenProvidingInt0")
				&& conv.getPlayedRole().equals("TokenProvider")) {
			DefaultCommControl dcc = new TokenProviderTokenProvidingInt0DefaultCommControl(
					conv.getConversationID(), msr, lr);
			sb = new TokenProviderTokenProvidingInt0StateBehavior(agentName,
					msr, msu, conv, conv.getPlayedRole(), actors, dcc, conv
							.getInteraction().getId());
			ActiveConversation aconv = new ActiveConversation(sb, dcc, conv);
			return aconv;
		}

		throw new WrongInteraction("Agent " + agentName
				+ " does not know any interaction protocol of type "
				+ conv.getInteraction().getId() + " where the "
				+ " agent knows how to play the protocol "
				+ conv.getPlayedRole());

	}

	public AgentExternalDescription[] getInteractionActors(
			final String interaction, final String orgid, final YellowPages yp)
			throws ingenias.jade.exception.NoAgentsFound {
		return getInteractionActorsWithAdapter(interaction, new AdapterYP() {
			public DFAgentDescription[] getAgents(String role)
					throws FIPAException {
				return yp.getAgents(orgid, role);
			};
		});
	}

	public AgentExternalDescription[] getInteractionActors(
			final String interaction, final String orgid, final String groupid,
			final YellowPages yp) throws ingenias.jade.exception.NoAgentsFound {
		return getInteractionActorsWithAdapter(interaction, new AdapterYP() {
			public DFAgentDescription[] getAgents(String role)
					throws FIPAException {
				return yp.getAgents(orgid, groupid, role);
			};
		});
	}

	public AgentExternalDescription[] getInteractionActors(String interaction,
			final YellowPages yp) throws ingenias.jade.exception.NoAgentsFound {
		return getInteractionActorsWithAdapter(interaction, new AdapterYP() {
			public DFAgentDescription[] getAgents(String role)
					throws FIPAException {
				return yp.getAgents(role);
			};
		});
	}

	private AgentExternalDescription[] getInteractionActorsWithAdapter(
			String interaction, AdapterYP yp)
			throws ingenias.jade.exception.NoAgentsFound {
		AgentExternalDescription[] result = null;

		if (interaction.equals("TokenProvidingInt0")) {

			Vector<AgentExternalDescription> cols = new Vector<AgentExternalDescription>();

			try {
				String cardinality = "1";

				DFAgentDescription[] agents = null;
				agents = yp.getAgents("TokenRequester");
				if (agents == null || agents.length <= 0)
					throw new ingenias.jade.exception.NoAgentsFound(
							"Could not find an agent playing the role TokenRequester");
				if (cardinality.equals("1") || cardinality.equals(""))
					cols.add(new AgentExternalDescription(agents[0].getName(),
							"TokenRequester"));
				else if (cardinality.equals("1__*"))
					for (int k = 0; k < agents.length; k++)
						cols.add(new AgentExternalDescription(agents[k]
								.getName(), "TokenRequester"));
			} catch (FIPAException fe) {
				fe.printStackTrace();
				throw new NoAgentsFound();
			}

			result = new AgentExternalDescription[cols.size()];
			cols.toArray(result);
		}

		return result;
	}

	/*
	 * public DFAgentDescription[ ] getDescription(String protocol, AID agentID)
	 * { DFAgentDescription[] result=null; Vector playedRoles=new Vector();
	 * DFAgentDescription dfd=null; dfd = new DFAgentDescription();
	 * 
	 * if (protocol.equals("TokenProvidingInt0")){ dfd.setName(agentID);
	 * ServiceDescription sd = new ServiceDescription();
	 * sd.setName(agentID.getLocalName() + "-sub-df");
	 * sd.setType("TokenProvider"); sd.setOwnership("JADE");
	 * dfd.addServices(sd); playedRoles.add(dfd); }
	 * 
	 * 
	 * if (protocol.equals("TokenProvidingInt0")){ dfd.setName(agentID);
	 * ServiceDescription sd = new ServiceDescription();
	 * sd.setName(agentID.getLocalName() + "-sub-df");
	 * sd.setType("TokenRequester"); sd.setOwnership("JADE");
	 * dfd.addServices(sd); playedRoles.add(dfd); }
	 * 
	 * result=new DFAgentDescription[playedRoles.size()];
	 * playedRoles.toArray(result); return result;
	 * 
	 * }
	 */

	public boolean verifyActors(String protocol,
			AgentExternalDescription[] actors) {

		if (protocol.equals("TokenProvidingInt0")) {
			Vector<String> toVerify = new Vector<String>();
			HashSet<String> rolesFound = new HashSet<String>();

			toVerify.add("TokenRequester");

			if (actors.length < toVerify.size())
				return false;
			boolean found = false;
			for (int k = 0; k < actors.length; k++) {
				if (toVerify.contains(actors[k].role)) {
					rolesFound.add(actors[k].role);
				}
			}
			return toVerify.size() == rolesFound.size();
		}

		return false;

	}

}
