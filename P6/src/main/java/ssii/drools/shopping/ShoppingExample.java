/*
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ssii.drools.shopping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.drools.core.event.DebugAgendaEventListener;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.AgendaGroupPoppedEvent;
import org.kie.api.event.rule.AgendaGroupPushedEvent;
import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.event.rule.MatchCancelledEvent;
import org.kie.api.event.rule.MatchCreatedEvent;
import org.kie.api.event.rule.RuleFlowGroupActivatedEvent;
import org.kie.api.event.rule.RuleFlowGroupDeactivatedEvent;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.KieSessionOption;
import org.kie.api.runtime.conf.MultiValueKieSessionOption;
import org.kie.api.runtime.conf.SingleValueKieSessionOption;
import org.kie.api.runtime.rule.FactHandle;

public class ShoppingExample {

	static KieSession ksession=null;
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
			ksession = kieBase.newKieSession();

			if (kbuilder.getResults().hasMessages(Message.Level.ERROR)) {
				throw new RuntimeException("Build Errors:\n" + kbuilder.getResults().toString());
			}
			
			DefaultAgendaEventListener dae= new DefaultAgendaEventListener(){
				@Override
				public void afterMatchFired(AfterMatchFiredEvent event) {
					// TODO Auto-generated method stub
					super.afterMatchFired(event);
					System.err.println("Rule fired: "+event.getMatch().getRule().getName());
				}

			};

			ksession.addEventListener(dae);      

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	       
	}


	public static final void main(String[] args) throws FileNotFoundException {

		Customer mark = new Customer( "mark",
				0 );
		ksession.insert( mark );

		Product shoes = new Product( "shoes",
				60 );
		ksession.insert( shoes );

		Product hat = new Product( "hat",
				60 );
		ksession.insert( hat );

		ksession.insert( new Purchase( mark,
				shoes ) );
		
		waitMillis(1000); // modify this to add a delay until the next purchase insertion

		FactHandle hatPurchaseHandle = ksession.insert( new Purchase( mark,
				hat ) );
		//printWorkingMemory(ksession);

		ksession.fireAllRules();
		//printWorkingMemory(ksession);
		System.out.println("New purchase does not create discount");
		hat = new Product( "hat",
				60 );
		ksession.insert( new Purchase( mark,
				hat ) );
		ksession.insert( hat );

		ksession.fireAllRules();    
		//printWorkingMemory(ksession);
		ksession.delete( hatPurchaseHandle );
		//System.out.println( "Customer  has returned the hat" );
		//printWorkingMemory(ksession);
		ksession.fireAllRules();
		//printWorkingMemory(ksession);
		ksession.destroy(); // finishes the session 

	}

	private static void waitMillis(long i) {

		try {
			Thread.currentThread().sleep(i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public static void printWorkingMemory(KieSession ksession) {
		System.out.println("--------BEGIN Working Memory-------------");
		for (Object obj:ksession.getObjects())
			System.out.println(obj);
		System.out.println("--------END Working Memory-------------");
	}

	public static class Customer {
		private String name;

		private double    discount;
		private double    shoppingTotal;

		public double getShoppingTotal() {
			return shoppingTotal;
		}

		public void setShoppingTotal(double shoppingTotal) {
			this.shoppingTotal = shoppingTotal;
		}

		public Customer(String name,
				int discount) {
			this.name = name;
			this.discount = discount;
		}

		public String getName() {
			return name;
		}

		public double getDiscount() {
			return discount;
		}

		public void setDiscount(double discount) {
			this.discount = discount;
		}
		public String toString(){
			return "Customer( "+name+","+discount+")";
		}

	}

	public static class Discount {
		private Customer customer;
		private double      amount;

		public Discount(Customer customer,
				double amount) {
			this.customer = customer;
			this.amount = amount;
		}

		public Customer getCustomer() {
			return customer;
		}

		public double getAmount() {
			return amount;
		}

		public String toString(){
			return "Discount( "+customer+","+amount+")";
		}

	}

	public static class Product {
		private String name;
		private float  price;

		public Product(String name,
				float price) {
			this.name = name;
			this.price = price;
		}

		public String getName() {
			return name;
		}

		public float getPrice() {
			return price;
		}

		public String toString(){
			return "Product("+name+","+price+")";
		}

	}
	

	public static class BotDetected{
	

		public BotDetected() {
		}


		public String toString(){
			return "BotDetected()";
		}
	}


	public static class Purchase{
		private Customer customer;
		private Product  product;

		public Purchase(Customer customer,
				Product product) {
			this.customer = customer;
			this.product = product;
		}

		public Customer getCustomer() {
			return customer;
		}

		public Product getProduct() {
			return product;
		}

		public String toString(){
			return "Purchase( "+customer+","+product+")";
		}
	}
}
