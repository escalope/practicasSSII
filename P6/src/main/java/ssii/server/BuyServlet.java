package ssii.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

import ssii.drools.shopping.ShoppingExample;
import ssii.drools.shopping.ShoppingExample.Customer;
import ssii.drools.shopping.ShoppingExample.Product;
import ssii.drools.shopping.ShoppingExample.Purchase;
import ssii.drools.shopping.ShoppingExample.BotDetected;

/**
 * Example of drools integration in a web app. It uses
 * the drools discount demo to integrate it in a regular servlet. It also serves to show how Complex Event Processing makes sense to detect 
 * irregular user behaviors 
 *  
 *  Distributed under GPLv3 license
 *  
 * @author Jorge J. Gomez-Sanz
 *
 */

public class BuyServlet extends HttpServlet
{
	static Vector<Product> products=new Vector<Product>();
	
	static KieServices ks = KieServices.Factory.get();
	static KieFileSystem kFileSystem = ks.newKieFileSystem();
	static KieBase kieBase=null;
	static KieContainer kContainer=null;

	static {
		FileInputStream fis;
		try {
			fis = new FileInputStream( "src/main/java/ssii/drools/shopping/Shopping.drl" );
			kFileSystem.write("src/main/resources/somename.drl",
					ks.getResources().newInputStreamResource( fis ) );

			KieBuilder kbuilder = ks.newKieBuilder( kFileSystem );
			kbuilder.buildAll();
			if (kbuilder.getResults().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
				throw new RuntimeException("Build time Errors: " + kbuilder.getResults().toString());
			}    
			kContainer = ks.newKieContainer(ks.getRepository().getDefaultReleaseId());

			KieBaseConfiguration config = ks.newKieBaseConfiguration();
			config.setOption(EventProcessingOption.STREAM);

			kieBase = kContainer.newKieBase( config );

			if (kbuilder.getResults().hasMessages(Message.Level.ERROR)) {
				throw new RuntimeException("Build Errors:\n" + kbuilder.getResults().toString());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	       
	}
	
	static {
		products.add(new Product("helmet",10));
		products.add(new Product("hat",100));
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{ 	
		HttpSession session = request.getSession(true);

		
		Vector<Product> basket=new Vector<Product>();	
		KieSession ksession =null;
		Customer customer=null;

		if (session.getAttribute("basket")!=null) {			
			basket=(Vector<Product>)session.getAttribute("basket");
			ksession = (KieSession)session.getAttribute("ksession");
			customer=(Customer)session.getAttribute("customer");
		} else {		
				ksession = kieBase.newKieSession();
				// Uncomment this to check rule firing events
				/*DefaultAgendaEventListener dae= new DefaultAgendaEventListener(){
					@Override
					public void afterMatchFired(AfterMatchFiredEvent event) {
						// TODO Auto-generated method stub
						super.afterMatchFired(event);
						System.err.println("Rule fired: "+event.getMatch().getRule().getName());
					}

				};
				ksession.addEventListener(dae);
				*/
				customer = new Customer( "mark",
						0 );
				ksession.insert( customer );
				      
				session.setAttribute("basket", basket);
				session.setAttribute("customer", customer);
				session.setAttribute("ksession",ksession);			
		}	


		

		procesarPeticion(customer, ksession, request,basket);

		printScreen(customer, ksession,response,basket);

		// uncomment this to check the working memory progress
		//ShoppingExample.printWorkingMemory(ksession);
	}

	private void procesarPeticion(
			Customer customer, 
			KieSession ksession, 
			HttpServletRequest request, 
			Vector<Product> cesta2) {
		if (request.getParameter("item")!=null && 
				request.getParameter("op")!= null && 
				request.getParameter("op").equalsIgnoreCase("buy")) {
			Product p=null;			
			for (Product disponible:products) {
				if (disponible.getName().equalsIgnoreCase(request.getParameter("item"))) {
					cesta2.add(disponible);					
					ksession.insert( new Purchase( customer,
							disponible));

				}
			}
		}
		if (request.getParameter("item")!=null && 
				request.getParameter("op")!= null &&
				request.getParameter("op").equalsIgnoreCase("delete")) {
			Product p=null;
			for (Product disponible:products) {
				if (disponible.getName().equalsIgnoreCase(request.getParameter("item"))) {
					cesta2.remove(disponible);
					Collection<FactHandle> handles = ksession.getFactHandles();
					for (FactHandle fh:handles) {
						Object toDelete=ksession.getObject(fh);
						if (toDelete instanceof Purchase && ((Purchase) toDelete).getProduct().equals(disponible)) {
							ksession.delete(fh);
							break;
						}
					}
				}
			}
		}

		ksession.fireAllRules();
	}

	private void printScreen( Customer customer, KieSession ksession, HttpServletResponse response, Vector<Product> basket) throws IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		Collection<FactHandle> handles = ksession.getFactHandles();
		for (FactHandle fh:handles) {
			Object toDelete=ksession.getObject(fh);
			if (toDelete instanceof BotDetected) {
				ksession.delete(fh);
				response.getWriter().println("<div style='background-color:red;color:yellow'> BOT ATTEMPT DETECTED </div>");
				break;
			}
		}	

		response.getWriter().println("<h1>Already bought</h1>");

		for (Product p:basket) {
			response.getWriter().println("<form action='/sales/buy'>");
			response.getWriter().println("<input type='hidden' name='item' value='"+p.getName()+"'/>");
			response.getWriter().println("<input type='hidden' name='op' value='delete'/>");
			response.getWriter().println("<input type=submit value='delete "+p.getName()+":"+p.getPrice()+" €'/>");
			response.getWriter().println("</form>");
		}


		response.getWriter().println("Total: "+customer.getShoppingTotal());
		response.getWriter().println("Discount: "+customer.getDiscount());
		response.getWriter().println("To pay: "+(customer.getShoppingTotal()-customer.getDiscount()));



		response.getWriter().println("<h1>Add to basket</h1>");
		for (Product p:products) {
			response.getWriter().println("<form action='/sales/buy'>");
			response.getWriter().println("<input type='hidden' name='item' value='"+p.getName()+"'/>");
			response.getWriter().println("<input type='hidden' name='op' value='buy'/>");
			response.getWriter().println("<input type=submit value='buy "+p.getName()+":"+p.getPrice()+" €'/>");
			response.getWriter().println("</form>");
		}
	}


	public static void main(String[] args) throws Exception
	{
		Server server = new Server(8080);
		ServletContextHandler servletContextHandler = new ServletContextHandler(server, "/sales", true, false);
		servletContextHandler.addServlet("ssii.server.BuyServlet","/buy");

		server.start();
		server.join();
	}
}

